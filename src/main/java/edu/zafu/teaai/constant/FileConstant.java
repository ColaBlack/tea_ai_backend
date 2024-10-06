package edu.zafu.teaai.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 文件上传相关常量
 *
 * @author ColaBlack
 */
public interface FileConstant {
    /**
     * 上传文件最大大小1MB
     */
    int MAX_FILE_SIZE = 1048576;

    /**
     * 允许上传的图片类型
     */
    List<String> ALLOW_IMAGE_TYPES = Arrays.asList("jpg", "jpeg", "png", "svg", "webp", "bmp");

    /**
     * 上传的文件路径
     */
    String IMAGE_UPLOAD_PATH = "\\src\\main\\resources\\images\\";
}
