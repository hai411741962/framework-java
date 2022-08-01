package net.getbang.log.config;

import net.getbang.log.listener.ErrorLogListener;
import net.getbang.log.model.ServerInfo;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 日志工具自动配置
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@ConditionalOnWebApplication
@Import(value = {ServerInfo.class,ErrorLogListener.class})
public class LogAutoConfiguration {

//    @Autowired
//    private ServerInfo serverInfo;



//    @Bean
//    public ErrorLogListener errorEventListener() {
//
//        System.out.println("====注入ErrorLogListener========================");
//
//        return new ErrorLogListener(logClientFallback, serverInfo);
//    }

}




