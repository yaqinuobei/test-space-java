package code.traveler.test.space.java.sb.elasticsearch.manage;

import code.traveler.test.space.java.sb.elasticsearch.service.IndicesService;
import code.traveler.test.space.java.sb.elasticsearch.util.RestHighLevelClientUtil;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EsShardManage {

    private RestHighLevelClient restHighLevelClient = RestHighLevelClientUtil.getRestHighLevelClient("source");

    @Autowired
    private IndicesService indicesService;

    public void moveShard(String srcNode, String tarNode) {

    }
}
