package test.space.redis.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/redis")
public class TestController {

    @Autowired
    @Qualifier("lettuceTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/addData")
    public String getUser(){
        redisTemplate.opsForValue().set("test:name","xiaoming");
        return "success";
    }
}
