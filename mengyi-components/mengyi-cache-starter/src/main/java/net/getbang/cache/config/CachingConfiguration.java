package net.getbang.cache.config;



import net.getbang.cache.utils.CacheManagerName;
import net.getbang.cache.utils.CacheNames;
import lombok.val;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.SizeOfPolicyConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author chenjun
 */
@EnableCaching
@Configuration
@ConfigurationProperties(prefix = "ew.cache")
public class CachingConfiguration extends CachingConfigurerSupport {

    /**默认eh缓存，false则为redis*/
    private boolean ehCache = true;

    /**
     * 配置缓存管理器
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean(CacheManagerName.REDIS_CACHE_MANAGER)
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        redisCacheConfiguration = redisCacheConfiguration
                // 设置缓存有效期一小时
                .entryTtl(Duration.ofHours(1));
        return RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }

    @Bean(CacheManagerName.EH_CACHE_MANAGER)
    public CacheManager ehCacheManager(){
        CacheConfiguration config15M = new CacheConfiguration();
        config15M.setName(CacheNames.CACHE_FIFTEEN_MINS);
        config15M.setMemoryStoreEvictionPolicy("LFU");//最少使用
        config15M.setTimeToLiveSeconds(TimeUnit.MINUTES.toSeconds(15));
        config15M.setMaxEntriesLocalHeap(20000);
        config15M.setClassLoader(Thread.currentThread().getContextClassLoader());

        CacheConfiguration config30M = new CacheConfiguration();
        config30M.setName(CacheNames.CACHE_THIRTY_MINS);
        config30M.setMemoryStoreEvictionPolicy("LFU");//最少使用
        config30M.setTimeToLiveSeconds(TimeUnit.MINUTES.toSeconds(30));
        config30M.setMaxEntriesLocalHeap(20000);
        config30M.setClassLoader(Thread.currentThread().getContextClassLoader());

        CacheConfiguration config60M = new CacheConfiguration();
        config60M.setName(CacheNames.CACHE_SIXTY_MINS);
        config60M.setMemoryStoreEvictionPolicy("LFU");//最少使用
        config60M.setTimeToLiveSeconds(TimeUnit.MINUTES.toSeconds(60));
        config60M.setMaxEntriesLocalHeap(20000);
        config60M.setClassLoader(Thread.currentThread().getContextClassLoader());

        // 设置ehcache配置文件，获取CacheManager
        net.sf.ehcache.config.Configuration configuration = new net.sf.ehcache.config.Configuration();
        configuration.setName("ehcache");
        configuration.addCache(config15M);
        configuration.addCache(config30M);
        configuration.addCache(config60M);


        SizeOfPolicyConfiguration sizeOfPolicyConfiguration = new SizeOfPolicyConfiguration();
        sizeOfPolicyConfiguration.setMaxDepth(20000);
        sizeOfPolicyConfiguration.setMaxDepthExceededBehavior("abort");
        configuration.sizeOfPolicy(sizeOfPolicyConfiguration);
        // 将CacheManager注册为bean，供缓存工具类使用
        val cachemanager = net.sf.ehcache.CacheManager.newInstance(configuration);
        return new EhCacheCacheManager(cachemanager);
    }

    @Bean
    @Primary
    public CacheManager cacheManager(CacheManager ehCacheManager,RedisCacheManager redisCacheManager) {
        return ehCache ? ehCacheManager : redisCacheManager;
    }


    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) ->
             target.getClass().getSimpleName() + "_"
                    + method.getName() + "_"
                    + StringUtils.arrayToDelimitedString(params, "_")
        ;
    }

    public boolean isEhCache() {
        return ehCache;
    }

    public void setEhCache(boolean ehCache) {
        this.ehCache = ehCache;
    }
}
