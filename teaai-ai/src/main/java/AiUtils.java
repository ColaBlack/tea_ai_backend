import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.*;

import config.AiConfig;
import io.reactivex.Flowable;

import java.util.ArrayList;
import java.util.List;

/**
 * AI调用模块
 *
 * @author ColaBlack
 */
public class AiUtils {

    /**
     * 业务ID
     */
    private static final String REQUEST_ID_TEMPLATE = "teaAI-request";


    private static final ClientV4 CLIENT = new ClientV4.Builder(AiConfig.API_KEY).build();

    /**
     * 调用AI接口(同步)
     *
     * @param prompt 提示词
     * @return AI返回的答案
     */
    public static String aiCaller(String prompt) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        messages.add(chatMessage);
        String requestId = String.format(REQUEST_ID_TEMPLATE, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model(AiConfig.MODEL_NAME).stream(Boolean.FALSE).invokeMethod(Constants.invokeMethod).messages(messages).requestId(requestId).build();
        ModelApiResponse invokeModelApiResp = CLIENT.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getData().getChoices().get(0).getMessage().getContent().toString();
    }

    /**
     * 调用AI接口(SSE)
     *
     * @author ColaBlack
     */
    public static Flowable<ModelData> aiCallerFlow(String prompt) {
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt);
        messages.add(chatMessage);
        String requestId = String.format(REQUEST_ID_TEMPLATE, System.currentTimeMillis());
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder().model("glm-4-flash").stream(Boolean.TRUE).invokeMethod(Constants.invokeMethod).messages(messages).requestId(requestId).build();
        ModelApiResponse invokeModelApiResp = CLIENT.invokeModelApi(chatCompletionRequest);
        return invokeModelApiResp.getFlowable();
    }
}
