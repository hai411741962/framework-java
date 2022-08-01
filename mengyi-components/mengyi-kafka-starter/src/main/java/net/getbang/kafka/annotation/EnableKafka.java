package net.getbang.kafka.annotation;

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
@EnableKafkaProducer
@EnableKafkaConsumer
public @interface EnableKafka {

}
