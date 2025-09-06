package code.traveler.test.space.java.io.kubernetes.client.outer.manager;

import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Node;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ObjectApiManager {

    @Autowired
    private CoreV1Api api;

    public V1PodList loadAllPodObject() throws IOException, ApiException {

      V1PodList list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);

      for(V1Pod pod:list.getItems()){
          System.out.println("uid=" + pod.getMetadata().getUid() + ",name=" + pod.getMetadata().getName()+",kind="+pod.getKind());
      }

      return list;
  }

    public V1NodeList loadAllNodeObject() throws IOException, ApiException {

        V1NodeList list = api.listNode(null, null, null, null, null, null, null, null, null);

        for (V1Node node : list.getItems()) {
            System.out.println("uid=" + node
                    .getMetadata()
                    .getUid() + ",name=" + node
                    .getMetadata()
                    .getName() + ",kind=" + node.getKind());
            node
                    .getStatus()
                    .getAddresses()
                    .forEach(addr -> log.info("addressType={},address={}", addr.getType(), addr.getAddress()));
            ;
        }

        return list;
    }

}
