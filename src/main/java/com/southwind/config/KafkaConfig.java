

package com.southwind.config;

import java.util.HashMap;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author 吕茂陈
 * @description
 * @date 2022/12/15 15:15
 */
@Configuration
public class KafkaConfig {


    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;
    @Value("${spring.kafka.producer.retries}")
    private String retries;
    @Value("${spring.kafka.producer.batch-size}")
    private String batchSize;
    @Value("${spring.kafka.producer.client-id}")
    private String clientId;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        HashMap<String, Object> configs = new HashMap<>(7);
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        configs.put(ProducerConfig.RETRIES_CONFIG, retries);
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        //设置序列化
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        //设置自定义分区
//        configs.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class);
        configs.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
        DefaultKafkaProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(configs);
        return new KafkaTemplate<>(producerFactory);
    }


    @Bean
    public KafkaConsumer<String, String> kafkaConsumer() {
        HashMap<String, Object> configs = new HashMap<>(7);
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new KafkaConsumer<>(configs);
    }

    @Bean
    public NewTopic myTopic() {
        return new NewTopic("myTopic", 1, Short.parseShort("1"));
    }

    @Bean
    public NewTopic myTopic2() {
        return new NewTopic("myTopic2", 2, Short.parseShort("1"));
    }

    @Bean
    public NewTopic myTopic3() {
        return new NewTopic("myTopic3", 3, Short.parseShort("1"));
    }
}
