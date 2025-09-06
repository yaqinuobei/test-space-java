package code.traveler.test.space.java.sb.encrypt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("test")
@Component
@Data
public class TestProperties {

    private String password;
}
