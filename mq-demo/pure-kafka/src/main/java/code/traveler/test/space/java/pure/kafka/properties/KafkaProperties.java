package code.traveler.test.space.java.pure.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("kafka.server")
@Data
public class KafkaProperties {

    private String bootstrapServers;

    private int requestTimeoutMs;
}
