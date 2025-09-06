package code.traveler.test.space.java.pure.kafka.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

//    @Autowired
//    private BinderAwareChannelResolver resolver;

//    @RequestMapping(path = "/{target}", method = POST, consumes = "*/*")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void handleRequest(@RequestBody String body, @PathVariable("target") String target,
//                              @RequestHeader(HttpHeaders.CONTENT_TYPE) Object contentType) {
//        sendMessage(body, target, contentType);
//    }

//    private void sendMessage(String body, String target, Object contentType) {
//        resolver.resolveDestination(target).send(MessageBuilder.createMessage(body,
//                new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, contentType))));
//    }

    @Value("${clientId}")
    private String clientId;

    @GetMapping("test")
    public String test(){
            return clientId;
    }

}
