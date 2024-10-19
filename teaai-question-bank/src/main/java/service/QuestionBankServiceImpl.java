package service;


import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import common.ErrorCode;
import common.exception.BusinessException;
import common.exception.ThrowUtils;
import constant.CommonConstant;
import lombok.extern.slf4j.Slf4j;
import mapper.QuestionBankMapper;
import model.dto.questionbank.QuestionBankQueryRequest;
import model.enums.QuestionBankScoringStrategyEnum;
import model.enums.QuestionBankTypeEnum;
import model.enums.ReviewStatusEnum;
import model.po.QuestionBank;
import model.po.User;
import model.vo.QuestionBankVO;
import model.vo.UserVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import utils.SqlUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题库服务实现
 *
 * @author ColaBlack
 */
@Service
@Slf4j
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank> implements QuestionBankService {

    @Resource
    private UserService userService;

    /**
     * 校验数据
     *
     * @param questionBank 题库对象
     * @param add          是否为创建的数据进行校验
     */
    @Override
    public void validQuestionBank(QuestionBank questionBank, boolean add) {
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
     * @param questionBankQueryRequest 查询条件对象
     * @return 查询条件
     */
    @Override
    public QueryWrapper<QuestionBank> getQueryWrapper(QuestionBankQueryRequest questionBankQueryRequest) {
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
     * @param questionBank 题库对象
     * @param request      请求对象
     * @return 题库封装
     */
    @Override
    public QuestionBankVO getQuestionBankVO(QuestionBank questionBank, HttpServletRequest request) {
        // 对象转封装类
        QuestionBankVO questionBankVO = QuestionBankVO.poToVo(questionBank);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = questionBank.getUserId();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionBankVO.setUser(userVO);
        // endregion
        return questionBankVO;
    }

    /**
     * 分页获取题库封装
     *
     * @param questionBankPage 分页对象
     * @param request          请求对象
     * @return 分页题库封装
     */
    @Override
    public Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage, HttpServletRequest request) {
        List<QuestionBank> questionBankList = questionBankPage.getRecords();
        Page<QuestionBankVO> questionBankVOPage = new Page<>(questionBankPage.getCurrent(), questionBankPage.getSize(), questionBankPage.getTotal());
        if (CollUtil.isEmpty(questionBankList)) {
            return questionBankVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionBankVO> questionBankVOList = questionBankList.stream().map(QuestionBankVO::poToVo).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionBankList.stream().map(QuestionBank::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionBankVOList.forEach(questionBankVO -> {
            Long userId = questionBankVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionBankVO.setUser(userService.getUserVO(user));
        });
        // endregion

        questionBankVOPage.setRecords(questionBankVOList);
        return questionBankVOPage;
    }

    /**
     * 审核题库
     *
     * @param reviewMessage 审核请求
     * @param request       请求对象
     * @param id            题库id
     * @param reviewStatus  审核状态
     */
    @Override
    public boolean questionBankReview(String reviewMessage, HttpServletRequest request, Long id, Integer reviewStatus) {
        // 参数校验
        ReviewStatusEnum reviewStatusEnum = ReviewStatusEnum.getEnumByValue(reviewStatus);
        if (id == null || reviewStatusEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 判断是否存在
        QuestionBank oldQuestionBank = this.getById(id);
        ThrowUtils.throwIf(oldQuestionBank == null, ErrorCode.NOT_FOUND_ERROR);
        // 已是该状态
        if (oldQuestionBank.getReviewStatus().equals(reviewStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请勿重复审核");
        }
        // 更新审核状态
        User loginUser = userService.getLoginUser(request);
        QuestionBank questionBank = new QuestionBank();
        questionBank.setId(id);
        questionBank.setReviewStatus(reviewStatus);
        questionBank.setReviewMessage(reviewMessage);
        questionBank.setReviewerid(loginUser.getId());
        questionBank.setReviewTime(new Date());
        int result = this.baseMapper.updateById(questionBank);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR);
        return true;
    }
}
