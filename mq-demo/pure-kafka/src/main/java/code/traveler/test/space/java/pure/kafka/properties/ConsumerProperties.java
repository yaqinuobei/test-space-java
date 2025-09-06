package code.traveler.test.space.java.pure.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("kafka.consumer")
@Data
public class ConsumerProperties {

    private String groupId;

    private String enableAutoCommit;

    private String  autoCommitIntervalMs;

    private String keySerializer;

    private String valueSerializer;

}
