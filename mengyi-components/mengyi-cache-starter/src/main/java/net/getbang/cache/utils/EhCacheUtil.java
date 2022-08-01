package net.getbang.cache.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;


@Component
@Slf4j
public class EhCacheUtil {

    public static final String EW_EHCACHE = "EW_EHCACHE";

    @Autowired
    @Lazy
    private CacheManager cacheManager;

    // 默认的缓存存在时间（秒）
    private static final int DEFAULT_LIVE_SECOND = 20 * 60;

    /**
     * 是否存在key
     */
    public boolean hasKey(String key){
        Cache cache = cacheManager.getCache(CacheNames.EW_EHCACHE);
        Element element = cache.getQuiet(key);
        return element != null;
    }

    public void removeAll(){
        try {
            Cache cache = cacheManager.getCache(EhCacheUtil.EW_EHCACHE);
            cache.removeAll();
        } catch (Exception e) {
            log.error("删除缓存数据失败：{}",e.getMessage());
        }
    }

    /**
     * 返回缓存中所有元素键的列表，无论它们是否已过期。
     */
    public List getKeys(){
        Cache cache = cacheManager.getCache(CacheNames.EW_EHCACHE);
        return cache.getKeys();
    }


    /**
     * 添加缓存
     * @param key
     * @param value
     * @param timeToLiveSeconds 缓存生存时间（秒）
     */
    public void set(String cacheName,String key,Object value,int timeToLiveSeconds){
      //  HashMap<String, String> map = new HashMap<>();
        Cache cache = cacheManager.getCache(cacheName);
        Element element = new Element(
                key, value,
                0,// timeToIdleSeconds=0
                timeToLiveSeconds);
        cache.put(element);
    }
    /**
     * 删除缓存数据
     * @param key
     */
    public boolean delete(String cacheName,String key) {
        try {
            Cache cache = cacheManager.getCache(cacheName);
            return cache.remove(key);
        } catch (Exception e) {
            log.error("删除缓存数据失败：{}",e.getMessage());
            return false;
        }
    }
    /**
     * 获取缓存
     * @param key
     * @return
     */
    public <T> T get(String cacheName,String key, Class<T> clazz){
        Cache cache = cacheManager.getCache(cacheName);
        if (ObjectUtils.isEmpty(cache)){
            log.info("cacheManager缓存调用空key:{}",cacheName);
            return null;
        }
        Element element = cache.get(key);
        if(element == null){
            return null;
        }
        return (T)element.getObjectValue();
    }
    /**
     * 获取缓存
     * @param key
     * @return
     */
    @SneakyThrows
    public <T> T get(String cacheName,String key, Callable<? extends T> callable){
        long start3 = System.currentTimeMillis();
        Cache cache = cacheManager.getCache(cacheName);
        Element element = cache.get(key);
        if(element == null){
            T ret = callable.call();
            this.set(key, ret);
            return ret;
        }
        long end3 = System.currentTimeMillis();
        log.debug("查询本地缓存耗时{}ms",end3-start3);
        return (T)element.getObjectValue();
    }
    /**
     * 添加缓存
     * @param key
     * @param value
     */
    public void set(String key,Object value){
        Cache cache = cacheManager.getCache(CacheNames.EW_EHCACHE);
        Element element = new Element(
                key, value,
                0,// timeToIdleSeconds
                DEFAULT_LIVE_SECOND);
        cache.put(element);
    }
    /**
     * 获取缓存
     * @param key
     * @return
     */
    public <T> T get(String key, Class<T> clazz){
        Cache cache = cacheManager.getCache(EhCacheUtil.EW_EHCACHE);
        Element element = cache.get(key);
        if(element == null){
            return null;
        }
        return (T)element.getObjectValue();
    }
    /**
     * 删除缓存数据
     * @param key
     */
    public boolean delete(String key) {
        try {
            Cache cache = cacheManager.getCache(EhCacheUtil.EW_EHCACHE);
            return cache.remove(key);
        } catch (Exception e) {
            log.error("删除缓存数据失败：{}",e.getMessage());
            return false;
        }
    }
    @SneakyThrows
    public <T> T get(String cacheName,String key, Callable<? extends T> callable, int timeToLiveSeconds){
        Cache cache = cacheManager.getCache(cacheName);
        Element element = cache.get(key);
        if(element == null){
            T ret = callable.call();
            this.set(cacheName,key, ret, timeToLiveSeconds);
            return ret;
        }
        return (T)element.getObjectValue();
    }
    /**
     * 获取缓存
     * @param key
     * @param timeToLiveSeconds 缓存生存时间（秒）
     */
    @SneakyThrows
    public <T> T get(String cacheName,String key, Callable<? extends T> callable, int timeToLiveSeconds,Object emptyObject){
        Cache cache = cacheManager.getCache(cacheName);
        if (ObjectUtils.isEmpty(cache)){
            log.info("cacheManager缓存调用空key:{}",cacheName);
            return null;
        }
        Element element = cache.get(key);
        if(element == null){
            T ret = null;
            try {
                ret = callable.call();
            } catch (Exception e) {
                log.error("缓存调用异常key:{},异常信息:{}",key,e);
                return (T)emptyObject;
            } if(ret==null || (ret instanceof List && ((List) ret).size() == 0)) {
                timeToLiveSeconds = 10;
                ret = (T)emptyObject;
            }
            this.set(cacheName,key, ret, timeToLiveSeconds);
            return ret;
        }
        return (T)element.getObjectValue();
    }
    /**
     * 获取缓存
     * @param key
     * @param timeToLiveSeconds 缓存生存时间（秒）
     */
    @SneakyThrows
    public <T> T get(String key, Callable<? extends T> callable, int timeToLiveSeconds,Object emptyObject){
        Cache cache = cacheManager.getCache(EhCacheUtil.EW_EHCACHE);
        Element element = cache.get(key);
        if(element == null){
            T ret = null;
            try {
                ret = callable.call();
            } catch (Exception e) {
                log.error("缓存调用异常key:{},异常信息:{}",key,e);
                return (T)emptyObject;
            } if(ret==null || (ret instanceof List && ((List) ret).size() == 0)) {
                timeToLiveSeconds = 10;
                ret = (T)emptyObject;
            }
            this.set(key, ret, timeToLiveSeconds);
            return ret;
        }
        return (T)element.getObjectValue();
    }
    /**
     * 添加缓存
     * @param key
     * @param value
     * @param timeToLiveSeconds 缓存生存时间（秒）
     */
    public void set(String key,Object value,int timeToLiveSeconds){
        HashMap<String, String> map = new HashMap<>();
        Cache cache = cacheManager.getCache(EhCacheUtil.EW_EHCACHE);
        Element element = new Element(
                key, value,
                0,// timeToIdleSeconds=0
                timeToLiveSeconds);
        cache.put(element);
    }

    /**
     * 获取缓存
     * @param key
     * @param timeToLiveSeconds 缓存生存时间（秒）
     */
    @SneakyThrows
    public <T> T get(String key, Callable<? extends T> callable, int timeToLiveSeconds){
        Cache cache = cacheManager.getCache(EhCacheUtil.EW_EHCACHE);
        Element element = cache.get(key);
        if(element == null){
            T ret = callable.call();
            this.set(key, ret, timeToLiveSeconds);
            return ret;
        }
        return (T)element.getObjectValue();
    }
}
