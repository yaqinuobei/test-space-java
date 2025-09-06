package code.traveler.test.space.java.sb.victoriametrics.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import code.traveler.test.space.java.sb.victoriametrics.dto.MetricsEntity;
import code.traveler.test.space.java.sb.victoriametrics.util.VMUtil;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import protobuf.Remote;
import protobuf.Types;

@Slf4j
public class VMTest1 {

    public static void main(String[] args) {
        //String url = "http://10.253.81.102:8989/vm_insert/insert/2/prometheus/api/v1/write";
        String url = "http://10.253.81.102:8480/insert/2/prometheus/api/v1/write";
        String username = "admin";
        String password = "Cater@99.44";

        Map<String,Object> sl = new HashMap<>();
        sl.put("project","apm");
        sl.put("service","user");
        MetricsEntity me = new MetricsEntity();
        me.setName("tpp");
        me.setValue(40.34);
        me.setTimestamp(System.currentTimeMillis());
        me.setLabels(sl);
        List<MetricsEntity> ms = new ArrayList<>();
        ms.add(me);



        //构建请求json数据
        String jsonData = "{\"metric\":\"point\",\"value\":40.34,\"tags\":{\"t1\":\"v1\",\"t2\":\"v2\"}}";

        HttpResponse response = null;
        try {
            response = HttpRequest
                    .post(url)
                    .basicAuth(username, password)
//                    .body(VMUtil.deSerialise(buildApmMetrics()))
                    .body(VMUtil.deSerialise(ms))
                    .header("Content-Encoding", "snappy")
                    .header("Content-Type","application/x-protobuf")
                    .header("X-Prometheus-Remote-Write-Version","0.1.0")
                    .execute();
        } catch (IOException e) {
            log.error("post metrics to vm error:{}",e);
        }
        int statusCode = response.getStatus();
        String responseBody = response.body();

        System.out.println("Response Code:"+statusCode);
        System.out.println("Response Body:"+responseBody);

    }

    private static List<MetricsEntity> buildApmMetrics(){
        List<MetricsEntity> mes = new ArrayList<>();
        Map<String,Object> labels = new HashMap<>();
        labels.put("api","/apps/v1/clusters");
        labels.put("hostname","Mysql-Service@");
        labels.put("service","Mysql-Service");
        labels.put("topic","305");
        MetricsEntity me  = new MetricsEntity();
        me.setLabels(labels);
        me.setName("api_max");
        me.setValue(278);
        me.setTimestamp(System.currentTimeMillis());
        mes.add(me);
        return mes;
    }



}

