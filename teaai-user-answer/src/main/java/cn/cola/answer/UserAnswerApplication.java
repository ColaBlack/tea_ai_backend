package cn.cola.answer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 主类（项目启动入口）
 *
 * @author ColaBlack
 */
@SpringBootApplication
@MapperScan("cn.cola.answer.mapper")
@ComponentScan("cn.cola")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.cola.serviceclient.service"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class UserAnswerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserAnswerApplication.class, args);
    }

}
