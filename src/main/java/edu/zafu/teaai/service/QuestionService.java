package edu.zafu.teaai.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.zafu.teaai.model.dto.question.QuestionQueryRequest;
import edu.zafu.teaai.model.po.Question;
import edu.zafu.teaai.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 题目服务
 *
 * @author ColaBlack
 */
public interface QuestionService extends IService<Question> {

    /**
     * 校验数据
     *
     * @param question 待校验的数据
     * @param add      是否为创建的数据进行校验
     */
    void validQuestion(Question question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest 查询条件
     * @return 查询条件封装
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question 题目实体
     * @param request 请求对象
     * @return 题目封装
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage 分页对象
     * @param request 请求对象
     * @return 分页题目封装
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
