package code.traveler.test.space.java.mcp.client.manage;

import code.traveler.test.space.java.mcp.client.controller.ChatPlatformAndModelOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@Component
public class ChatManage {

    @Autowired
    private ChatClientFactory clientFactory;

    @Value("classpath:system-message1.st")
    private Resource systemResource;

    public String chatByCall(ChatPlatformAndModelOptions chatOptions) {
        ChatClient chatClient = clientFactory.getChatClient(chatOptions);

        AssistantMessage output = chatClient
                .prompt()
                .system(systemResource)
                .user(chatOptions.getMessage())
                .advisors(a -> a.param(CONVERSATION_ID, chatOptions.getUsername()))
                .call()
                .chatResponse()
                .getResult()
                .getOutput();

        String response=output.getText();

        return response;
    }

    public Flux<ServerSentEvent<String>> chatByStream(ChatPlatformAndModelOptions chatOptions) {
        ChatClient chatClient = clientFactory.getChatClient(chatOptions);

        return chatClient.prompt()
                         .system(systemResource)
                         .user(chatOptions.getMessage())
                         .stream()
                         .content()
                         .map(content -> ServerSentEvent.builder(content)
                                                        .event("message")
                                                        .build())
                         .onErrorResume(e -> Flux.just(ServerSentEvent.<String>builder()
                                                                      .event("error")
                                                                      .data(e.getMessage())
                                                                      .build()));
    }
}
