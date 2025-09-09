package code.traveler.test.space.java.mcp.client.config;

import com.alibaba.cloud.ai.memory.jdbc.PostgresChatMemoryRepository;
import com.alibaba.cloud.ai.memory.redis.LettuceRedisChatMemoryRepository;
import com.alibaba.cloud.ai.memory.redis.RedissonRedisChatMemoryRepository;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class ChatMemoryConfig {

    private final int MAX_MESSAGES = 20;

    @Value("${spring.ai.chat.memory.repository.redis.host}")
    private String redisHost;
    @Value("${spring.ai.chat.memory.repository.redis.port}")
    private int redisPort;
    @Value("${spring.ai.chat.memory.repository.redis.password}")
    private String redisPassword;
    @Value("${spring.ai.chat.memory.repository.redis.timeout}")
    private int redisTimeout;

    @Value("${spring.ai.chat.memory.repository.jdbc.postgres.jdbc-url}")
    private String postgresJdbcUrl;
    @Value("${spring.ai.chat.memory.repository.jdbc.postgres.username}")
    private String postgresUsername;
    @Value("${spring.ai.chat.memory.repository.jdbc.postgres.password}")
    private String postgresPassword;
    @Value("${spring.ai.chat.memory.repository.jdbc.postgres.driver-class-name}")
    private String postgresDriverClassName;

    @Bean
    public PostgresChatMemoryRepository postgresChatMemoryRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(postgresDriverClassName);
        dataSource.setUrl(postgresJdbcUrl);
        dataSource.setUsername(postgresUsername);
        dataSource.setPassword(postgresPassword);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return PostgresChatMemoryRepository
                .postgresBuilder()
                .jdbcTemplate(jdbcTemplate)
                .build();
    }

    @Bean
    public LettuceRedisChatMemoryRepository redisChatMemoryRepository() {
        return LettuceRedisChatMemoryRepository
                .builder()
                .host(redisHost)
                .port(redisPort)
                // 若没有设置密码则注释该项
//                .password(redisPassword)
                .timeout(redisTimeout)
                .build();
    }


    @Bean
    public InMemoryChatMemoryRepository inMemoryChatMemoryRepository(){
        return new InMemoryChatMemoryRepository();
    }

    @Bean
    public MessageWindowChatMemory messageWindowChatMemoryInMemory(@Qualifier("inMemoryChatMemoryRepository") ChatMemoryRepository inMemoryChatMemoryRepository){
        MessageWindowChatMemory.Builder builder =
                MessageWindowChatMemory.builder().chatMemoryRepository(inMemoryChatMemoryRepository).maxMessages(MAX_MESSAGES);
        return builder.build();
    }

    @Bean
    public MessageWindowChatMemory messageWindowChatMemoryInRedis(@Qualifier("redisChatMemoryRepository") ChatMemoryRepository redisChatMemoryRepository){
        MessageWindowChatMemory.Builder builder =
                MessageWindowChatMemory.builder().chatMemoryRepository(redisChatMemoryRepository).maxMessages(MAX_MESSAGES);
        return builder.build();
    }

    @Bean
    public MessageWindowChatMemory messageWindowChatMemoryInPostgres(@Qualifier("postgresChatMemoryRepository") ChatMemoryRepository postgresChatMemoryRepository){
        MessageWindowChatMemory.Builder builder =
                MessageWindowChatMemory.builder().chatMemoryRepository(postgresChatMemoryRepository).maxMessages(MAX_MESSAGES);
        return builder.build();
    }
}
