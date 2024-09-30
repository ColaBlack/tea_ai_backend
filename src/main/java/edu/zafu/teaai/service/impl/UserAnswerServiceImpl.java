package edu.zafu.teaai.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.zafu.teaai.mapper.UserAnswerMapper;
import edu.zafu.teaai.model.po.UserAnswer;
import edu.zafu.teaai.service.UserAnswerService;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【user_answer(用户答题记录)】的数据库操作Service实现
* @createDate 2024-09-30 08:19:09
*/
@Service
public class UserAnswerServiceImpl extends ServiceImpl<UserAnswerMapper, UserAnswer>
    implements UserAnswerService{

}




