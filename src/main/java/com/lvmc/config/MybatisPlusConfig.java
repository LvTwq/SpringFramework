package com.lvmc.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/10/19 16:07
 */
@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MybatisPlusConfig {


/*    @ConditionalOnClass(value = {MybatisPlusInterceptor.class})
    @ConditionalOnMissingBean
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        paginationInnerInterceptor.setOverflow(true);
        mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
        return mybatisPlusInterceptor;
    }*/
}
