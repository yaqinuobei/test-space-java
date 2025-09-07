package code.traveler.test.space.java.mcp.client.manage;

import code.traveler.test.space.java.mcp.client.controller.ChatPlatformAndModelOptions;
import com.alibaba.cloud.ai.dashscope.audio.transcription.AudioTranscriptionModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.deepseek.DeepSeekChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatClientFactory {

    //聊天模型
    private Map<String, ChatModel> chatModels = new HashMap<>();


    //文生视频模型
    private Map<String, AudioTranscriptionModel> audioModels;

    public ChatClientFactory(DashScopeChatModel dashScopeChatModel, DeepSeekChatModel deepSeekChatModel,
                             OllamaChatModel ollamaChatModel) {
        chatModels.put("dashscope", dashScopeChatModel);
        chatModels.put("deepseek", deepSeekChatModel);
        chatModels.put("ollama", ollamaChatModel);
    }


    public ChatClient getChatClient(ChatPlatformAndModelOptions chatOptions) {
        ChatModel chatModel = chatModels.get(chatOptions.getPlatform());
        if (chatModel == null) {
            throw new IllegalArgumentException("Unknown platform: " + chatOptions.getPlatform());
        }

        ChatClient.Builder chatClientBuilder = ChatClient.builder(chatModel);

        ChatOptions.Builder chatOptionsBuilder = ChatOptions.builder();
        if (Objects.nonNull(chatOptions.getModel())) {
            chatOptionsBuilder.model(chatOptions.getModel());
        }
        if (Objects.nonNull(chatOptions.getTemperature())) {
            chatOptionsBuilder.temperature(chatOptions.getTemperature());
        }

        chatClientBuilder.defaultOptions(chatOptionsBuilder.build());
        return chatClientBuilder.build();
    }

    public List<String> getAvailableModels() {
        return new ArrayList<>(chatModels.keySet());
    }
}
