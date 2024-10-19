package model.dto.useranswer;

import lombok.Data;

import java.io.Serializable;

/**
 * 编辑用户答案请求
 *
 * @author ColaBlack
 */
@Data
public class UserAnswerEditRequest implements Serializable {

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
    private String choices;
}