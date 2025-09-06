package code.traveler.test.space.java.sb.elasticsearch.util;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RestHighLevelClientUtil {

    private static Map<String, RestHighLevelClient> restHighLevelClientMap = new ConcurrentHashMap<>();

    private static Map<String, ElasticsearchRestTemplate> elasticsearchRestTemplateMap = new ConcurrentHashMap<>();

    public static void putRestHighLevelClient(String name, RestHighLevelClient restHighLevelClient){

        restHighLevelClientMap.put(name,restHighLevelClient);
    }

    public static RestHighLevelClient getRestHighLevelClient(String name){

        return restHighLevelClientMap.get(name);
    }

    public static void putElasticsearchRestTemplate(String name, ElasticsearchRestTemplate elasticsearchRestTemplate){

        elasticsearchRestTemplateMap.put(name,elasticsearchRestTemplate);
    }

    public static ElasticsearchRestTemplate getElasticsearchRestTemplate(String name){

        return elasticsearchRestTemplateMap.get(name);
    }
}
