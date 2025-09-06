package code.traveler.test.space.java.sb.elasticsearch;

import cn.hutool.core.util.StrUtil;
import code.traveler.test.space.java.sb.elasticsearch.util.JsonUtil;
import code.traveler.test.space.java.sb.elasticsearch.util.RestClientUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.explain.ExplainRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EsTest {

    @Test
    public void testIndices() {

        RestHighLevelClient client = this.buildClient();

        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        RequestOptions requestOptions = builder.build();
        requestOptions.getRequestConfig();
//      client.indices()

        ExplainRequest explainRequest = new ExplainRequest();
//      explainRequest.index()
//      client.explain()
    }

    public RestHighLevelClient buildClient() {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("127.0.0.1", 9201, "http")));
        return client;
    }

    @Test
    public void testIndices2() {
        String result = RestClientUtil.doGet("http://127.0.0.1:9201/_cat/shards?h=index,shard,prirep,state,unassigned.reason");

        String content = "{\"index\":\"162-xxdependencies-20230723\",\"shard\":0,\"primary\":false}";
        String allocationExplainResult = RestClientUtil.sendPost("http://127.0.0.1:9201/_cluster/allocation/explain", content);

        System.out.println(allocationExplainResult);
    }

    @Test
    public void cleanIndices() {
        List<String> indices = new ArrayList<>();
        try {
            indices = fetchIndices("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        RequestConfig requestConfig = RequestConfig.DEFAULT;
        RequestOptions requestOptions = builder
                .setRequestConfig(requestConfig)
                .build();
        RestHighLevelClient client = this.buildClient();

        indices.forEach(index -> {

            if (index.contains("xxtrace")) {
                this.removeIndexWithoutAlias(index, client, requestOptions);
            }
            if (isRemoveIndex(index)) {
                this.removeIndex(index, client, requestOptions);
            }
        });

    }

    public boolean isRemoveIndex(String index) {
        //isRemove xxservice
        if (index.contains("xxservice") && !index.endsWith("20230724") && !index.contains("xxservice_api")) {

            return true;
        }
        if (index.contains("xxexception") && !index.endsWith("20230724")) {

            return true;
        }
        if (index.contains("xxapi") && !index.endsWith("20230724")) {

            return true;
        }
        if (index.contains("xxdependencies") && !index.endsWith("20230724")) {

            return true;
        }
        return false;
    }

    public void removeIndexWithoutAlias(String index, RestHighLevelClient client, RequestOptions requestOptions) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(index);
        final IndicesClient indicesClient = client.indices();
        try {
            GetIndexResponse getIndexResponse = indicesClient.get(getIndexRequest, requestOptions);
            Map<String, List<AliasMetadata>> aliasMaps = getIndexResponse.getAliases();
            if (aliasMaps
                    .get(index)
                    .isEmpty()) {
                System.out.println("未包含别名的索引：" + index);
                //清理该索引
                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
                AcknowledgedResponse acknowledgedResponse = indicesClient.delete(deleteIndexRequest, requestOptions);
                System.out.println("清楚索引：" + index + "结果：" + acknowledgedResponse.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeIndex(String index, RestHighLevelClient client, RequestOptions requestOptions) {
        final IndicesClient indicesClient = client.indices();
        try {
            //清理该索引
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
            AcknowledgedResponse acknowledgedResponse = indicesClient.delete(deleteIndexRequest, requestOptions);
            System.out.println("清楚索引：" + index + "结果：" + acknowledgedResponse.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    @Test
    public void removeRedIndices() {
        List<String> indices = fetchIndices("red");
        System.out.println(indices);
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        RequestConfig requestConfig = RequestConfig.DEFAULT;
        RequestOptions requestOptions = builder
                .setRequestConfig(requestConfig)
                .build();
        RestHighLevelClient client = this.buildClient();

        indices.forEach(index -> {

            if (index.endsWith("xxservice_api")) {
                this.removeIndex(index, client, requestOptions);
            }
        });

    }

    public List<String> fetchIndices(String healthStatus) throws IOException {
        RestHighLevelClient client = this.buildClient();
        List<String> indices = null;

        RestClient restClient = client.getLowLevelClient();
        Response response = null;
        try {
            Request request = new Request("GET", "/_cat/indices?v");
            response = restClient.performRequest(request);
        } catch (IOException e) {
            System.out.println("异常");
        }

        InputStream inputStream = null;
        if (response != null) {
            try {
                inputStream = response
                        .getEntity()
                        .getContent();
            } catch (IOException e) {
                System.out.println("异常");
            }
        }

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            indices = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Get tokens with no whitespace
                String[] tokens = line.split("\\s+");
                if (StrUtil.isNotBlank(healthStatus)) {
                    String currentStatus = tokens[0];
                    if (healthStatus.equals(currentStatus)) {
                        for (String token : tokens) {
                            // TODO - make the startsWith() token configurable
//                    if (token.startsWith(SOME_TOKEN)) {
                            if (token.contains("ops")) {
                                System.out.println("Found elasticsearch index " + token);
                                indices.add(token);
                                break;
                            }
                        }
                    }
                } else {
                    for (String token : tokens) {
                        // TODO - make the startsWith() token configurable
//                    if (token.startsWith(SOME_TOKEN)) {
                        if (token.contains("ops")) {
                            System.out.println("Found elasticsearch index " + token);
                            indices.add(token);
                            break;
                        }
                    }
                }
            }
        }

        // Only update if we got data back from our REST call
        return indices;
    }

    @Test
    public void getRedShard() {

        List<RedShard> redShards = new ArrayList<>();
        String shardQueryResult = RestClientUtil.doGet("http://127.0.0.1:9201/_cluster/health?wait_for_status=red&level=shards");
        JsonNode shardQueryResultJsonNode = JsonUtil.toJsonNode(shardQueryResult);
        JsonNode indicesJsonNode = shardQueryResultJsonNode.get("indices");
        Iterator<Map.Entry<String, JsonNode>> indicesIteratorJsonNode = ((ObjectNode) indicesJsonNode).fields();
        while (indicesIteratorJsonNode.hasNext()) {
            Map.Entry<String, JsonNode> indicesMapEntryJsonNode = indicesIteratorJsonNode.next();
            String indicesMapKey = indicesMapEntryJsonNode.getKey();
            JsonNode indicesMapValue = indicesMapEntryJsonNode.getValue();
            String indicesStatus = indicesMapValue
                    .get("status")
                    .textValue();
            if (!indicesStatus.equals("red")) {
                continue;
            }
            JsonNode shardsJsonNode = indicesMapValue.get("shards");
            Iterator<Map.Entry<String, JsonNode>> shardsIteratorJsonNode = ((ObjectNode) shardsJsonNode).fields();
            while (shardsIteratorJsonNode.hasNext()) {
                Map.Entry<String, JsonNode> shardsMapEntryJsonNode = shardsIteratorJsonNode.next();
                String shardId = shardsMapEntryJsonNode.getKey();
                JsonNode shardMapValue = shardsMapEntryJsonNode.getValue();
                String shardStatus = shardMapValue
                        .get("status")
                        .textValue();
                boolean primary_active = shardMapValue
                        .get("status")
                        .booleanValue();
                if (shardStatus.equals("red") && Boolean.FALSE.equals(primary_active)) {
                    RedShard redShard = new RedShard();
                    redShard.setIndex(indicesMapKey);
                    redShard.setPrimary(primary_active);
                    redShard.setShard(Integer.valueOf(shardId));
                    redShards.add(redShard);
                }
            }
        }

        //move
        List<Map<String, List<Map<String, Map<String, Object>>>>> allocateStalePrimaryRequest = new ArrayList<>();
        redShards.forEach(redShard -> {
            Map<String, Object> moveCommandMap = new HashMap<>();
            moveCommandMap.put("index", redShard.getIndex());
            moveCommandMap.put("shard", redShard.getShard());
            moveCommandMap.put("from_node", "");
            moveCommandMap.put("to_node", "");

            Map<String, Object> allocateStalePrimaryValueMap = new HashMap<>();
            allocateStalePrimaryValueMap.put("index", redShard.getIndex());
            allocateStalePrimaryValueMap.put("shard", redShard.getShard());
            allocateStalePrimaryValueMap.put("node", "node_physical_03_cold");
            allocateStalePrimaryValueMap.put("accept_data_loss", true);
            Map<String, Map<String, Object>> allocateStalePrimaryMap = new HashMap<>();
            allocateStalePrimaryMap.put("allocate_stale_primary", allocateStalePrimaryValueMap);
            List<Map<String, Map<String, Object>>> commands = new ArrayList<>();
            commands.add(allocateStalePrimaryMap);
            Map<String, List<Map<String, Map<String, Object>>>> commandsMap = new HashMap<>();
            commandsMap.put("commands", commands);
            allocateStalePrimaryRequest.add(commandsMap);
        });

        allocateStalePrimaryRequest.forEach(req -> {
            String reqStr = JsonUtil.toJson(req);
            System.out.println("request:" + reqStr);
            String resStr = RestClientUtil.sendPost("http://127.0.0.1:9201/_cluster/reroute", reqStr);
            System.out.println("response:" + resStr);
        });
    }

    @Data
    private class RedShard {

        private String nodeName;

        private String index;

        private int shard;

        private boolean primary;

    }

}
