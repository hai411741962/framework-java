package net.getbang.kafka.annotation;

import net.getbang.kafka.config.MyProducerConfig;
import org.springframework.context.annotation.Import;

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
@Import(MyProducerConfig.class)
public @interface EnableKafkaProducer {

}
