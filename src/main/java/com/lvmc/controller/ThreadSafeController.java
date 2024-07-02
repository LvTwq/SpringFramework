package com.lvmc.controller;

import cn.hutool.http.HttpRequest;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.lvmc.entity.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * @author 吕茂陈
 */
@Slf4j
@RestController
@RequestMapping("thread")
public class ThreadSafeController {

    /**
     * ThreadLocal 是利用独占资源的方式，来解决线程安全问题
     * 代表需要在线程中保存的用户信息
     */
    private static final ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    /**
     * 线程个数
     */
    private static int THREAD_COUNT = 10;

    /**
     * 总元素数量
     */
    private static int ITEM_COUNT = 1000;


    /**
     * tomcat 重用线程，可能首次从 ThreadLocal 获取的值是之前其他用户的请求遗留的值。这时，ThreadLocal 中的用户信息就是其他用户的信息
     *
     * @param userId
     * @return
     */
    @GetMapping("threadlocal/wrong")
    public Map<String, String> wrong(@RequestParam("userId") Integer userId) {
        // 设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + "：" + currentUser.get();
        // 设置用户信息到ThreadLocal
        currentUser.set(userId);
        // 设置用户信息之后再查询一次ThreadLocal中的用户信息
        String after = Thread.currentThread().getName() + "：" + currentUser.get();
        // 汇总输出两次查询结果
        Map<String, String> result = Maps.newHashMap();
        result.put("before", before);
        result.put("after", after);
        return result;
    }

    @GetMapping("threadlocal/right")
    public Map<String, String> right(@RequestParam("userId") Integer userId) {
        // 设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + "：" + currentUser.get();
        // 设置用户信息到ThreadLocal
        currentUser.set(userId);
        try {
            // 设置用户信息之后再查询一次ThreadLocal中的用户信息
            String after = Thread.currentThread().getName() + "：" + currentUser.get();
            // 汇总输出两次查询结果
            Map<String, String> result = Maps.newHashMap();
            result.put("before", before);
            result.put("after", after);
            return result;
        } finally {
            // 显式清除ThreadLocal中的数据，新的请求过来即使使用了之前的线程也不会获取到错误的用户信息了
            currentUser.remove();
        }
    }


    /**
     * 在每一个线程的代码逻辑中先通过size方法拿到当前元素的数量，计算 ConcurrentHashMap目前还需要补充多少元素，然后通过putAll方法补充缺少的元素
     *
     * @return
     * @throws InterruptedException
     */
    @GetMapping("current/wrong")
    public String wrong() throws InterruptedException {
        ConcurrentHashMap<String, Long> concurrentHashMap = getData(ITEM_COUNT - 100);
        // 初始化900个元素
        log.info("初始化元素数量{}", concurrentHashMap.size());

        ForkJoinPool forkJoinPool = new ForkJoinPool(THREAD_COUNT);
        // 使用线程池并发处理
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, 10).parallel().forEach(i -> {
            int gap = ITEM_COUNT - concurrentHashMap.size();
            log.info("gap 大小：{}", gap);
            // 补充元素
            concurrentHashMap.putAll(getData(gap));
        }));
        // 平滑的关闭ExecutorService，调用此方法时，ExecutorService停止接收新的任务，并且等待所有任务（提交正在执行、提交未执行）完成，当所有任务执行完毕，线程池被关闭
        forkJoinPool.shutdown();
        // 用于设定超时时间即单位，当等待超过设定时间时，会监测ExecutorService是否已经关闭，关闭返回true，否则返回false，一般和shutdown()方法组合使用
        if (forkJoinPool.awaitTermination(1, TimeUnit.HOURS)) {
            log.info("线程池关闭！");
        } else {
            log.error("线程池没有关闭！");
        }
        log.info("完成后，大小：{}", concurrentHashMap.size());
        return "OK";
    }

    /**
     * 用于获取一个指定元素数量模拟数据的 ConcurrentHashMap
     *
     * @param count
     * @return
     */
    private ConcurrentHashMap<String, Long> getData(int count) {
        return LongStream.rangeClosed(1, count)
                .boxed()
                .collect(Collectors.toConcurrentMap(i -> UUID.randomUUID().toString(), Function.identity(),
                        (o1, o2) -> o1, ConcurrentHashMap::new));
    }


    static Map<String, Item> items = new HashMap<>();

    /**
     * 创建购物车
     *
     * @return
     */
    private static List<Item> createCart() {
        return IntStream.rangeClosed(1, 3)
                .mapToObj(i -> "item" + ThreadLocalRandom.current().nextInt(items.size()))
                .map(name -> items.get(name)).collect(Collectors.toList());
    }

    /**
     * 下单
     *
     * @param order
     * @return
     */
    private static boolean createOrder(List<Item> order) {
        // 存放所有获得的锁
        List<ReentrantLock> locks = new ArrayList<>();

        for (Item item : order) {
            try {
                // 获得锁10秒超时
                if (item.lock.tryLock(10, TimeUnit.SECONDS)) {
                    locks.add(item.lock);
                } else {
                    locks.forEach(ReentrantLock::unlock);
                    return false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 锁全部拿到后执行扣减库存业务逻辑
        try {
            order.forEach(item -> item.remaining--);
        } finally {
            locks.forEach(ReentrantLock::unlock);
        }
        return true;
    }

    @GetMapping("wrong")
    public long wrong2() {
        long begin = System.currentTimeMillis();
        // 并发进行100次下单操作，统计成功次数
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart();
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("成功次数：{}，剩下商品：{}，耗时：{}毫秒，items：{}", success, items.values().stream()
                .map(item -> item.remaining).reduce(0, Integer::sum), System.currentTimeMillis() - begin, items);
        return success;
    }

    @GetMapping("right")
    public long right() {
        long begin = System.currentTimeMillis();
        long success = IntStream.rangeClosed(1, 100).parallel()
                .mapToObj(i -> {
                    List<Item> cart = createCart().stream().sorted(Comparator.comparing(Item::getName)).collect(Collectors.toList());
                    return createOrder(cart);
                })
                .filter(result -> result)
                .count();
        log.info("成功次数：{}，剩下商品：{}，耗时：{}毫秒，items：{}", success, items.values().stream()
                .map(item -> item.remaining).reduce(0, Integer::sum), System.currentTimeMillis() - begin, items);
        return success;
    }

    private static final ExecutorService DOWNLOAD_CENTER_EXECUTOR =
            new ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(512),
                    new ThreadFactoryBuilder()
                            .setUncaughtExceptionHandler((thread, throwable) -> log.error("{} 线程异常！", thread, throwable))
                            .setNameFormat("enlink-download-center-%d").build());

    @GetMapping("single")
    public void single() {
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            DOWNLOAD_CENTER_EXECUTOR.execute(() -> {
                String url = "http://daemon.ztrust.to:8001/filecenter/uploadFile";
                HttpRequest httpRequest = HttpRequest.post(url)
                        .setChunkedStreamingMode(1024 * 1024)
                        .form(Map.of("file", new File("sql/order.sql")));
                String result = httpRequest.execute().body();
                log.info("调用下载中心接口：{}，上传文件结果：{}", url, result);
                log.info("{}", finalI);
            });
        }
    }

}
