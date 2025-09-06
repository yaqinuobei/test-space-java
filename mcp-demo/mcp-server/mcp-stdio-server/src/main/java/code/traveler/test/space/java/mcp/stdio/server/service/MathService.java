package code.traveler.test.space.java.mcp.stdio.server.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MathService {

    @Tool(description = "加法方法")
    public Integer add(Integer a, Integer b) {
        log.info("===============add方法被调用: a={}, b={}", a, b);
        return a + b;
    }

    @Tool(description = "乘法方法")
    public Integer multiply(Integer a, Integer b) {
        log.info("===============multiply方法被调用: a={}, b={}", a, b);
        return a * b;
    }
}