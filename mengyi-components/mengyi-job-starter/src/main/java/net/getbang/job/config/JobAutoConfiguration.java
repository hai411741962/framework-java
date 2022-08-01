package net.getbang.job.config;

import net.getbang.job.properties.JobProperties;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.List;

/**
 * job工具自动配置
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({JobProperties.class})
@ConditionalOnWebApplication
@Import(DynamicXxlJobService.class)
@Slf4j
public class JobAutoConfiguration {


    @Autowired
    private InetUtils inetUtils;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(JobProperties jobProperties) {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
      //  xxlJobSpringExecutor.setAdminAddresses(jobProperties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAdminAddresses(packAdminAddresses(jobProperties.getAdmin().getAddresses()));

        xxlJobSpringExecutor.setAppname(jobProperties.getExecutor().getAppName());
       // String ipAddress = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
       // xxlJobSpringExecutor.setAddress(ipAddress);
        xxlJobSpringExecutor.setIp(jobProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(jobProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setAccessToken(jobProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(jobProperties.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(jobProperties.getExecutor().getLogRetentionDays());
        return xxlJobSpringExecutor;
    }

    /**
     * 针对xxl-job根据adminAddresses配置进行动态服务发现
     * @return
     */
    private final String packAdminAddresses(String adminAddresses){
        String replace = adminAddresses.replace("http://", "");
        //获取appName,有可能是ip:port
        String serviceId = replace.substring(0, replace.indexOf("/"));
        //获取服务列表
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        //如果服务列表有值，则组装url，否则直接返回
        if(null != instances && !instances.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(ServiceInstance ins: instances){

                sb.append(adminAddresses.replace(serviceId, ins.getHost() + ":" + ins.getPort()))
                        .append(',');
            }
            return sb.substring(0, sb.lastIndexOf(","));
        }
        return adminAddresses;
    }

}




