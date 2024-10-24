package cn.cola.serviceclient.service;


import cn.cola.common.constant.CommonConstant;
import cn.cola.common.utils.SqlUtils;
import cn.cola.model.po.ScoringResult;
import cn.cola.model.scoringresult.ScoringResultQueryRequest;
import cn.cola.model.vo.ScoringResultVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评分结果服务
 *
 * @author ColaBlack
 */
@FeignClient(name = "teaai-scoring-result", url = "/api/scoring/result")
public interface ScoringResultService {

    @PostMapping("/inner/list/queryWrapper")
    List<ScoringResult> list(@RequestBody QueryWrapper queryWrapper);

    @PostMapping("/inner/list/lambdaQueryWrapper")
    List<ScoringResult> list(@RequestBody LambdaQueryWrapper lambdaQueryWrapper);

    @GetMapping("/inner/page")
    Page<ScoringResult> page(@RequestParam(value = "page") Page<ScoringResult> page,
                             @RequestParam(value = "queryWrapper") QueryWrapper queryWrapper);

    @GetMapping("/inner/save/queryWrapper")
    boolean save(@RequestParam(value = "scoringResult") ScoringResult scoringResult,
                 @RequestParam(value = "QueryWrapper") QueryWrapper<ScoringResult> queryWrapper);

    @PostMapping("/inner/save")
    boolean save(@RequestBody ScoringResult scoringResult);

    @DeleteMapping("/inner/delete")
    boolean removeById(@RequestParam(value = "id") Long id);

    @GetMapping("/inner/get/id")
    ScoringResult getById(@RequestParam(value = "id") Long id);

    @PostMapping("/inner/update/id")
    boolean updateById(@RequestBody ScoringResult scoringResult);

    /**
     * 校验数据
     *
     * @param scoringResult 待校验的数据
     * @param add           是否为创建的数据进行校验
     */
    @GetMapping("/valid")
    void validScoringResult(@RequestParam(value = "scoringResult") ScoringResult scoringResult,
                            @RequestParam(value = "add") boolean add);

    /**
     * 获取查询条件
     *
     * @param scoringResultQueryRequest 查询条件
     * @return 查询条件
     */
    default QueryWrapper<ScoringResult> getQueryWrapper(ScoringResultQueryRequest scoringResultQueryRequest) {
        QueryWrapper<ScoringResult> queryWrapper = new QueryWrapper<>();
        if (scoringResultQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = scoringResultQueryRequest.getId();
        String resultName = scoringResultQueryRequest.getResultName();
        String resultDesc = scoringResultQueryRequest.getResultDesc();
        String resultProp = scoringResultQueryRequest.getResultProp();
        Integer resultScoreRange = scoringResultQueryRequest.getResultScoreRange();
        Long bankid = scoringResultQueryRequest.getBankid();
        Long userId = scoringResultQueryRequest.getUserid();
        String searchText = scoringResultQueryRequest.getSearchText();
        String sortField = scoringResultQueryRequest.getSortField();
        String sortOrder = scoringResultQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("result_name", searchText).or().like("resultDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(resultName), "result_name", resultName);
        queryWrapper.like(StringUtils.isNotBlank(resultDesc), "result_desc", resultDesc);
        queryWrapper.like(StringUtils.isNotBlank(resultProp), "result_prop", resultProp);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(bankid), "bankid", bankid);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultScoreRange), "result_score_range", resultScoreRange);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    ;

    /**
     * 获取评分结果封装
     *
     * @param scoringResult 评分结果
     * @return 评分结果封装
     */
    @PostMapping("/get/vo")
    ScoringResultVO getScoringResultVO(@RequestBody ScoringResult scoringResult);

    /**
     * 分页获取评分结果封装
     *
     * @param scoringResultPage 分页对象
     * @return 分页评分结果封装
     */
    @PostMapping("/get/vo/page")
    Page<ScoringResultVO> getScoringResultVOPage(@RequestBody Page<ScoringResult> scoringResultPage);
}
