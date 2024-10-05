package edu.zafu.teaai.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.zafu.teaai.annotation.AuthCheck;
import edu.zafu.teaai.common.BaseResponse;
import edu.zafu.teaai.common.DeleteRequest;
import edu.zafu.teaai.common.ErrorCode;
import edu.zafu.teaai.common.ResultUtils;
import edu.zafu.teaai.common.exception.BusinessException;
import edu.zafu.teaai.common.exception.ThrowUtils;
import edu.zafu.teaai.constant.UserConstant;
import edu.zafu.teaai.model.dto.scoringresult.ScoringResultAddRequest;
import edu.zafu.teaai.model.dto.scoringresult.ScoringResultEditRequest;
import edu.zafu.teaai.model.dto.scoringresult.ScoringResultQueryRequest;
import edu.zafu.teaai.model.dto.scoringresult.ScoringResultUpdateRequest;
import edu.zafu.teaai.model.po.ScoringResult;
import edu.zafu.teaai.model.po.User;
import edu.zafu.teaai.model.vo.ScoringResultVO;
import edu.zafu.teaai.service.ScoringResultService;
import edu.zafu.teaai.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 评分结果接口
 *
 * @author ColaBlack
 */
@RestController
@RequestMapping("/scoringResult")
@Slf4j
public class ScoringResultController {

    @Resource
    private ScoringResultService scoringResultService;

    @Resource
    private UserService userService;

    // region 增删改查

