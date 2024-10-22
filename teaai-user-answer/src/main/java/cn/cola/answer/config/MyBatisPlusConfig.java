package cn.cola.answer.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 *
 * @author ColaBlack
 */
@Configuration
@MapperScan("cn.cola.question.mapper")
public class MyBatisPlusConfig extends CommonMyBatisPlusConfig{

}