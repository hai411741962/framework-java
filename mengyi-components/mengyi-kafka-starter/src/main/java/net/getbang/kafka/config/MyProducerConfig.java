package net.getbang.kafka.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
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
public class MyProducerConfig {
    @Value("${spring.kafka.bootstrap-servers:#{null}}")
    private String bootstrapServers;

    @Value("${spring.kafka.security-protocol-config:#{null}}")
    private String securityProtocolConfig;

    /**
     * 默认
     *
     * @return
     */
    @Bean(name = "kafkaTemplate")
    @ConditionalOnMissingBean(name = "kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<String, String>(new DefaultKafkaProducerFactory<>(configs()));
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(configs());
    }

    /**
     * 生产者配置
     *
     * @return
     */
    private Map<String, Object> configs() {
        Assert.notNull(bootstrapServers, "缺少spring.kafka.bootstrap-servers，请补充");
        Map<String, Object> props = new HashMap<>();
        // 连接地址
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // 键的序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 值的序列化方式
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        // 重试，0为不启用重试机制
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 300);
        // 控制批处理大小，单位为字节
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // 批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        // 生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        if (StringUtils.isNotBlank(securityProtocolConfig)) {
            props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocolConfig);
        }
        return props;
    }
}
