package cn.cola.serviceclient.service;


import cn.cola.common.common.ErrorCode;
import cn.cola.common.common.exception.ThrowUtils;
import cn.cola.common.constant.CommonConstant;
import cn.cola.common.utils.SqlUtils;
import cn.cola.model.enums.QuestionBankScoringStrategyEnum;
import cn.cola.model.enums.QuestionBankTypeEnum;
import cn.cola.model.enums.ReviewStatusEnum;
import cn.cola.model.po.QuestionBank;
import cn.cola.model.questionbank.QuestionBankQueryRequest;
import cn.cola.model.vo.QuestionBankVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 题库服务
 *
 * @author ColaBlack
 */
@FeignClient(name = "teaai-question-bank", url = "/api/question/bank/")
public interface QuestionBankService {

    /**
     * 校验数据
     *
     * @param questionBank 待校验的数据
     * @param add          是否为创建的数据进行校验
     */
    default void validQuestionBank(QuestionBank questionBank, boolean add) {
        ThrowUtils.throwIf(questionBank == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String bankName = questionBank.getBankName();
        String bankDesc = questionBank.getBankDesc();
        Integer bankType = questionBank.getBankType();
        Integer scoringStrategy = questionBank.getScoringStrategy();
        Integer reviewStatus = questionBank.getReviewStatus();

        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(bankName), ErrorCode.PARAMS_ERROR, "题库名称不能为空");
            ThrowUtils.throwIf(StringUtils.isBlank(bankDesc), ErrorCode.PARAMS_ERROR, "题库描述不能为空");
            QuestionBankTypeEnum questionBankTypeEnum = QuestionBankTypeEnum.getEnumByValue(bankType);
            ThrowUtils.throwIf(questionBankTypeEnum == null, ErrorCode.PARAMS_ERROR, "题库类别非法");
            QuestionBankScoringStrategyEnum scoringStrategyEnum = QuestionBankScoringStrategyEnum.getEnumByValue(scoringStrategy);
            ThrowUtils.throwIf(scoringStrategyEnum == null, ErrorCode.PARAMS_ERROR, "题库评分策略非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(bankName)) {
            ThrowUtils.throwIf(bankName.length() > 80, ErrorCode.PARAMS_ERROR, "题库名称要小于 80");
        }
        if (reviewStatus != null) {
            ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
            ThrowUtils.throwIf(reviewStatusEnum == null, ErrorCode.PARAMS_ERROR, "审核状态非法");
        }
    }

    /**
     * 获取查询条件
     *
     * @param questionBankQueryRequest 查询条件
     * @return 查询条件封装
     */
    default QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest) {
        QueryWrapper<QuestionBank> queryWrapper = new QueryWrapper<>();
        if (questionBankQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionBankQueryRequest.getId();
        String bankName = questionBankQueryRequest.getBankName();
        String bankDesc = questionBankQueryRequest.getBankDesc();
        Integer bankType = questionBankQueryRequest.getBankType();
        Integer scoringStrategy = questionBankQueryRequest.getScoringStrategy();
        Integer reviewStatus = questionBankQueryRequest.getReviewStatus();
        String reviewMessage = questionBankQueryRequest.getReviewMessage();
        Long reviewerId = questionBankQueryRequest.getReviewerid();
        Long userId = questionBankQueryRequest.getUserId();
        String searchText = questionBankQueryRequest.getSearchText();
        String sortField = questionBankQueryRequest.getSortField();
        String sortOrder = questionBankQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("bank_name", searchText).or().like("bankDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(bankName), "bank_name", bankName);
        queryWrapper.like(StringUtils.isNotBlank(bankDesc), "bank_desc", bankDesc);
        queryWrapper.like(StringUtils.isNotBlank(reviewMessage), "review_message", reviewMessage);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(bankType), "bank_type", bankType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), "scoring_strategy", scoringStrategy);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewStatus), "review_status", reviewStatus);
        queryWrapper.eq(ObjectUtils.isNotEmpty(reviewerId), "reviewerId", reviewerId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_Id", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 获取题库封装
     *
     * @param questionBank 待封装的数据
     * @return 题库封装
     */
    @PostMapping("/get/VO")
    QuestionBankVO getQuestionBankVO(@RequestBody QuestionBank questionBank);

    /**
     * 分页获取题库封装
     *
     * @param questionBankPage 分页对象
     * @return 分页题库封装
     */
    @PostMapping("/get/VO/page")
    Page<QuestionBankVO> getQuestionBankVOPage(@RequestBody Page<QuestionBank> questionBankPage);

    /**
     * 审核题库
     *
     * @param reviewMessage 审核信息
     * @param reviewerId    审核人id
     * @param id            题库id
     * @param reviewStatus  审核状态
     */
    @GetMapping("/review")
    boolean questionBankReview(
            @RequestParam(value = "reviewMessage") String reviewMessage,
            @RequestParam(value = "reviewerId") Long reviewerId,
            @RequestParam(value = "id") Long id,
            @RequestParam(value = "reviewStatus") Integer reviewStatus
    );

    @GetMapping("/inner/get/id")
    QuestionBank getById(@RequestParam(value = "id") Long id);

    @PostMapping("/inner/save")
    boolean save(@RequestBody QuestionBank questionBank);

    @PostMapping("/inner/update")
    boolean updateById(@RequestBody QuestionBank questionBank);

    @DeleteMapping("/inner/delete/id")
    boolean removeById(@RequestParam(value = "id") Long id);

    @GetMapping("/inner/get/page")
    Page<QuestionBank> page(@RequestParam(value = "QuestionBankPage") Page<QuestionBank> questionBankPage,
                            @RequestParam(value = "queryWrapper") QueryWrapper<QuestionBank> queryWrapper);
}
