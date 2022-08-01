package net.getbang.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 * @author chenjun
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /**
     * 分布式锁key前缀
     */
    String keyPrefix() default "";

    /**
     * key el表达式
     * @return
     */
    String value() default "";

    /**
     * 获取锁等待时间（默认两秒，还没获取到锁即放弃）
     */
    long waitTime() default 2;

    /**
     * 过期时长，防止一直占用锁
     */
    long expire() default 10;

    /**
     * 过期时长单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
