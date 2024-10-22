package cn.cola.model.file;

import lombok.Data;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 * @author ColaBlack
 */
@Data
public class UploadFileRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务
     */
    private String biz;
}