    /**
     * 创建评分结果
     *
     * @param scoringResultAddRequest 评分结果创建请求
     * @param request                 请求对象
     * @return 新写入数据的 id
     */
    @PostMapping("/add")
    public BaseResponse<Long> addScoringResult(@RequestBody ScoringResultAddRequest scoringResultAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(scoringResultAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 在此处将实体类和 DTO 进行转换
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(scoringResultAddRequest, scoringResult);
        scoringResult.setResultProp(JSONUtil.toJsonStr(scoringResultAddRequest.getResultProp()));
        // 数据校验
        scoringResultService.validScoringResult(scoringResult, true);
        // 填充默认值
        User loginUser = userService.getLoginUser(request);
        scoringResult.setUserid(loginUser.getId());
        // 写入数据库
        boolean result = scoringResultService.save(scoringResult);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        // 返回新写入的数据 id
        long newScoringResultId = scoringResult.getId();
        return ResultUtils.success(newScoringResultId);
    }

    /**
     * 删除评分结果
     *
     * @param deleteRequest 删除请求
     * @param request       请求对象
     * @return 是否删除成功
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteScoringResult(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        ScoringResult oldScoringResult = scoringResultService.getById(id);
        ThrowUtils.throwIf(oldScoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldScoringResult.getUserid().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = scoringResultService.removeById(id);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 更新评分结果（仅管理员可用）
     *
     * @param scoringResultUpdateRequest 评分结果更新请求
     * @return 是否更新成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateScoringResult(@RequestBody ScoringResultUpdateRequest scoringResultUpdateRequest) {
        if (scoringResultUpdateRequest == null || scoringResultUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(scoringResultUpdateRequest, scoringResult);
        scoringResult.setResultProp(JSONUtil.toJsonStr(scoringResultUpdateRequest.getResultProp()));
        // 数据校验
        scoringResultService.validScoringResult(scoringResult, false);
        // 判断是否存在
        long id = scoringResultUpdateRequest.getId();
        ScoringResult oldScoringResult = scoringResultService.getById(id);
        ThrowUtils.throwIf(oldScoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean result = scoringResultService.updateById(scoringResult);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

//    /**
//     * 根据 id 获取评分结果（封装类）
//     *
//     * @param id 评分结果 id
//     * @return 评分结果封装类
//     */
//    @GetMapping("/get/vo")
//    public BaseResponse<ScoringResultVO> getScoringResultVOById(long id, HttpServletRequest request) {
//        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
//        // 查询数据库
//        ScoringResult scoringResult = scoringResultService.getById(id);
//        ThrowUtils.throwIf(scoringResult == null, ErrorCode.NOT_FOUND_ERROR);
//        // 获取封装类
//        return ResultUtils.success(scoringResultService.getScoringResultVO(scoringResult, request));
//    }

    /**
     * 分页获取评分结果列表（仅管理员可用）
     *
     * @param scoringResultQueryRequest 查询请求
     * @return 分页结果
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<ScoringResult>> listScoringResultByPage(@RequestBody ScoringResultQueryRequest scoringResultQueryRequest) {
        long current = scoringResultQueryRequest.getCurrent();
        long size = scoringResultQueryRequest.getPageSize();
        // 查询数据库
        Page<ScoringResult> scoringResultPage = scoringResultService.page(new Page<>(current, size), scoringResultService.getQueryWrapper(scoringResultQueryRequest));
        return ResultUtils.success(scoringResultPage);
    }

    /**
     * 分页获取评分结果列表（封装类）
     *
     * @param scoringResultQueryRequest 查询请求
     * @param request                   请求对象
     * @return 分页结果
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<ScoringResultVO>> listScoringResultVOByPage(@RequestBody ScoringResultQueryRequest scoringResultQueryRequest, HttpServletRequest request) {
        long current = scoringResultQueryRequest.getCurrent();
        long size = scoringResultQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Page<ScoringResult> scoringResultPage = scoringResultService.page(new Page<>(current, size), scoringResultService.getQueryWrapper(scoringResultQueryRequest));
        // 获取封装类
        return ResultUtils.success(scoringResultService.getScoringResultVOPage(scoringResultPage, request));
    }

//    /**
//     * 分页获取当前登录用户创建的评分结果列表
//     *
//     * @param scoringResultQueryRequest 查询请求
//     * @param request                   请求对象
//     * @return 分页结果
//     */
//    @PostMapping("/my/list/page/vo")
//    public BaseResponse<Page<ScoringResultVO>> listMyScoringResultVOByPage(@RequestBody ScoringResultQueryRequest scoringResultQueryRequest, HttpServletRequest request) {
//        ThrowUtils.throwIf(scoringResultQueryRequest == null, ErrorCode.PARAMS_ERROR);
//        // 补充查询条件，只查询当前登录用户的数据
//        User loginUser = userService.getLoginUser(request);
//        scoringResultQueryRequest.setUserid(loginUser.getId());
//        long current = scoringResultQueryRequest.getCurrent();
//        long size = scoringResultQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        // 查询数据库
//        Page<ScoringResult> scoringResultPage = scoringResultService.page(new Page<>(current, size), scoringResultService.getQueryWrapper(scoringResultQueryRequest));
//        // 获取封装类
//        return ResultUtils.success(scoringResultService.getScoringResultVOPage(scoringResultPage, request));
//    }

    /**
     * 编辑评分结果（给用户使用）
     *
     * @param scoringResultEditRequest 评分结果编辑请求
     * @param request                  请求对象
     * @return 是否编辑成功
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editScoringResult(@RequestBody ScoringResultEditRequest scoringResultEditRequest, HttpServletRequest request) {
        if (scoringResultEditRequest == null || scoringResultEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 在此处将实体类和 DTO 进行转换
        ScoringResult scoringResult = new ScoringResult();
        BeanUtils.copyProperties(scoringResultEditRequest, scoringResult);
        scoringResult.setResultProp(JSONUtil.toJsonStr(scoringResultEditRequest.getResultProp()));
        // 数据校验
        scoringResultService.validScoringResult(scoringResult, false);
        User loginUser = userService.getLoginUser(request);
        // 判断是否存在
        long id = scoringResultEditRequest.getId();
        ScoringResult oldScoringResult = scoringResultService.getById(id);
        ThrowUtils.throwIf(oldScoringResult == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldScoringResult.getUserid().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 操作数据库
        boolean result = scoringResultService.updateById(scoringResult);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    // endregion
}
