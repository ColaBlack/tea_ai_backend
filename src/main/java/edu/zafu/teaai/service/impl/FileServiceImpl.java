package edu.zafu.teaai.service.impl;

import cn.hutool.core.io.FileUtil;
import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.exception.BusinessException;
import edu.zafu.teaai.common.exception.ThrowUtils;
import edu.zafu.teaai.constant.FileConstant;
import edu.zafu.teaai.service.FileService;
import edu.zafu.teaai.utils.MinioUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Minio文件服务实现类
 *
 * @author ColaBlack
 */
@Service
public class FileServiceImpl implements FileService {

    @Resource
    private MinioUtils minioUtils;

    /**
     * 上传文件
     *
     * @param file    上传的文件
     * @param biz     业务类型
     * @param request 请求对象
     * @return 上传结果
     */
    @SneakyThrows
    @Override
    public String uploadImage(MultipartFile file, String biz, HttpServletRequest request) {
        // 校验文件
        validFile(file);

        //重命名文件
        String fileName = biz + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        //使用minio上传文件
        minioUtils.putObject(FileConstant.BUCKET_NAME, fileName, file.getInputStream(), file.getSize());
        //获取外链
        return minioUtils.getObjectUrl(FileConstant.BUCKET_NAME, fileName);
    }

    /**
     * 校验文件
     *
     * @param file 文件
     */
    @Override
    public void validFile(MultipartFile file) {
        ThrowUtils.throwIf(ObjectUtils.isEmpty(file), ErrorCode.PARAMS_ERROR, "文件不能为空");
        // 文件大小
        long fileSize = file.getSize();
        if (fileSize > FileConstant.MAX_FILE_SIZE) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
        }
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(file.getOriginalFilename());
        if (!FileConstant.ALLOW_IMAGE_TYPES.contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
        }
    }
}
