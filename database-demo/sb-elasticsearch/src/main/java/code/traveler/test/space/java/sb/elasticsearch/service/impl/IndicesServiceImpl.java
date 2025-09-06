package code.traveler.test.space.java.sb.elasticsearch.service.impl;

import code.traveler.test.space.java.sb.elasticsearch.service.IndicesService;
import code.traveler.test.space.java.sb.elasticsearch.util.RestHighLevelClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IndicesServiceImpl implements IndicesService {

    private RestHighLevelClient restHighLevelClient = RestHighLevelClientUtil.getRestHighLevelClient("default");

    private ElasticsearchRestTemplate elasticsearchRestTemplate = RestHighLevelClientUtil.getElasticsearchRestTemplate("default");

    public Map<String, List<AliasMetadata>> listIndices() {

        GetIndexRequest getIndexRequest = new GetIndexRequest("382_xxweb-event_0");
        GetIndexResponse getIndexResponse = null;
        try {
            getIndexResponse = restHighLevelClient
                    .indices()
                    .get(getIndexRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("索引查询异常，异常信息：{},异常详情：{}", e.getMessage(), e);
        }
        log.info("getIndexResponse = {}", getIndexResponse);
        return getIndexResponse.getAliases();
    }


    public Map<String, List<AliasMetadata>> listIndicesByTemplate() {
        return null;
    }
}
