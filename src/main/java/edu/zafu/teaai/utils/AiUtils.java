package edu.zafu.teaai.utils;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;
import edu.zafu.teaai.config.AiConfig;
import io.reactivex.Flowable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * AI调用模块
 *
 * @author ColaBlack
 */
@Component
public class AiUtils {

    /**
     * AI调用配置信息
     */
    @Resource
    private AiConfig aiConfig;

    /**
     * AI调用客户端
     */
    @Resource
    private ClientV4 client;

    @PostConstruct
    public void init() {
        // 确保在构建客户端之前aiConfig不为null
        if (aiConfig == null) {
            throw new IllegalStateException("AiConfig对象初始化失败");
        }
        // 确保apiKey不为null
        String apiKey = aiConfig.getApiKey();
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("API key 不能为空");
        }
        // 使用配置信息初始化客户端
        client = new ClientV4.Builder(apiKey).build();
    }

    /**
     * 调用AI接口(同步)
     *
     * @param prompt 提示词
     * @return AI返回的答案
     */
    public String aiCaller(String prompt) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        messages.add(chatMessage);
        String requestId = String.format(aiConfig.getRequestIdTemplate(), System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(aiConfig.getModelName())
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();

        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
    }

    /**
     * 调用AI接口(SSE)
     *
     * @author ColaBlack
     */
    public Flowable<ModelData> aiCallerFlow(String prompt) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        messages.add(chatMessage);
        String requestId = java.lang.String.format(aiConfig.getRequestIdTemplate(), System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(aiConfig.getModelName()).
                stream(Boolean.TRUE).
                invokeMethod(Constants.invokeMethod)
                .messages(messages)
                .requestId(requestId)
                .build();
        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getFlowable();
    }
}
