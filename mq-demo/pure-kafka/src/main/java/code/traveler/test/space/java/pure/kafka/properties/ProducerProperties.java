package code.traveler.test.space.java.pure.kafka.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("kafka.producer")
@Data
public class ProducerProperties {

    private String acks;

    private int retries;

    private int batchSize;

    private int lingerMs;

    private int bufferMemory;

    private String keySerializer;

    private String valueSerializer;

}
