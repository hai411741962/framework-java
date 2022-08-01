package net.getbang.cache.configuration;



import net.getbang.cache.aspect.CacheAspect;
import net.getbang.cache.config.CachingConfiguration;
import net.getbang.cache.config.RedisOperationsConfiguration;
import net.getbang.cache.config.RedisTemplateConfiguration;
import net.getbang.cache.utils.EhCacheUtil;
import net.getbang.cache.utils.RedisUtil;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisOperations;


/**
 * @author chenjun
 */
@Configuration
@ConditionalOnClass(RedisOperations.class)
@AutoConfigureBefore(RedissonAutoConfiguration.class)
@Import(value = {CachingConfiguration.class, RedisTemplateConfiguration.class,
        RedisOperationsConfiguration.class, RedisUtil.class, EhCacheUtil.class, CacheAspect.class})
public class CacheAutoConfiguration {

}
