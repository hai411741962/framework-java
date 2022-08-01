package net.getbang.cache.aspect;


import net.getbang.cache.annotation.DistributedLock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 分布式锁注解实现
 * @author chenjun
 */
@Aspect
@Component
@AllArgsConstructor
@Order(1)
@Slf4j
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    private final static Pattern PATH_PATTERN = Pattern.compile("(?<=\\{)[^\\}]+");

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint pjp, DistributedLock distributedLock) throws NoSuchMethodException {

        String key = this.parseKey(distributedLock.keyPrefix(), distributedLock.value(), getMethod(pjp), pjp.getArgs());

        RLock lock = redissonClient.getLock(key);
        boolean hasLock = false;
        try {
            hasLock = lock.tryLock(distributedLock.waitTime(), distributedLock.expire(), distributedLock.timeUnit());
            if (hasLock) {
                return pjp.proceed();
            }
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
            //todo
            throw new RuntimeException(throwable.getMessage());
        } finally {
            if (hasLock) {
                lock.unlock();
            }
        }
        //todo
        throw new RuntimeException("获取分布式锁失败");
    }

    public Method getMethod(ProceedingJoinPoint pjp) throws NoSuchMethodException {
        Class targetClass = pjp.getTarget().getClass();
        String methodName = pjp.getSignature().getName();
        Class[] parameterTypes = ((MethodSignature)pjp.getSignature()).getMethod().getParameterTypes();
        return targetClass.getMethod(methodName, parameterTypes);
    }

    private String parseKey(String keyPre, String key, Method method, Object[] args) {
        String value;
        for(Matcher matcher = PATH_PATTERN.matcher(key); matcher.find(); key = key.replace("{" + matcher.group() + "}", value == null ? "-" : value)) {
            value = getStringMethodValue(matcher.group(), method, args);
        }

        return "local"+ keyPre + ":" + key;
    }

    public static String getStringMethodValue(String key, Method method, Object[] args) {
        return (String)getMethodValue(key, method, args, String.class);
    }

    public static <T> T getMethodValue(String key, Method method, Object[] args, Class<T> claszz) {
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for(int i = 0; i < paraNameArr.length; ++i) {
            context.setVariable(paraNameArr[i], args[i]);
        }

        return parser.parseExpression(key).getValue(context, claszz);
    }
}
