package cn.cola.answer.service;

import cn.cola.answer.mapper.UserAnswerMapper;
import cn.cola.model.po.QuestionBank;
import cn.cola.model.po.User;
import cn.cola.model.po.UserAnswer;
import cn.cola.model.vo.UserAnswerVO;
import cn.cola.model.vo.UserVO;
import cn.cola.serviceclient.service.QuestionBankService;
import cn.cola.serviceclient.service.UserAnswerService;
import cn.cola.serviceclient.service.UserService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import common.ErrorCode;
import common.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户答案服务实现
 *
 * @author ColaBlack
 */
@Service
@Slf4j
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer> implements UserAnswerService {

    @Resource
    private UserService userService;

    @Resource
    private QuestionBankService questionBankService;

    /**
     * 校验数据
     *
     * @param userAnswer 用户答案对象
     * @param add        是否为创建的数据进行校验
     */
    @Override
    public void validUserAnswer(UserAnswer userAnswer, boolean add) {
        ThrowUtils.throwIf(userAnswer == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long bankid = userAnswer.getBankid();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(bankid == null || bankid <= 0, ErrorCode.PARAMS_ERROR, "bankid 非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (bankid != null) {
            QuestionBank questionBank = questionBankService.getById(bankid);
            ThrowUtils.throwIf(questionBank == null, ErrorCode.PARAMS_ERROR, "题库不存在");
        }
    }

    /**
     * 获取用户答案封装
     *
     * @param userAnswer 用户答案对象
     * @return 用户答案封装
     */
    @Override
    public UserAnswerVO getUserAnswerVO(UserAnswer userAnswer) {
        // 对象转封装类
        UserAnswerVO userAnswerVO = UserAnswerVO.poToVo(userAnswer);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = userAnswer.getUserid();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        userAnswerVO.setUser(userVO);
        // endregion

        return userAnswerVO;
    }

    /**
     * 分页获取用户答案封装
     *
     * @param userAnswerPage 用户答案分页对象
     * @return 用户答案分页封装
     */
    @Override
    public Page<UserAnswerVO> getUserAnswerVOPage(Page<UserAnswer> userAnswerPage) {
        List<UserAnswer> userAnswerList = userAnswerPage.getRecords();
        Page<UserAnswerVO> userAnswerVOPage = new Page<>(userAnswerPage.getCurrent(), userAnswerPage.getSize(), userAnswerPage.getTotal());
        if (CollUtil.isEmpty(userAnswerList)) {
            return userAnswerVOPage;
        }
        // 对象列表 => 封装对象列表
        List<UserAnswerVO> userAnswerVOList = userAnswerList.stream().map(UserAnswerVO::poToVo).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = userAnswerList.stream().map(UserAnswer::getUserid).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        userAnswerVOList.forEach(userAnswerVO -> {
            Long userId = userAnswerVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            userAnswerVO.setUser(userService.getUserVO(user));
        });
        // endregion

        userAnswerVOPage.setRecords(userAnswerVOList);
        return userAnswerVOPage;
    }

}
