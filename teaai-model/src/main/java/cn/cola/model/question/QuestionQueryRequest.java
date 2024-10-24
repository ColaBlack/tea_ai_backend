package cn.cola.model.question;



import cn.cola.model.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询题目请求
 *
 * @author ColaBlack
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuestionQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    private Long id;
    /**
     * 题目内容（json格式）
     */
    private String questionContent;
    /**
     * 题库 id
     */
    private Long questionBankId;
    /**
     * 创建用户 id
     */
    private Long userId;
}