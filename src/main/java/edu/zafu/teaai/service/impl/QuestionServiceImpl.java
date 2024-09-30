package edu.zafu.teaai.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zafu.teaai.mapper.QuestionMapper;
import edu.zafu.teaai.model.po.Question;
import edu.zafu.teaai.service.QuestionService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2024-09-30 08:19:09
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService {

}




