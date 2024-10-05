package edu.zafu.teaai.model.dto.useranswer;

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

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 题库 id
     */
    private Long bankid;
    /**
     * 用户答案（JSON 数组）
     */
    private List<String> choices;
}