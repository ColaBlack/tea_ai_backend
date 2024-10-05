# teaai-backend

**Read this in English: [English](README_en.md)**

**想看中文文档请点击这里：[中文](README.md)**

#### 介绍
teaai后端项目
是与前端项目[teaai-forntend](https://gitee.com/colablack/teaai-frontend)配套的后端项目

#### 软件架构

- 使用spring boot进行项目开发
- 使用mybatis和mybatis plus进行数据访问层开发
- 出于后期可能的用户量激增问题，先使用了spring-session-data-redis实现分布式登录（由于此方案侵入性极低所以才提前使用）
- 为了节省开发成本，使用了Hutool和apache commons-lang3以及lombok提高开发效率
- 默认使用glm-4-flash大模型提供AI支持
- 由于AI调用速度较慢，使用了RX java进行响应式编程，优化了前端用户等待AI响应的体验
- 出于加速判题和节省Token的考虑，使用caffine对选项的判题结果进行了本地缓存
- 由于glm-4-flash大模型自带限流等功能，所以暂时先不考虑在后端进行限流等操作，待后续有需求会补齐
- 项目自带了腾讯云对象存储能力，可以用于存储用户上传的头像、题库、判题结果展示图等信息

#### 安装教程

1.  克隆或下载本仓库代码
2. 运行
```bash
mvn dependency:resolve
```
或使用IntelliJ IDEA
安装项目所需的maven依赖
3. 修改application.yml的配置信息如：
MySQL数据库或其他关系型数据库的驱动、地址、账号和密码
redis的地址、账号和密码
腾讯云或其他对象存储的配置信息
4.开发时为了避免不小心把调用AI的API_KEY一并开源所以将AI的配置gitignore了。
为了顺利运行并项目，请在
> src/main/java/edu/zafu/teaai/config
文件夹
新建java接口AiConfig.java
内容如下
```java
package edu.zafu.teaai.config;

/**
 * AI配置信息
 *
 * @author ColaBlack
 */
public interface AiConfig {

    /**
     * API key
     */
    String API_KEY = "xxxxxx";
    //todo: 务必换成自己的API key

    /**
     * 模型名称
     */
    String MODEL_NAME = "glm-4-flash";
}

```

5.运行sql/create_table.sql里的建表语句创建项目所需的表
6.运行MainApplication启动项目

#### 使用说明

1.  由于你使用的对象存储未必是腾讯云，甚至有可能想把文件存在个人的图床（就是我）或本地，所以调用腾讯云对象存储的代码并不完全，也可以删除需要则自己补全
其中
- src/main/java/edu/zafu/teaai/config/CosClientConfig.java
- src/main/java/edu/zafu/teaai/constant/FileConstant.java
- src/main/java/edu/zafu/teaai/controller/FileController.java
- src/main/java/edu/zafu/teaai/manager/CosManager.java
的代码均与之相关

2. 代码中存在很多被注释的接口，那些接口代码是后端开发时为前端预留的接口，但最终并没有使用所以通过注释的方式关闭了

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat分支
3.  提交代码
4.  新建 Pull Request
