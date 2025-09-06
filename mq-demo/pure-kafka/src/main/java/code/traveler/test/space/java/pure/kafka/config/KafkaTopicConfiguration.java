package code.traveler.test.space.java.pure.kafka.config;

import code.traveler.test.space.java.pure.kafka.properties.ConsumerProperties;
import code.traveler.test.space.java.pure.kafka.properties.KafkaProperties;
import code.traveler.test.space.java.pure.kafka.properties.ProducerProperties;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaTopicConfiguration {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Autowired
    private ProducerProperties producerProperties;

    @Autowired
    private ConsumerProperties consumerProperties;

//    @Bean
//    public KafkaAdmin kafkaAdmin(){
//        Map<String,Object> prop = new HashMap<>();
//        prop.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServers);
//        return new KafkaAdmin(prop);
//    }

    @Bean
    public AdminClient adminClient() {
        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProperties.getRequestTimeoutMs());
        return AdminClient.create(props);
    }

    @Bean
    public Producer<String, String> producer() {
        Properties props = new Properties();
        //kafka 集群，broker-list
        props.put("bootstrap.servers", kafkaProperties.getBootstrapServers());

        props.put("acks", producerProperties.getAcks());
        //重试次数
        props.put("retries", producerProperties.getRetries());
        //批次大小
        props.put("batch.size", producerProperties.getBatchSize());
        //等待时间
        props.put("linger.ms", producerProperties.getLingerMs());
        //RecordAccumulator 缓冲区大小
        props.put("buffer.memory", producerProperties.getBufferMemory());

        props.put("key.serializer", producerProperties.getKeySerializer());
        props.put("value.serializer", producerProperties.getValueSerializer());

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        return producer;
    }

    @Bean
    public Consumer<String, String> consumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaProperties.getBootstrapServers());
        props.put("group.id", consumerProperties.getGroupId());
        // 开启自动提交 offset 功能
        props.put("enable.auto.commit", consumerProperties.getEnableAutoCommit());
        // 自动提交 offset 的时间间隔
        props.put("auto.commit.interval.ms", consumerProperties.getAutoCommitIntervalMs());
        props.put("key.deserializer", consumerProperties.getKeySerializer());
        props.put("value.deserializer", consumerProperties.getValueSerializer());
        KafkaConsumer<String, String> consumer = new KafkaConsumer(props);

        return consumer;
    }

}
