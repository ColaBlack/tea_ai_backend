package edu.zafu.teaai.config;


import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * minio配置类
 *
 * @author ColaBlack
 */
@Configuration
public class MinioConfig {

    @Resource
    private MinioProps props;

    /**
     * 获取 Minio客户端对象
     *
     * @return MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(props.getEndpoint())
                .credentials(props.getAccesskey(), props.getSecretkwy())
                .build();
    }
}

