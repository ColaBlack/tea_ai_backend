package edu.zafu.teaai.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 获取minio的配置信息
 *
 * @author ColaBlack
 */
@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProps {

    /**
     * minio的url
     */
    private String endpoint;

    /**
     * minio的ak
     */
    private String accesskey;

    /**
     * minio的sk
     */
    private String secretkwy;
}

