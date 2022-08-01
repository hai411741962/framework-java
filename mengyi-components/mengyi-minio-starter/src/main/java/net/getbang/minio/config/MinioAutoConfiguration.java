package net.getbang.minio.config;


import lombok.AllArgsConstructor;
import net.getbang.minio.utils.MinioUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnWebApplication
@Import(value = {MinioConfig.class, MinioUtil.class})
public class MinioAutoConfiguration {
}
