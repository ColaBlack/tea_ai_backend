package edu.zafu.teaai.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.zafu.teaai.model.dto.questionbank.QuestionBankQueryRequest;
import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.model.vo.QuestionBankVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 题库服务
 *
 * @author ColaBlack
 */
public interface QuestionBankService extends IService<QuestionBank> {

    /**
     * 校验数据
     *
     * @param questionBank 待校验的数据
     * @param add          是否为创建的数据进行校验
     */
    void validQuestionBank(QuestionBank questionBank, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionBankQueryRequest 查询条件
     * @return 查询条件封装
     */
    QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest);

    /**
     * 获取题库封装
     *
     * @param questionBank 待封装的数据
     * @param request      请求对象
     * @return 题库封装
     */
    QuestionBankVO getQuestionBankVO(QuestionBank questionBank, HttpServletRequest request);

    /**
     * 分页获取题库封装
     *
     * @param questionBankPage 分页对象
     * @param request          请求对象
     * @return 分页题库封装
     */
    Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage, HttpServletRequest request);

    /**
     * 审核题库
     *
     * @param reviewMessage 审核信息
     * @param request       请求对象
     * @param id            题库id
     * @param reviewStatus  审核状态
     */
    boolean questionBankReview(String reviewMessage, HttpServletRequest request, Long id, Integer reviewStatus);
}
