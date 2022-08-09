package com.southwind.utils;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

/**
 * @author 吕茂陈
 * @date 2022-07-28 10:25
 */
@UtilityClass
public class MdcUtil {

    private static final String TRACE_ID = "TRACE_ID";


    /**
     * 请求到了应用服务器，tomcat 会从线程池中分配一个线程去处理该请求
     * 而 MDC 底层使用 ThreadLocal 存储和获取traceId
     * 那么在请求的整个过程中，保存到 MDC 的 ThreadLocal 的参数，也是该线程独享的，所以不会有线程安全问题
     *
     * @return
     */
    public static String get() {
        return MDC.get(TRACE_ID);
    }

    public static void add(String value) {
        MDC.put(TRACE_ID, value);
    }
}
