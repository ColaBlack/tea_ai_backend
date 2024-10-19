package service;


import model.dto.file.UploadFileRequest;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件服务接口
 *
 * @author ColaBlack
 */
public interface FileService {
    /**
     * 上传文件
     *
     * @param file              上传的文件
     * @param uploadFileRequest 上传文件请求
     * @param request           请求对象
     * @return 上传结果
     */
    String uploadImage(@RequestPart("file") MultipartFile file, UploadFileRequest uploadFileRequest, HttpServletRequest request);

    /**
     * 验证文件
     *
     * @param file 文件
     */
    void validFile(MultipartFile file);
}
