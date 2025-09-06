package code.traveler.test.space.java.mcp.client.controller;

import code.traveler.test.space.java.mcp.client.manage.ChatManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RequestMapping("/chat/xx")
@RestController
@Validated
public class ChatController {

    @Autowired
    private ChatManage chatManage;

    @PostMapping("/askQuetion")
    public Flux<ServerSentEvent<String>> queryCapacity(@RequestBody @Valid QueryDTO queryDTO) {

        return chatManage.chat(queryDTO);
    }
}
