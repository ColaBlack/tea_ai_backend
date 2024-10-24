package cn.cola.serviceclient.service;


import cn.cola.common.constant.CommonConstant;
import cn.cola.common.utils.SqlUtils;
import cn.cola.model.po.Question;
import cn.cola.model.question.QuestionQueryRequest;
import cn.cola.model.vo.QuestionVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 题目服务
 *
 * @author ColaBlack
 */
@FeignClient(name = "teaai-question-service", url = "/api/question/")
public interface QuestionService {

    /**
     * 校验数据
     *
     * @param question 待校验的数据
     * @param add      是否为创建的数据进行校验
     */
    @GetMapping("/valid")
    void validQuestion(@RequestParam(value = "question") Question question,
                       @RequestParam(value = "add") boolean add);

    @PostMapping("/inner/get/one")
    Question getOne(@RequestBody LambdaQueryWrapper queryWrapper);

    @PostMapping("/inner/save")
    boolean save(@RequestBody Question question);

    @PostMapping("/inner/get/id")
    Question getById(@RequestParam("id") Long id);

    @DeleteMapping("/inner/delete/id")
    boolean removeById(@RequestParam("id") Long id);

    @PutMapping("/inner/update/id")
    boolean updateById(@RequestBody Question question);

    @GetMapping("/inner/get/page")
    Page<Question> page(@RequestParam("page") Page<Question> questionPage,
                        @RequestParam("query") QueryWrapper<Question> queryWrapper);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest 查询条件
     * @return 查询条件封装
     */
    default QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if (questionQueryRequest == null) {
            return queryWrapper;
        }
        // 从对象中取值
        Long id = questionQueryRequest.getId();
        String questionContent = questionQueryRequest.getQuestionContent();
        Long questionBankId = questionQueryRequest.getQuestionBankId();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        // 补充需要的查询条件
        // 模糊查询
        queryWrapper.like(StringUtils.isNotBlank(questionContent), "question_content", questionContent);
        // 精确查询
        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionBankId), "bankId", questionBankId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        // 排序规则
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    /**
     * 获取题目封装
     *
     * @param question 题目实体
     * @return 题目封装
     */
    @PostMapping("/get/VO")
    QuestionVO getQuestionVO(@RequestBody Question question);

    /**
     * 分页获取题目封装
     *
     * @param questionPage 分页对象
     * @return 分页题目封装
     */
    @PostMapping("/get/VO/page")
    Page<QuestionVO> getQuestionVOPage(@RequestBody Page<Question> questionPage);
}
