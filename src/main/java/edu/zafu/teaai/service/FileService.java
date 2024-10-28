package edu.zafu.teaai.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * minio文件服务接口
 *
 * @author ColaBlack
 */
public interface FileService {

    /**
     * 上传文件
     *
     * @param file    上传的文件
     * @param biz     业务类型
     * @param request 请求对象
     * @return 上传结果
     */
    String uploadImage(MultipartFile file, String biz, HttpServletRequest request);

    /**
     * 验证文件
     *
     * @param file 文件
     */
    void validFile(MultipartFile file);
}
