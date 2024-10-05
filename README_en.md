# teaai-backend

**Read this in Chinese: [中文](README.md)**

**For English documentation, please click here: [English](README_en.md)**

> This README document is translated from Chinese to English by chat-glm 4,and I haven't checked the correctness of the translation.Therefore,if there is any error in the translation, please let me know.

#### Introduction
The teaai backend project is a companion backend project for the frontend project [teaai-frontend](https://gitee.com/colablack/teaai-frontend).

#### Software Architecture

- The project is developed using Spring Boot.
- Data access layer development is done using MyBatis and MyBatis Plus.
- To prepare for a possible surge in user numbers in the future, Spring-Session-Data-Redis is used to implement distributed login (this solution is chosen due to its low invasiveness).
- To save on development costs, Hutool, Apache Commons Lang3, and Lombok are used to improve development efficiency.
- By default, the GLM-4-Flash large model is used to provide AI support.
- Since AI calls are slow, RX Java is used for reactive programming to optimize the user experience of waiting for AI responses on the frontend.
- To speed up problem judging and save on tokens, Caffeine is used to locally cache the judging results of options.
- Since the GLM-4-Flash large model has its own rate limiting and other functions, we currently do not consider implementing rate limiting on the backend. This will be addressed if there is a need in the future.
- The project comes with Tencent Cloud object storage capabilities, which can be used to store user-uploaded avatars, question banks, and judging result display images, among other information.

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
4. To avoid accidentally open-sourcing the AI API_KEY during development, it is gitignored. To run the project smoothly, please create a new Java interface `AiConfig.java` in the folder:
   > src/main/java/edu/zafu/teaai/config

   Content as follows:
    ```java
    package edu.zafu.teaai.config;

    /**
     * AI configuration information
     *
     * @author ColaBlack
     */
    public interface AiConfig {

        /**
         * API key
         */
        String API_KEY = "xxxxxx";
        //todo: Make sure to replace it with your own API key

        /**
         * Model name
         */
        String MODEL_NAME = "glm-4-flash";
    }
    ```

5. Run the SQL statements in sql/create_table.sql to create the tables required for the project.
6. Run MainApplication to start the project.

#### Usage Instructions

1. Since the object storage you use may not be Tencent Cloud, or you might want to store files in a personal image hosting service (like me) or locally, the code for calling Tencent Cloud object storage is not complete and can be deleted. You can also complete it as needed.
    - The code related to this is in:
        - src/main/java/edu/zafu/teaai/config/CosClientConfig.java
        - src/main/java/edu/zafu/teaai/constant/FileConstant.java
        - src/main/java/edu/zafu/teaai/controller/FileController.java
        - src/main/java/edu/zafu/teaai/manager/CosManager.java

2. There is a lot of commented interface code in the code. Those interfaces are reserved for the frontend during backend development but were ultimately not used, so they are closed with comments.

#### Contributing

1. Fork this repository.
2. Create a new Feat branch.
3. Commit your code.
4. Create a Pull Request.
