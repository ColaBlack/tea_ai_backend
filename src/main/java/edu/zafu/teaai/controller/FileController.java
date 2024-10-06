package edu.zafu.teaai.controller;

import edu.zafu.teaai.common.BaseResponse;
import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.ResultUtils;
import edu.zafu.teaai.common.exception.ThrowUtils;
import edu.zafu.teaai.model.dto.file.UploadFileRequest;
import edu.zafu.teaai.model.enums.FileUploadBizEnum;
import edu.zafu.teaai.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 文件上传接口
 *
 * @author ColaBlack
 */
@RequestMapping("/file")
@RestController
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload/avatar")
    public BaseResponse<String> uploadAvatar(@RequestPart("file") MultipartFile file,
                                             UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        // 校验业务类型
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ThrowUtils.throwIf(!Objects.equals(fileUploadBizEnum, FileUploadBizEnum.USER_AVATAR), ErrorCode.PARAMS_ERROR, "上传业务类型不正确");

        // 上传文件
        String res = fileService.uploadImage(file, uploadFileRequest, request);
        return ResultUtils.success(res);
    }

    @PostMapping("/upload/bank")
    public BaseResponse<String> uploadBankImage(@RequestPart("file") MultipartFile file,
                                                UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        // 校验业务类型
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ThrowUtils.throwIf(!Objects.equals(fileUploadBizEnum, FileUploadBizEnum.BANK_IMAGE), ErrorCode.PARAMS_ERROR, "上传业务类型不正确");

        // 上传文件
        String res = fileService.uploadImage(file, uploadFileRequest, request);
        return ResultUtils.success(res);
    }

    @PostMapping("/upload/result")
    public BaseResponse<String> uploadResultImage(@RequestPart("file") MultipartFile file,
                                                  UploadFileRequest uploadFileRequest, HttpServletRequest request) {
        // 校验业务类型
        String biz = uploadFileRequest.getBiz();
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        ThrowUtils.throwIf(!Objects.equals(fileUploadBizEnum, FileUploadBizEnum.RESULT_IMAGE), ErrorCode.PARAMS_ERROR, "上传业务类型不正确");

        // 上传文件
        String res = fileService.uploadImage(file, uploadFileRequest, request);
        return ResultUtils.success(res);
    }
}

