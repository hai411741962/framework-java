package net.getbang.minio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.connectTimeout}")
    private long connectTimeout;

    @Value("${minio.readTimeout}")
    private long readTimeout;

    @Value("${minio.writeTimeout}")
    private long writeTimeout;

    @Bean
    public MinioClient getMinioClient() {
        MinioClient client = null;
        try {
            client = MinioClient.builder()
                    .endpoint(url)
                    .credentials(accessKey, secretKey)
                    .build();
            client.setTimeout(connectTimeout, writeTimeout, readTimeout);
        } catch (Exception ex) {
            ex.getMessage();
        }
        return client;
    }

}
