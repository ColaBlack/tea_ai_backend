package edu.zafu.teaai.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.exception.BusinessException;
import edu.zafu.teaai.common.exception.ThrowUtils;
import edu.zafu.teaai.constant.FileConstant;
import edu.zafu.teaai.constant.ImageBedConstant;
import edu.zafu.teaai.constant.UserConstant;
import edu.zafu.teaai.model.dto.file.UploadFileRequest;
import edu.zafu.teaai.model.enums.FileUploadBizEnum;
import edu.zafu.teaai.service.FileService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Objects;
import java.util.UUID;

/**
 * 文件上传服务实现类
 *
 * @author ColaBlack
 */
@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(MultipartFile file, UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        // 校验文件
        validFile(file);

        //登录用户才能上传文件
        Object attribute = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        ThrowUtils.throwIf(ObjectUtils.isEmpty(attribute), ErrorCode.NOT_LOGIN_ERROR, "用户未登录");

        //重命名文件
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        String fileName = Objects.requireNonNull(fileUploadBizEnum).getValue() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        //获取文件保持路径
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String filePath = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() + FileConstant.IMAGE_UPLOAD_PATH + fileName;

        //本人使用图床上传图片，需要先将图片临时上传到本地服务器，然后再上传到图床
        try {
            file.transferTo(new File(filePath));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }

        //上传到图床
        String res = HttpRequest.post(ImageBedConstant.IMAGE_BED_UPLOAD_URL + "?authCode=" + ImageBedConstant.AUTH_CODE).form("file", filePath).timeout(60000).execute().body();

        //删除本地文件
        FileUtil.del(filePath);
        return res;
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
