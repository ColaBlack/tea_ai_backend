package cn.cola.user.config;

import cn.cola.question.CommonMyBatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 *
 * @author ColaBlack
 */
@Configuration
@MapperScan("cn.cola.question.mapper")
public class MyBatisPlusConfig extends CommonMyBatisPlusConfig {

}