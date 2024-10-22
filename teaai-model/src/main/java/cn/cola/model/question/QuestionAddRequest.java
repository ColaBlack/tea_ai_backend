package cn.cola.model.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 创建题目请求
 *
 * @author ColaBlack
 */
@Data
public class QuestionAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 题目内容（json格式）
     */
    private List<QuestionContentDTO> questionContent;
    /**
     * 题库 id
     */
    private Long questionBankId;
}