package cn.cola.result;

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
@MapperScan("cn.cola.result.mapper")
@ComponentScan("cn.cola")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"cn.cola.serviceclient.service"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class ScoringResultApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoringResultApplication.class, args);
    }

}
