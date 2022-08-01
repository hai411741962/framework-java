package net.getbang.rabbitmq;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 配置rabbitmq 开关
 */

public class ConsumerRunCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String property = conditionContext.getEnvironment().getProperty("ew.rabbitmq.enabled");
        return property != null && "TRUE".equals(property.toUpperCase());
    }
}
