package edu.zafu.teaai.model.dto.userAnswer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
* 更新用户答案请求
*
* @author ColaBlack
*/
@Data
public class UserAnswerUpdateRequest implements Serializable {

/**
* id
*/
private Long id;

/**
* 标题
*/
private String title;

/**
* 内容
*/
private String content;

/**
* 标签列表
*/
private List
<String> tags;

    private static final long serialVersionUID = 1L;
    }