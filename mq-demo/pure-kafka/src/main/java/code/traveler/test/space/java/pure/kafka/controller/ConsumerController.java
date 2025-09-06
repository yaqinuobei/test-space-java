package code.traveler.test.space.java.pure.kafka.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Value(value = "${kafka.bootstrapServers:localhost:9092}")
    private String bootstrapServers;


    //动态消费topic


}
