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
- 为了节省开发成本，使用了apache commons-lang3以及lombok提高开发效率
- 使用minio作为对象存储，可以实现上传的头像、题库、判题结果展示图等信息
- 默认使用glm-4-flash大模型提供AI支持
- 由于AI调用速度较慢，使用了RX java进行响应式编程，优化了前端用户等待AI响应的体验
- 出于加速判题和节省Token的考虑，使用caffine对选项的判题结果进行了本地缓存
- 由于glm-4-flash大模型自带限流等功能，所以暂时先不考虑在后端进行限流等操作，待后续有需求会补齐

#### 安装教程

1. 克隆或下载本仓库代码
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
   4.配置application.yml中的信息，
   如：数据库配置

```yaml
  # 数据库配置
  datasource:
    # todo 需替换配置
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/teaai
    username: root
    password: 123456
  # Redis 配置
  redis:
    # todo 需替换配置
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
```

AI配置

```yaml
# ai配置
# todo 需替换配置
ai:
   modelName: "glm-4-flash" # 需替换配置
   apiKey: "你的apiKey" # 需替换配置
   request-id-template: "teaAI-request-%s" # 请求id模板，可以不更换
```

minio连接配置

```yaml
# minio配置
# todo 需替换配置
minio:
   endpoint: http://你的minio地址:9000 # 需替换配置
   accesskey: 你的ak # 需替换配置
   secretkwy: 你的sk # 需替换配置
```

5 .创建src/main/resources/images文件夹，用于存放临时上传的图片
6.运行doc/create_table.sql里的建表语句创建项目所需的表
7.运行MainApplication启动项目

#### 使用说明

1. 由于你使用的对象存储未必是minio，甚至有可能想把文件存在个人的图床或本地，那么你就需要修改以下代码：

- edu/zafu/teaai/config/MinioConfig.java 用于从application.yml中读取minio的配置信息
- edu/zafu/teaai/config/MinioConfig.java 用于配置minio的连接信息
- edu/zafu/teaai/utils/MinioUtils.java 提供了一系列基于minio的操作方法，如上传等
- edu/zafu/teaai/service/FileService.java 及其实现类FileServiceImpl.java 用于上传文件到minio
- edu/zafu/teaai/controller/FileController.java 提供了上传文件的接口

2. 代码中存在很多被注释的接口，那些接口代码是后端开发时为前端预留的接口，但最终并没有使用所以通过注释的方式关闭了

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat分支
3. 提交代码
4. 新建 Pull Request
