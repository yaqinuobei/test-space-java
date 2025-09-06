package code.traveler.test.space.java.sb.victoriametrics.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/metrics/jvm")
public class JvmMetricsController {

    @GetMapping("/memory")
    public String getMemoryMetrics(){

        return "";
    }
}
