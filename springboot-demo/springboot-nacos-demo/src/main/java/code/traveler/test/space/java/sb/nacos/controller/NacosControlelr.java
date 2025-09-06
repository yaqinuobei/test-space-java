package code.traveler.test.space.java.sb.nacos.controller;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/nacos")
public class NacosControlelr {

    @Autowired
    private ConfigService configService;

    @GetMapping("/config")
    public String getConfig(){
        try {
            return configService.getConfig("env.json","DEFAULT_GROUP",3000);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
