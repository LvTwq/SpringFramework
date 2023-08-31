

package com.southwind.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author 吕茂陈
 * @description
 * @date 2023/3/31 10:38
 */
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;


    private final StepBuilderFactory stepBuilderFactory;


}

