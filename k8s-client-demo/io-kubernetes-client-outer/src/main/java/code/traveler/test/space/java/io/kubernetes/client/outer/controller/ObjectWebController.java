package code.traveler.test.space.java.io.kubernetes.client.outer.controller;

import code.traveler.test.space.java.io.kubernetes.client.outer.manager.InformerManager;
import code.traveler.test.space.java.io.kubernetes.client.outer.manager.ObjectApiManager;
import code.traveler.test.space.java.io.kubernetes.client.outer.manager.ObjectWatchManager;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.models.V1NodeList;
import io.kubernetes.client.openapi.models.V1PodList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1")
public class ObjectWebController {

    @Autowired
    private ObjectApiManager objectApiManager;

    @Autowired
    private ObjectWatchManager objectWatchManager;

    @Autowired
    private InformerManager informerManager;

    @GetMapping("/pods")
    public V1PodList getPods() {
        try {
            return objectApiManager.loadAllPodObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/watch")
    public String watch() {
        try {
            objectWatchManager.watch();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("/index")
    public String index() {
        try {
            informerManager.testInformer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }

    @GetMapping("/node")
    public V1NodeList listNodes() {
        try {
            return objectApiManager.loadAllNodeObject();
        } catch (IOException | ApiException e) {
            e.printStackTrace();
        }
        return null;
    }

}
