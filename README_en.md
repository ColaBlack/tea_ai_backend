# teaai-backend

**Read this in Chinese: [中文](README.md)**

**For English documentation, please click here: [English](README_en.md)**

> This README document is translated from Chinese to English by chat-glm 4,and I haven't checked the correctness of the
> translation.Therefore,if there is any error in the translation, please let me know.

#### Introduction

The teaai backend project is a companion backend project for the frontend
project [teaai-frontend](https://gitee.com/colablack/teaai-frontend).

#### Software Architecture

- The project is developed using Spring Boot.
- Data access layer development is done using MyBatis and MyBatis Plus.
- To prepare for a possible surge in user numbers in the future, Spring-Session-Data-Redis is used to implement
  distributed login (this solution is chosen due to its low invasiveness).
- To save on development costs, Apache Commons Lang3, and Lombok are used to improve development efficiency.
- Minio is used to To store user-uploaded avatars, question banks, and judging result display images, among other
  information.
- By default, the GLM-4-Flash large model is used to provide AI support.
- Since AI calls are slow, RX Java is used for reactive programming to optimize the user experience of waiting for AI
  responses on the frontend.
- To speed up problem judging and save on tokens, Caffeine is used to locally cache the judging results of options.
- Since the GLM-4-Flash large model has its own rate limiting and other functions, we currently do not consider
  implementing rate limiting on the backend. This will be addressed if there is a need in the future.

#### Installation Instructions

1. Clone or download the repository code.
2. Run
    ```bash
    mvn dependency:resolve
    ```
   or use IntelliJ IDEA to install the required Maven dependencies for the project.
3. Modify the configuration information in application.yml, such as:
    - MySQL or other relational database drivers, addresses, accounts, and passwords.
    - Redis address, account, and password.
    - Tencent Cloud or other object storage configuration information.
4. Modify the configuration information in application.yml, such as:
   the configuration of the database,

```yaml
  # database configuration
  datasource:
    # todo you have to rewrite the configuration
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/teaai
    username: root
    password: 123456
  # Redis configuration
  redis:
    # todo you have to rewrite the configuration
    database: 1
    host: localhost
    port: 6379
    timeout: 5000
```

AI configuration,

```yaml
# AI configuration
# todo you have to rewrite the configuration
ai:
  modelName: "glm-4-flash" # please replace with the model you want to use
  apiKey: "your_apiKey" # please replace with your apiKey
  request-id-template: "teaAI-request-%s" # this is used to generate request ids for AI requests ,and you needn't change it right now.
```

and minio configuration.

```yaml
# minio configuration
# todo you have to rewrite the configuration
minio:
  endpoint: http://localhost:9000 # may be different from the example
  accesskey: your-access-key # tell me your ak and sk
  secretkwy: your-secret-key # you must replace it with your own
```

5.Create a folder named src/main/resources/images to store temporarily uploaded images.
6.Run the SQL statements in sql/create_table.sql to create the tables required for the project.
7.Run MainApplication to start the project.

#### Usage Instructions

1. Since the object storage you use may not be Minio, or you might want to store files in a personal image hosting
   service or locally, I suggest you modify the code related to file storage.

- edu/zafu/teaai/config/MinioConfig.java It is used to read the configuration information in application.yml .
- edu/zafu/teaai/config/MinioConfig.java It is used to configure the minio client.
- edu/zafu/teaai/utils/MinioUtils.java This class provides some basic operations on minio, such as uploading.
- edu/zafu/teaai/service/FileService.java and it's implementation FileServiceImpl.java offer some basic operations on minio.
- edu/zafu/teaai/controller/FileController.java This file is used to handle file upload requests.

2. There is a lot of commented interface code in the code. Those interfaces are reserved for the frontend during backend
   development but were ultimately not used, so they are closed with comments.

#### Contributing

1. Fork this repository.
2. Create a new Feat branch.
3. Commit your code.
4. Create a Pull Request.
