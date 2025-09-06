package code.traveler.test.space.java.pure.grpc.server.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import code.traveler.test.space.java.pure.grpc.server.service.impl.CatServiceImpl;

@RestController
@RequestMapping("/v1/test")
@Slf4j
public class TestController {

    @Qualifier("catServiceImpl")
    @Autowired
    private CatServiceImpl catServiceImpl;

    @GetMapping("/add")
    public String getDetail(String sNum) {
        catServiceImpl.addRequest(sNum);
        return "success";
    }
}
