package net.getbang.cache.aspect;

import net.getbang.cache.annotation.MyCacheEvict;
import net.getbang.cache.annotation.MyCacheable;
import net.getbang.core.util.SPELUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义缓存切面2
 */
@Component
@Aspect
@Log4j2
public class CacheAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 定义缓存逻辑
     */
    @Around("@annotation(net.getbang.cache.annotation.MyCacheable)")
    public Object cache(ProceedingJoinPoint pjp) throws IOException {
        Object result = null;

        Method method = getMethod(pjp);
        MyCacheable cacheable = method.getAnnotation(MyCacheable.class);

        String key = parseKey(cacheable.key(), method, pjp.getArgs());

        //使用redis 的hash进行存取，易于管理
        result = redisTemplate.opsForValue().get(key);

        if (result == null) {
            try {
                // 空或者null不存缓存
                if (ObjectUtils.isEmpty(result = pjp.proceed())) {
                    log.warn("Cache key[" + key + "] value is null");
                    return result;
                }
                redisTemplate.opsForValue().set(key, result);
                redisTemplate.expire(key, cacheable.expireTime(), TimeUnit.SECONDS);
            } catch (Throwable e) {
                log.error("", e);
            }
        }
        return result;
    }

    /**
     * 定义清除缓存逻辑
     */
    @Around(value = "@annotation(net.getbang.cache.annotation.MyCacheEvict)")
    public void evict(ProceedingJoinPoint pjp) {
        Method method = getMethod(pjp);
        MyCacheEvict cacheEvict = method.getAnnotation(MyCacheEvict.class);

        String key = parseKey(cacheEvict.key(), method, pjp.getArgs());
        redisTemplate.delete(key);
    }

    /**
     * 获取被拦截方法对象
     * <p/>
     * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象
     * 而缓存的注解在实现类的方法上
     * 所以应该使用反射获取当前对象的方法对象
     */
    public Method getMethod(ProceedingJoinPoint pjp) {
        //获取参数的类型
        Object[] args = pjp.getArgs();
        Class[] argTypes = new Class[pjp.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = null;
        try {
            method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return method;

    }

    private String parseKey(String key, Method method, Object[] args) {
        Pattern pattern = Pattern.compile("(?<=\\{)[^\\}]+");
        Matcher matcher = pattern.matcher(key);
        while (matcher.find()) {
            if (ObjectUtil.isEmpty(SPELUtil.getStringMethodValue(matcher.group(), method, args))){
                key = key.replace("{" + matcher.group() + "}", "-");
            }else{
                key = key.replace("{" + matcher.group() + "}", SPELUtil.getStringMethodValue(matcher.group(), method, args));
            }

        }
        return key;
    }

}
