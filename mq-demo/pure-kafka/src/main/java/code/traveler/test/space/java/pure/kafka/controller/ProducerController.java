package code.traveler.test.space.java.pure.kafka.controller;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

@RestController
@RequestMapping("/v1/producer")
public class ProducerController {

    @Autowired
    private Producer producer;

    private Collection<String> topics = new LinkedBlockingQueue<>();

    //生产消息至动态topic
    @PostMapping("/send/{topic}")
    public void regist(@PathVariable("topic") String topic, @RequestParam("message") String message){
        producer.send(new ProducerRecord<String, String>("test",   message));
    }
}
