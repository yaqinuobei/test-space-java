package code.traveler.test.space.java.mcp.client.manage;

import code.traveler.test.space.java.mcp.client.controller.QueryDTO;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

@Component
public class ChatManage {

    @Value("classpath:system-message.st")
    private Resource systemResource;

    private ChatClient chatClient;

    private Map<String, ChatClient> chatModelMap;

    public ChatManage(@Autowired DashScopeChatModel dashScopeChatModel){

        ChatClient.Builder dashScopeBuilder = ChatClient.builder(dashScopeChatModel);
        ChatClient dashScopeChatClient = dashScopeBuilder.build();
        chatModelMap = Map.of("dashScope", dashScopeChatClient);

    }

    public Flux<ServerSentEvent<String>> chat(QueryDTO queryDTO){
        return null;
    }
}
