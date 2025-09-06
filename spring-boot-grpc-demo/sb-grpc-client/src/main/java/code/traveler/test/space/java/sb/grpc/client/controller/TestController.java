package code.traveler.test.space.java.sb.grpc.client.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import code.traveler.test.space.java.sb.grpc.client.server.HelloGrpcClient;
import code.traveler.test.space.java.sb.grpc.client.server.StudentClient;

@RestController
@RequestMapping("/v1/test")
@Slf4j
public class TestController {

    @Autowired
    private HelloGrpcClient grpcClient;

    @Autowired
    private StudentClient studentClient;

    @GetMapping("/sendMsgBlocking")
    public String sendMsgBlocking(String msg) {
        grpcClient.sendMessageBlocking(msg);
        return "success";
    }

    @GetMapping("/sendMsg")
    public String sendMsg(String msg) {
        grpcClient.sendMessage(msg);
        return "success";
    }


    @GetMapping("/student")
    public String student(Integer sNum, String name) {
        studentClient.getStudentDetai(sNum, name);
        return "success";
    }
}
