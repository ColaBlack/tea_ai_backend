package cn.cola.questionbank.service;


import cn.cola.common.common.ErrorCode;
import cn.cola.common.common.exception.BusinessException;
import cn.cola.common.common.exception.ThrowUtils;
import cn.cola.model.enums.ReviewStatusEnum;
import cn.cola.model.po.QuestionBank;
import cn.cola.model.po.User;
import cn.cola.model.vo.QuestionBankVO;
import cn.cola.model.vo.UserVO;
import cn.cola.questionbank.mapper.QuestionBankMapper;
import cn.cola.serviceclient.service.QuestionBankService;
import cn.cola.serviceclient.service.UserService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
     * 获取题库封装
     *
     * @param questionBank 题库对象
     * @return 题库封装
     */
    @Override
    public QuestionBankVO getQuestionBankVO(QuestionBank questionBank) {
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
     * @return 分页题库封装
     */
    @Override
    public Page<QuestionBankVO> getQuestionBankVOPage(Page<QuestionBank> questionBankPage) {
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
     * @param reviewerId    审核人id
     * @param id            题库id
     * @param reviewStatus  审核状态
     */
    @Override
    public boolean questionBankReview(String reviewMessage, Long reviewerId, Long id, Integer reviewStatus) {
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
        QuestionBank questionBank = new QuestionBank();
        questionBank.setId(id);
        questionBank.setReviewStatus(reviewStatus);
        questionBank.setReviewMessage(reviewMessage);
        questionBank.setReviewerid(reviewerId);
        questionBank.setReviewTime(new Date());
        int result = this.baseMapper.updateById(questionBank);
        ThrowUtils.throwIf(result <= 0, ErrorCode.OPERATION_ERROR);
        return true;
    }

    @Override
    public QuestionBank getById(Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public boolean removeById(Long id) {
        return this.baseMapper.deleteById(id) > 0;
    }

    @Override
    public Page<QuestionBank> page(Page<QuestionBank> questionBankPage, QueryWrapper<QuestionBank> queryWrapper) {
        return this.baseMapper.selectPage(questionBankPage, queryWrapper);
    }

    @Override
    public boolean save(QuestionBank entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(QuestionBank entity) {
        return super.updateById(entity);
    }
}
