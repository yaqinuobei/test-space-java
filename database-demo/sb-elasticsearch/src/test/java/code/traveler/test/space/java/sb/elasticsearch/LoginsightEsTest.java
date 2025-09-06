package code.traveler.test.space.java.sb.elasticsearch;

import code.traveler.test.space.java.sb.elasticsearch.util.JsonUtil;
import code.traveler.test.space.java.sb.elasticsearch.util.RestClientUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class LoginsightEsTest {

    @Test
    public void receverIndex() {
        String[] indexs = new String[]{"322_xxweb-event_0", "298_xxweb-js_0", "299_xxweb-res_error_0", "276_xxweb-js_0", "303_xxweb-res_error_0",
                "170_xxweb-event_0", "170_xxweb-perf_0", "264_xxweb-event_0", "99999999_xxagent_4"};

        for (String index : indexs) {
            int projectId = getProjectId(index);
            String indexPrefix = getIndexPrefix(index);
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("description", "");
            reqMap.put("indexAnalyzer", "standard");
            reqMap.put("indexPrefix", indexPrefix);
            reqMap.put("projectId", projectId);
            reqMap.put("retentionColdIndices", 2);
            reqMap.put("retentionDeleteIndices", 3);
            reqMap.put("shards", 1);
            String reqStr = JsonUtil.toJson(reqMap);
            String result = RestClientUtil.sendPost("http://xxxx/insert", reqStr);
            System.out.println("请求结果：" + result);

        }
    }

    private String getIndexPrefix(String index) {

        return index.substring(index.indexOf("_") + 1, index.lastIndexOf("_"));
    }

    private int getProjectId(String index) {
        String[] subArr = index.split("_");
        return Integer.valueOf(subArr[0]);
    }

    public static void main(String[] args) {
        String index = "303_xxweb-res_error_0";
        String indexPrefix = index.substring(index.indexOf("_") + 1, index.lastIndexOf("_"));
        System.out.println(indexPrefix);
    }
}
