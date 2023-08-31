

package com.southwind.controller;

import cn.hutool.json.JSONUtil;
import com.southwind.entity.AdminLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.southwind.service.EsComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 吕茂陈
 * @description
 * @date 2022/12/19 17:06
 */
@Slf4j
@RestController
@RequestMapping("elk")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EsController {

    private final RestHighLevelClient restHighLevelClient;
    private final EsComponent esComponent;

    @GetMapping("admin")
    public List<AdminLog> allRes() throws IOException {
        //指定查询索引名
        SearchRequest searchRequest = new SearchRequest().indices("admin-2023.08");
        //构造查询对象
        //设置时间查询以及匹配查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("account", "lmc"))
            .filter(QueryBuilders.rangeQuery("mtime").gte("1685073234000").lte("1688004333027"));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder)/*.sort("@mtime.keyword", SortOrder.DESC)*/;
        //开始点
        searchSourceBuilder.from(0);
        //指定单次查询个数
        searchSourceBuilder.size(100);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //拿到查询出的数据
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        List<Map<String, Object>> buckets = Arrays.stream(searchHits).map(SearchHit::getSourceAsMap)
            .collect(Collectors.toList());
        List<AdminLog> list = new ArrayList<>();
        for (Map<String, Object> bucket : buckets) {
            AdminLog respLogVo = JSONUtil.toBean(JSONUtil.toJsonStr(bucket), AdminLog.class);
            list.add(respLogVo);
        }
        return list;
    }


    @PostMapping("admin")
    public void addAdminLog() {
        esComponent.addBulkData(List.of(new AdminLog()));
    }

/*    private final ArticleRepository articleRepository;
    private final ElasticsearchRestTemplate elasticsearchTemplate;
    private final ResRepository resRepository;



    @GetMapping("resEntity")
    public Iterable<ResEntity> all() {
        return resRepository.findAll();
    }


    @PostMapping("articleEntity")
    public List<ArticleEntity> query(@RequestBody ArticleEntity article) {
        Page<ArticleEntity> page = articleRepository.findByName(article.getName(), PageRequest.of(1, 10));
        return page.toList();
    }

    *//**
     * 根据文档id查询数据
     *
     * @param id 文档id
     * @return 文档详情
     *//*
    @GetMapping("/byId")
    public String findById(@RequestParam String id) {
        return articleRepository.findById(id).map(ArticleEntity::getContent).orElse("");
    }

    *//**
     * 保存文档信息
     *
     * @param article 文档详情
     * @return 保存的文档信息
     *//*
    @PostMapping("/saveArticle")
    public String saveArticle(@RequestBody ArticleEntity article) {
        ArticleEntity result = articleRepository.save(article);
        return result.toString();
    }


    @PostMapping("/batchSaveArticle/{min}/{max}")
    public void batchSaveArticle(@PathVariable int min, @PathVariable int max) {
        List<ArticleEntity> collect = IntStream.rangeClosed(min, max).mapToObj(i ->
            ArticleEntity.builder().*//*id(String.valueOf(i)).*//*age(i).name("name" + i + "name")
                .content("content" + i + "content")
                .title("title" + i + "title")
                .userId(i)
                .build()
        ).collect(Collectors.toList());
        articleRepository.saveAll(collect);
    }


    //    @Scheduled(fixedRate = 1000)
    public void saveSchedule() {
        List<ArticleEntity> collect = IntStream.rangeClosed(0, 1000).mapToObj(i ->
            ArticleEntity.builder()
                .age(i)
                .name("name" + i + "name")
                .content("content" + i + "content")
                .title("title" + i + "title")
                .userId(i)
                .build()
        ).collect(Collectors.toList());
        articleRepository.saveAll(collect);
    }

    @DeleteMapping("/deleteById")
    public String deleteArticle(@RequestParam String id) {
        articleRepository.deleteById(id);
        return "success";
    }

    *//**
     * 分页查询
     *
     * @param pageNum  页码，从1开始
     * @param pageSize 分页大小
     * @return 查询结果
     *//*
    @GetMapping("/queryPage")
    public SearchPage<ArticleEntity> queryPage(@RequestParam int pageNum, @RequestParam int pageSize) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "mill lane"));

        PageRequest pageRequest = PageRequest.of(pageNum - 1, pageSize);
        queryBuilder.withPageable(pageRequest.previousOrFirst());

        SearchHits<ArticleEntity> searchHitsResult = elasticsearchTemplate.search(queryBuilder.build(),
            ArticleEntity.class);
        // 获取分页数据
        return SearchHitSupport.searchPageFor(searchHitsResult,
            pageRequest.previousOrFirst());
    }

    *//**
     * 滚动查询
     *
     * @param scrollId 滚动id
     * @param pageSize 分页大小
     * @return 查询结果
     *//*
    @GetMapping(value = "/scrollQuery")
    public Map<String, String> scroll(@RequestParam String scrollId, @RequestParam Integer pageSize) {
        NativeSearchQuery query = new NativeSearchQuery(new BoolQueryBuilder());
        query.setPageable(PageRequest.of(0, pageSize));
        SearchHits<ArticleEntity> searchHits;
        if (CharSequenceUtil.isEmpty(scrollId) || scrollId.equals("0")) {
            // 开启一个滚动查询,设置该scroll上下文存在60s
            // 同一个scroll上下文,只需要设置一次query(查询条件)
            searchHits = elasticsearchTemplate.searchScrollStart(60000, query, ArticleEntity.class,
                IndexCoordinates.of("article"));
            if (searchHits instanceof SearchHitsImpl) {
                scrollId = ((SearchHitsImpl<?>) searchHits).getScrollId();
            }
        } else {
            // 继续滚动
            searchHits = elasticsearchTemplate.searchScrollContinue(scrollId, 60000, ArticleEntity.class,
                IndexCoordinates.of("article"));
        }
        List<ArticleEntity> articles = searchHits.getSearchHits().stream().map(SearchHit::getContent)
            .collect(Collectors.toList());
        if (articles.isEmpty()) {
            // 结束滚动
            elasticsearchTemplate.searchScrollClear(Collections.singletonList(scrollId));
            scrollId = null;
        }
        Map<String, String> result;
        if (Objects.isNull(scrollId)) {
            result = new HashMap<>(2);
            result.put("articles", articles.toString());
            result.put("message", "已到末尾");
        } else {
            result = new HashMap<>();
            result.put("count", String.valueOf(searchHits.getTotalHits()));
            result.put("pageSize", String.valueOf(articles.size()));
            result.put("articles", articles.toString());
            result.put("scrollId", scrollId);
        }
        return result;
    }*/
}
