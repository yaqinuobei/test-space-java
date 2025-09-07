package code.traveler.test.space.java.mcp.client.controller;

import code.traveler.test.space.java.mcp.client.manage.ChatClientFactory;
import code.traveler.test.space.java.mcp.client.manage.ChatManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/chat")
@RestController
@Validated
public class ChatController {

    @Autowired
    private ChatManage chatManage;

    @Autowired
    private ChatClientFactory clientFactory;

    @PostMapping("/askQuetion/call")
    public String chatByCall(@RequestBody @Valid ChatPlatformAndModelOptions queryDTO) {

        return chatManage.chatByCall(queryDTO);
    }

    @PostMapping("/askQuetion/stream")
    public Flux<ServerSentEvent<String>> chatByStream(@RequestBody @Valid ChatPlatformAndModelOptions queryDTO) {

        return chatManage.chatByStream(queryDTO);
    }

    @GetMapping("/models")
    public List<String> listAvailableModels() {
        return clientFactory.getAvailableModels();
    }

}
