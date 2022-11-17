package test.space.redis.demo.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置文件
 */
@EnableCaching
@Configuration
public class RedisTemplateConfig extends CachingConfigurerSupport {

    @Autowired

//    @Autowired
//    private LettucePoolingClientConfiguration lettuceClientConfiguration;
//
//    @Autowired
//    private RedisClusterConfiguration redisClusterConfiguration;
//    //    @Autowired
////    private RedisSentinelConfiguration redisSentinelConfiguration;
//    @Autowired
//    private RedisStandaloneConfiguration redisStandaloneConfiguration;
//
//
//    /**
//     * redis哨兵配置
//     *
//     * @return
//     */
//    @Bean
//    public RedisSentinelConfiguration redisSentinelConfiguration() {
//        final RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration();
//        sentinelConfig.setMaster(redisProperties.getMaster());
//        final Set<RedisNode> sentinels = new HashSet<>();
//        final String[] host = redisProperties.getRedisNodes();
//        for (final String redisHost : host) {
//            final String[] item = redisHost.split(":");
//            final String ip = item[0].trim();
//            final String port = item[1].trim();
//            sentinels.add(new RedisNode(ip, Integer.parseInt(port)));
//        }
//        sentinelConfig.setSentinels(sentinels);
//        sentinelConfig.setDatabase(redisProperties.getDatabase());
//        sentinelConfig.setPassword(RedisPassword.of(redisProperties.getPassword())); //redis 密码
//        return sentinelConfig;
//    }
//

//    /**
//     * redis 单节点配置
//     *
//     * @return
//     */
//    @Bean
//    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
//        final RedisStandaloneConfiguration standConfig = new RedisStandaloneConfiguration();
//        standConfig.setHostName(redisProperties.getHost());
//        standConfig.setPort(redisProperties.getPort());
//        standConfig.setDatabase(redisProperties.getDatabase());
//        //redis 密码
//        standConfig.setPassword(RedisPassword.of(redisProperties.getPassword()));
//        return standConfig;
//    }


//    @Bean
//    public RedisClusterConfiguration redisClusterConfiguration() {
//        final RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
//        final Set<RedisNode> sentinels = new HashSet<>();
//        final String[] host = redisProperties.getRedisClusterNodes();
//        for (final String redisHost : host) {
//            final String[] item = redisHost.split(":");
//            final String ip = item[0].trim();
//            final String port = item[1].trim();
//            sentinels.add(new RedisNode(ip, Integer.parseInt(port)));
//        }
//        clusterConfiguration.setClusterNodes(sentinels);
//        // redis 密码
//        clusterConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
//        return clusterConfiguration;
//    }

//    /**
//     * lettuce 连接池配置
//     *
//     * @return
//     */
//    @Bean
//    public LettucePoolingClientConfiguration lettucePoolConfig() {
//        final GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//
//        poolConfig.setMaxTotal(redisProperties.getMaxActive());
//        poolConfig.setMinIdle(redisProperties.getMinIdle());
//        poolConfig.setMaxIdle(redisProperties.getMaxIdle());
//        poolConfig.setMaxWaitMillis(redisProperties.getMaxWait());
//        poolConfig.setTestOnCreate(redisProperties.getTestOnCreate());
//        poolConfig.setTestOnBorrow(redisProperties.getTestOnBorrow());
//        poolConfig.setTestOnReturn(redisProperties.getTestOnReturn());
//        poolConfig.setTestWhileIdle(redisProperties.getTestWhileIdle());
//        poolConfig.setNumTestsPerEvictionRun(redisProperties.getNumTestsPerEvictionRun());
//        poolConfig.setTimeBetweenEvictionRunsMillis(redisProperties.getTimeBetweenEvictionRunsMillis());
//        poolConfig.setMinEvictableIdleTimeMillis(redisProperties.getMinEvictableIdleTimeMills());
//
//        return LettucePoolingClientConfiguration.builder().poolConfig(poolConfig)
//                .commandTimeout(Duration.ofSeconds(redisProperties.getCommandTimeout()))
//                .shutdownTimeout(Duration.ofMillis(redisProperties.getShutdownTimeout())).build();
//    }

//    /**
//     * lettuce 连接工厂
//     *
//     * @return
//     */
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        final LettuceConnectionFactory factory;
//        if (redisProperties.getIsCluster()) {
//            factory = new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
//        } else {
//            factory = new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
//        }
//        return factory;
//    }

    @Bean(name = "lettuceTemplate")
//    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        //StringRedisTemplate的构造方法中默认设置了stringSerializer
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();

        template.setConnectionFactory(redisConnectionFactory);

        //设置开启事务
        //template.setEnableTransactionSupport(true);
        //set key serializer
        final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
                Object.class);
        final ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        om.activateDefaultTyping(om.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL);//可以对实体进行反序列化
        jackson2JsonRedisSerializer.setObjectMapper(om);

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

}

