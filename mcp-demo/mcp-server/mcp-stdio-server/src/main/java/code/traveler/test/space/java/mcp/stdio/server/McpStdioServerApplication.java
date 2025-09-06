package code.traveler.test.space.java.mcp.stdio.server;

import code.traveler.test.space.java.mcp.stdio.server.service.MathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class McpStdioServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(McpStdioServerApplication.class, args);
        log.info("===============McpServerApplication服务启动成功");
    }

    @Bean
    public ToolCallbackProvider mathTools(MathService mathService) {
        return MethodToolCallbackProvider.builder()
                                         .toolObjects(mathService).build();
    }


}
