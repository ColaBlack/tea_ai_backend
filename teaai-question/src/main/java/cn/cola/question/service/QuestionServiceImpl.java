package cn.cola.question.service;

import cn.cola.common.common.ErrorCode;
import cn.cola.common.common.exception.ThrowUtils;
import cn.cola.model.po.Question;
import cn.cola.model.po.QuestionBank;
import cn.cola.model.po.User;
import cn.cola.model.question.QuestionQueryRequest;
import cn.cola.model.vo.QuestionVO;
import cn.cola.model.vo.UserVO;
import cn.cola.question.mapper.QuestionMapper;
import cn.cola.serviceclient.service.QuestionBankService;
import cn.cola.serviceclient.service.QuestionService;
import cn.cola.serviceclient.service.UserService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 题目服务实现
 *
 * @author ColaBlack
 */
@Service
@Slf4j
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {
    @Override
    public Question getById(Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public boolean removeById(Long id) {
        return this.baseMapper.deleteById(id) > 0;
    }

    @Override
    public Page<Question> page(Page<Question> questionPage, QueryWrapper<Question> queryWrapper) {
        return this.baseMapper.selectPage(questionPage, queryWrapper);
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        return QuestionService.super.getQueryWrapper(questionQueryRequest);
    }

    @Resource
    private UserService userService;

    @Resource
    private QuestionBankService questionBankService;

    /**
     * 校验数据
     *
     * @param question 要校验的数据
     * @param add      是否为创建的数据进行校验
     */
    @Override
    public void validQuestion(Question question, boolean add) {
        ThrowUtils.throwIf(question == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String questionContent = question.getQuestionContent();
        Long bankid = question.getBankid();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(questionContent), ErrorCode.PARAMS_ERROR, "题目内容不能为空");
            ThrowUtils.throwIf(bankid == null || bankid <= 0, ErrorCode.PARAMS_ERROR, "bankid 非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (bankid != null) {
            QuestionBank questionBank = questionBankService.getById(bankid);
            ThrowUtils.throwIf(questionBank == null, ErrorCode.PARAMS_ERROR, "题库不存在");
        }
    }

    @Override
    public Question getOne(LambdaQueryWrapper queryWrapper) {
        return this.getOne((Wrapper<Question>) queryWrapper);
    }

    /**
     * 获取题目封装
     *
     * @param question 题目对象
     * @return 题目封装
     */
    @Override
    public QuestionVO getQuestionVO(Question question) {
        // 对象转封装类
        QuestionVO questionVO = QuestionVO.poToVo(question);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = question.getUserid();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUser(userVO);
        // endregion

        return questionVO;
    }

    /**
     * 分页获取题目封装
     *
     * @param questionPage 分页对象
     * @return 分页题目封装
     */
    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage) {
        List<Question> questionList = questionPage.getRecords();
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        if (CollUtil.isEmpty(questionList)) {
            return questionVOPage;
        }
        // 对象列表 => 封装对象列表
        List<QuestionVO> questionVOList = questionList.stream().map(QuestionVO::poToVo).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionList.stream().map(Question::getUserid).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        questionVOList.forEach(questionVO -> {
            Long userId = questionVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            questionVO.setUser(userService.getUserVO(user));
        });
        // endregion

        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }

    @Override
    public boolean save(Question entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(Question entity) {
        return super.updateById(entity);
    }
}
