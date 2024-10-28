package edu.zafu.teaai.utils;


import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * minio操作类
 *
 * @author ColaBlack
 */
@Component
public class MinioUtils {

    @Resource
    private MinioClient client;

    /**
     * 上传文件
     *
     * @param bucketName 桶名称
     * @param objectName 文件名
     * @param stream     输入流
     * @param fileSize   文件大小
     */
    @SneakyThrows
    public void putObject(String bucketName, String objectName, InputStream stream, Long fileSize) {
        client.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(stream, fileSize, -1)
                        .build());
    }


    /**
     * 获取文件在minio在服务器上的外链
     *
     * @param bucketName 桶名称
     * @param objectName 文件名
     * @return 外链
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName) {
        return client.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

}
