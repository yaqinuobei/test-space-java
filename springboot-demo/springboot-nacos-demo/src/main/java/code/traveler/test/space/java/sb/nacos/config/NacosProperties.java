package code.traveler.test.space.java.sb.nacos.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("demo.nacos")
@Data
public class NacosProperties {

    private String ip;

    private String port;

    private String username;

    private String password;

    private String namespace;

    private String group;

    public String getServerAddr(){

        return ip + ":" + port;
    }

}
