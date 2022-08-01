package net.getbang.kafka.annotation;

import net.getbang.kafka.config.MyConsumerConfig;
import net.getbang.kafka.controller.KafkaManageController;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;

import java.lang.annotation.*;

/**
 * 自定义feign注解
 * 添加basePackages路径
 *
 * @author ruoyi
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@EnableKafka
@Import({ConsumerConfig.class, KafkaManageController.class})
public @interface EnableKafkaConsumer {

}
