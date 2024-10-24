package cn.cola.result.service;

import cn.cola.common.common.ErrorCode;
import cn.cola.common.common.exception.ThrowUtils;
import cn.cola.model.po.QuestionBank;
import cn.cola.model.po.ScoringResult;
import cn.cola.model.po.User;
import cn.cola.model.vo.ScoringResultVO;
import cn.cola.model.vo.UserVO;
import cn.cola.result.mapper.ScoringResultMapper;
import cn.cola.serviceclient.service.QuestionBankService;
import cn.cola.serviceclient.service.ScoringResultService;
import cn.cola.serviceclient.service.UserService;
import cn.hutool.core.collection.CollUtil;
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
 * 评分结果服务实现
 *
 * @author ColaBlack
 */
@Service
@Slf4j
public class ScoringResultServiceImpl extends ServiceImpl<ScoringResultMapper, ScoringResult> implements ScoringResultService {

    @Resource
    private UserService userService;

    @Resource
    private QuestionBankService questionBankService;

    @Override
    public List<ScoringResult> list(QueryWrapper queryWrapper) {
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ScoringResult> list(LambdaQueryWrapper lambdaQueryWrapper) {
        return this.baseMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public Page<ScoringResult> page(Page<ScoringResult> page, QueryWrapper queryWrapper) {
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean save(ScoringResult scoringResult, QueryWrapper<ScoringResult> queryWrapper) {
        return this.baseMapper.insert(scoringResult) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return this.baseMapper.deleteById(id) > 0;
    }

    @Override
    public ScoringResult getById(Long id) {
        return this.baseMapper.selectById(id);
    }

    /**
     * 校验数据
     *
     * @param scoringResult 待校验的数据
     * @param add           是否为创建的数据进行校验
     */
    @Override
    public void validScoringResult(ScoringResult scoringResult, boolean add) {
        ThrowUtils.throwIf(scoringResult == null, ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        String resultName = scoringResult.getResultName();
        Long bankid = scoringResult.getBankid();
        // 创建数据时，参数不能为空
        if (add) {
            // 补充校验规则
            ThrowUtils.throwIf(StringUtils.isBlank(resultName), ErrorCode.PARAMS_ERROR, "结果名称不能为空");
            ThrowUtils.throwIf(bankid == null || bankid <= 0, ErrorCode.PARAMS_ERROR, "bankid 非法");
        }
        // 修改数据时，有参数则校验
        // 补充校验规则
        if (StringUtils.isNotBlank(resultName)) {
            ThrowUtils.throwIf(resultName.length() > 128, ErrorCode.PARAMS_ERROR, "结果名称不能超过 128");
        }
        // 补充校验规则
        if (bankid != null) {
            QuestionBank questionBank = questionBankService.getById(bankid);
            ThrowUtils.throwIf(questionBank == null, ErrorCode.PARAMS_ERROR, "题库不存在");
        }
    }

    /**
     * 获取评分结果封装
     *
     * @param scoringResult 评分结果对象
     * @return 评分结果封装
     */
    @Override
    public ScoringResultVO getScoringResultVO(ScoringResult scoringResult) {
        // 对象转封装类
        ScoringResultVO scoringResultVO = ScoringResultVO.objToVo(scoringResult);

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Long userId = scoringResult.getUserid();
        User user = null;
        if (userId != null && userId > 0) {
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        scoringResultVO.setUser(userVO);
        // endregion

        return scoringResultVO;
    }

    /**
     * 分页获取评分结果封装
     *
     * @param scoringResultPage 分页对象
     * @return 分页评分结果封装
     */
    @Override
    public Page<ScoringResultVO> getScoringResultVOPage(Page<ScoringResult> scoringResultPage) {
        List<ScoringResult> scoringResultList = scoringResultPage.getRecords();
        Page<ScoringResultVO> scoringResultVOPage = new Page<>(scoringResultPage.getCurrent(), scoringResultPage.getSize(), scoringResultPage.getTotal());
        if (CollUtil.isEmpty(scoringResultList)) {
            return scoringResultVOPage;
        }
        // 对象列表 => 封装对象列表
        List<ScoringResultVO> scoringResultVOList = scoringResultList.stream().map(ScoringResultVO::objToVo).collect(Collectors.toList());

        // 可以根据需要为封装对象补充值，不需要的内容可以删除
        // region 可选
        // 1. 关联查询用户信息
        Set<Long> userIdSet = scoringResultList.stream().map(ScoringResult::getUserid).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));
        // 填充信息
        scoringResultVOList.forEach(scoringResultVO -> {
            Long userId = scoringResultVO.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            scoringResultVO.setUser(userService.getUserVO(user));
        });
        // endregion

        scoringResultVOPage.setRecords(scoringResultVOList);
        return scoringResultVOPage;
    }

    @Override
    public boolean save(ScoringResult entity) {
        return super.save(entity);
    }

    @Override
    public boolean updateById(ScoringResult entity) {
        return super.updateById(entity);
    }
}
