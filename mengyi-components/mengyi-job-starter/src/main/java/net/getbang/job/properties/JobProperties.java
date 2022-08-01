package net.getbang.job.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: CJ
 * @date: 2022/6/16
 */
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
@Data
public class JobProperties {

    private String accessToken;
    private Admin admin;
    private Executor executor;

    @Data
    public static class Admin{
        private String addresses;
        private String userName;
        private String password;
    }

    @Data
    public static class Executor{
        private String appName;
        private String ip;
        private int port;
        private String logPath;
        private int logRetentionDays ;
    }

}
