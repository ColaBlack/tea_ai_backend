package cn.cola.serviceclient.service;


import cn.cola.common.constant.CommonConstant;
import cn.cola.common.utils.SqlUtils;
import cn.cola.model.po.UserAnswer;
import cn.cola.model.useranswer.UserAnswerQueryRequest;
import cn.cola.model.vo.UserAnswerVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户答案服务
 *
 * @author ColaBlack
 */
@FeignClient(value = "teaai-user-answer", url = "/api/user/answer")
public interface UserAnswerService {

    @PutMapping("/inner/save")
    boolean save(@RequestBody UserAnswer userAnswer);

    @PutMapping("/inner/update/id")
    boolean updateById(@RequestBody UserAnswer userAnswer);

    @GetMapping("/inner/get/id")
    UserAnswer getById(@RequestParam("id") Long id);


    @DeleteMapping("/inner/remove/id")
    boolean removeById(@RequestParam("id") Long id);

    @GetMapping("/inner/get/page")
    Page<UserAnswer> page(@RequestParam("page") Page<UserAnswerVO> page,@RequestParam("queryWrapper") QueryWrapper<UserAnswer> queryWrapper);


    /**
     * 校验数据
     *
     * @param userAnswer 待校验的数据
     * @param add        是否为创建的数据进行校验
     */
    @GetMapping("/valid")
    void validUserAnswer(@RequestParam(value = "userAnswer") UserAnswer userAnswer,
                         @RequestParam(value = "add") boolean add);

    /**
     * 获取查询条件
     *
     * @param userAnswerQueryRequest 查询条件
     * @return 查询条件封装
     */
    default QueryWrapper<UserAnswer> getQueryWrapper(UserAnswerQueryRequest userAnswerQueryRequest) {
        QueryWrapper<UserAnswer> queryWrapper = new QueryWrapper<>();
        if (userAnswerQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = userAnswerQueryRequest.getId();
        Long bankid = userAnswerQueryRequest.getBankid();
        Integer bankType = userAnswerQueryRequest.getBanktype();
        Integer scoringStrategy = userAnswerQueryRequest.getScoringStrategy();
        String choices = userAnswerQueryRequest.getChoices();
        Long resultId = userAnswerQueryRequest.getResultId();
        String resultName = userAnswerQueryRequest.getResultName();
        String resultDesc = userAnswerQueryRequest.getResultDesc();
        String resultPicture = userAnswerQueryRequest.getResultPicture();
        Integer resultScore = userAnswerQueryRequest.getResultScore();
        Long userId = userAnswerQueryRequest.getUserId();
        String searchText = userAnswerQueryRequest.getSearchText();
        String sortField = userAnswerQueryRequest.getSortField();
        String sortOrder = userAnswerQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 从多字段中搜索
        if (StringUtils.isNotBlank(searchText)) {
            // 需要拼接查询条件
            queryWrapper.and(qw -> qw.like("resultName", searchText).or().like("resultDesc", searchText));
        }
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(choices), "choices", choices);
        queryWrapper.like(StringUtils.isNotBlank(resultName), "result_name", resultName);
        queryWrapper.like(StringUtils.isNotBlank(resultDesc), "result_desc", resultDesc);
        queryWrapper.like(StringUtils.isNotBlank(resultPicture), "result_oicture", resultPicture);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultId), "resultId", resultId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(bankid), "bankID", bankid);
        queryWrapper.eq(ObjectUtils.isNotEmpty(bankType), "bankType", bankType);
        queryWrapper.eq(ObjectUtils.isNotEmpty(resultScore), "result_score", resultScore);
        queryWrapper.eq(ObjectUtils.isNotEmpty(scoringStrategy), "scoring_strategy", scoringStrategy);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    /**
     * 获取用户答案封装
     *
     * @param userAnswer 用户答案
     * @return 用户答案封装
     */
    @PostMapping("/get/vo")
    UserAnswerVO getUserAnswerVO(@RequestBody UserAnswer userAnswer);

    /**
     * 分页获取用户答案封装
     *
     * @param userAnswerPage 分页对象
     * @return 分页用户答案封装
     */
    @PostMapping("/get/vo/page")
    Page<UserAnswerVO> getUserAnswerVOPage(@RequestBody Page<UserAnswer> userAnswerPage);
}
