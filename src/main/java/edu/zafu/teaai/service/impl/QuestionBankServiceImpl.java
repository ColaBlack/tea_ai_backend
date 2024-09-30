package edu.zafu.teaai.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zafu.teaai.mapper.QuestionBankMapper;
import edu.zafu.teaai.model.po.QuestionBank;
import edu.zafu.teaai.service.QuestionBankService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【question_bank(题库表)】的数据库操作Service实现
* @createDate 2024-09-30 08:19:09
*/
@Service
public class QuestionBankServiceImpl extends ServiceImpl<QuestionBankMapper, QuestionBank>
    implements QuestionBankService {

}




