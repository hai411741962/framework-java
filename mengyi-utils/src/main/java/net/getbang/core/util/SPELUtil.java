package net.getbang.core.util;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

public class SPELUtil {
    /**
     * 通过Spring表达式获取方法上参数的String值
     *
     * @param key    Spring表达式
     * @param method 目标方法
     * @param args   目标参数
     * @return
     */
    public static String getStringMethodValue(String key, Method method, Object[] args) {
        return getMethodValue(key, method, args, String.class);
    }
    /**
     * 通过Spring表达式获取方法上参数的值，支持泛型
     *
     * @param key    Spring表达式
     * @param method 目标方法
     * @param args   目标参数
     * @param claszz 返回值Class类型
     * @param <T>
     * @return
     */
    public static <T> T getMethodValue(String key, Method method, Object[] args, Class<T> claszz) {
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);

        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, claszz);
    }
}
