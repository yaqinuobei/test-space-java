package code.traveler.test.space.java.pure.kafka.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AlterConfigOp;
import org.apache.kafka.clients.admin.AlterConfigsResult;
import org.apache.kafka.clients.admin.Config;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.CreatePartitionsResult;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeConfigsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewPartitions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.config.ConfigResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/v1/topic")
@Slf4j
public class TopicController {

    @Autowired
    private AdminClient adminClient;

    //create topic
    @RequestMapping(path="/create",method = GET)
    public void create(@RequestParam String topic,@RequestParam int partitions,
                       @RequestParam int replicas ) throws ExecutionException,
            InterruptedException {

        NewTopic newTopic = new NewTopic(topic, partitions, (short) replicas);
        CreateTopicsResult topics = adminClient.createTopics(Collections.singletonList(newTopic));
        // 避免客户端连接太快断开而导致Topic没有创建成功
        Thread.sleep(500);
        log.info("{}", topics.all().get());


    }

    //delete topic
    @RequestMapping(path="/delete",method = GET)
    public void delete(@RequestParam String topic) throws ExecutionException, InterruptedException {

        DeleteTopicsResult result = adminClient.deleteTopics(Collections.singleton(topic));
        log.info("{}",result.all().get());
    }

    //list topic
    @RequestMapping(path="/list",method = GET)
    public void list(@RequestParam String topic) throws ExecutionException, InterruptedException {
        ListTopicsOptions listTopicsOptions = new ListTopicsOptions();
        listTopicsOptions.listInternal(true);

        ListTopicsResult listTopicsResult = adminClient.listTopics(listTopicsOptions);
        Collection<TopicListing> topicListings = listTopicsResult.listings().get();
        log.info("{}", topicListings);
    }

    //detail topic
    @RequestMapping(path="/detailTopic",method = GET)
    public void detailTopic(@RequestParam String topic) throws ExecutionException, InterruptedException {
        DescribeTopicsResult topicsResult = adminClient.describeTopics(Arrays.asList("my-admin-topic"));
        Map<String, TopicDescription> topicDescriptionMap = topicsResult.allTopicNames().get();
        topicDescriptionMap.forEach((k,v)->{
            log.info("name:{},desc:{}",k,v);
        });
    }

    //detail config
    @RequestMapping(path="/detailConfig",method = GET)
    public void detailConfig(@RequestParam String topic) throws ExecutionException, InterruptedException {
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, "my-admin-topic");
        DescribeConfigsResult describeConfigsResult = adminClient.describeConfigs(Arrays.asList(configResource));
        Map<ConfigResource, Config> configResourceConfigMap = describeConfigsResult.all().get();
        Set<Map.Entry<ConfigResource, Config>> entries = configResourceConfigMap.entrySet();
        for (Map.Entry<ConfigResource, Config> entry : entries) {
            log.info("configResource:{} value:{}", entry.getKey(), entry.getValue());
        }
    }

    //update topic config
    @RequestMapping(path="/update",method = GET)
    public void update(@RequestParam String topic) throws ExecutionException, InterruptedException {
        // 指定ConfigResource的类型及名称
        ConfigResource configResource = new ConfigResource(ConfigResource.Type.TOPIC, topic);
        Collection<AlterConfigOp> configs = Collections.singletonList(new AlterConfigOp(new ConfigEntry("preallocate","false"),AlterConfigOp.OpType.SET));

        Map<ConfigResource,Collection<AlterConfigOp>> configMaps = new HashMap<>();
        configMaps.put(configResource,configs);

        AlterConfigsResult result = adminClient.incrementalAlterConfigs(configMaps);
        log.info("{}",result.all().get());
    }

    // add topic partition，只能增加，不能减少或删除topic
    @RequestMapping(path="/createPartitions",method = GET)
    public void createPartitions(@RequestParam String topic) throws ExecutionException, InterruptedException {
        Map<String, NewPartitions> newPartitionsMap = new HashMap<>();
        // 将topic-spring-01的Partition数量调整为2
        newPartitionsMap.put(topic,NewPartitions.increaseTo(2));
        CreatePartitionsResult result = adminClient.createPartitions(newPartitionsMap);
        log.info("{}",result.all().get());
    }
}
