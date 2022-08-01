package net.getbang.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: CJ
 * @date: 2022/5/19
 */
@Configuration
@ConditionalOnProperty("spring.kafka.bootstrap-servers")
public class MyConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers:#{null}}")
    private String service;

    @Value("${spring.application.name}")
    private String groupId;

    @Value("${spring.kafka.consumer.enable-auto-commit:true}")
    private String autoCommit;

    @Value("${spring.kafka.consumer.auto-commit-interval:1000}")
    private String interval;

    @Value("${spring.kafka.consumer.key-deserializer:org.apache.kafka.common.serialization.StringDeserializer}")
    private String keyDeserializer;

    @Value("${spring.kafka.consumer.value-deserializer:org.apache.kafka.common.serialization.StringDeserializer}")
    private String valueDeserializer;


    @Value("${spring.kafka.consumer.auto-offset-reset:latest}")
    private String offsetReset;


    /**
     * 默认kafka实例
     *
     * @return kafka实例
     */
    @Bean(name = "kafkaListener")
    @ConditionalOnMissingBean(name = "kafkaListener")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(false));
        return factory;
    }

    /**
     * 获取kafka实例，该实例为批量消费
     *
     * @return kafka实例
     */
    @Bean(name = "batchKafkaListener")
    @ConditionalOnMissingBean(name = "batchKafkaListener")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> batchKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory(true));
        // 连接池中消费者数量
        factory.setConcurrency(4);
        // 是否并发消费
        factory.setBatchListener(true);
        return factory;
    }

    /**
     * 获取kafka配置
     *
     * @return 配置map
     */
    private Map<String, Object> consumerConfig() {
        Assert.notNull(service, "缺少spring.kafka.bootstrap-servers，请补充");
        Assert.notNull(groupId, "缺少hz.systemName，请确认");
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, service);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, interval);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializer);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
        return props;
    }

    /**
     * 获取工厂
     * @param isBatch 是否批量消费
     * @return kafka工厂
     */
    private ConsumerFactory<String, String> consumerFactory(boolean isBatch) {
        Map<String, Object> props = consumerConfig();
        // 单次poll的数量,批量消费时配置
        if (isBatch) {
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);
        }
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
