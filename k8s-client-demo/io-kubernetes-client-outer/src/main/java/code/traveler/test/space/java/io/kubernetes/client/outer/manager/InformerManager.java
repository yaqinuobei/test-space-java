package code.traveler.test.space.java.io.kubernetes.client.outer.manager;

import com.google.common.reflect.TypeToken;
import io.kubernetes.client.informer.ListerWatcher;
import io.kubernetes.client.informer.ResourceEventHandler;
import io.kubernetes.client.informer.SharedIndexInformer;
import io.kubernetes.client.informer.SharedInformerFactory;
import io.kubernetes.client.informer.cache.Indexer;
import io.kubernetes.client.informer.cache.Lister;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.CallGeneratorParams;
import io.kubernetes.client.util.Watch;
import io.kubernetes.client.util.Watchable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class InformerManager {

    @Autowired
    private CoreV1Api api;

    @Autowired
    private ApiClient apiClient;

    public void testInformer()throws IOException {

        SharedInformerFactory sharedInformerFactory = new SharedInformerFactory(apiClient);
        SharedIndexInformer<V1Pod> sharedIndexInformer = sharedInformerFactory.sharedIndexInformerFor(new ListerWatcher<V1Pod, V1PodList>() {

            @Override
            public V1PodList list(CallGeneratorParams callGeneratorParams) throws ApiException {

                return api.listPodForAllNamespaces(null, null, null, null, null, null, callGeneratorParams.resourceVersion, callGeneratorParams.timeoutSeconds, callGeneratorParams.watch);
            }

            @Override
            public Watchable<V1Pod> watch(CallGeneratorParams callGeneratorParams) throws ApiException {

                return Watch.createWatch(apiClient, api.listNamespaceCall(
                        null, true, null, null, null, 5, callGeneratorParams.resourceVersion, callGeneratorParams.timeoutSeconds, callGeneratorParams.watch, null), new TypeToken<Watch.Response<V1Pod>>() {}.getType());
            }
        },V1Pod.class,60000L);


        sharedIndexInformer.addEventHandler(new ResourceEventHandler<V1Pod>(){

            @Override
            public void onAdd(V1Pod obj) {
                log.info("add,obj={}",obj.toString());
            }

            @Override
            public void onUpdate(V1Pod oldObj, V1Pod newObj) {
                log.info("update,oldObj={},newObj={}",oldObj,newObj);
            }

            @Override
            public void onDelete(V1Pod obj, boolean deletedFinalStateUnknown) {
                log.info("delete,obj={},deletedFinalStateUnknown={}",obj,deletedFinalStateUnknown);
            }
        });
        sharedInformerFactory.startAllRegisteredInformers();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Indexer<V1Pod> v1PodIndexer = sharedIndexInformer.getIndexer();
        Lister<V1Pod> v1PodLister = new Lister(v1PodIndexer);
//        List<V1Pod> v1Pods = v1PodIndexer.list();
        List<V1Pod> v1Pods = v1PodLister.list();
        for(V1Pod v1Pod:v1Pods){
            log.info("v1Pod={}",v1Pod);
        }

    }

}
