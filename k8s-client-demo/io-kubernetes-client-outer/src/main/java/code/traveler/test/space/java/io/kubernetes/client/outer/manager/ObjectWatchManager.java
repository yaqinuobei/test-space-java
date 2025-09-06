package code.traveler.test.space.java.io.kubernetes.client.outer.manager;

import com.google.common.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Namespace;
import io.kubernetes.client.openapi.models.V1NamespaceList;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import io.kubernetes.client.util.Watch;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

@Component
public class ObjectWatchManager {

    @Autowired
    private CoreV1Api api;

    @Autowired
    private ApiClient apiClient;

    public void watch() throws IOException, ApiException {

        Call call = api.listNamespaceCall(null, null, null, null, null, 5, null, null, Boolean.FALSE, null);
        Response response = call.execute();
        Type localVarReturnType = (new com.google.gson.reflect.TypeToken<V1NamespaceList>() {
        }).getType();
        V1NamespaceList namelist = apiClient.getJSON().deserialize(response.body().string(),localVarReturnType);

        Watch<V1Namespace> watch = Watch.createWatch(apiClient,api.listNamespaceCall(
                null, null, null, null, null, 5, null, null, Boolean.TRUE, null), new TypeToken<Watch.Response<V1Namespace>>() {}.getType());

        try {
            for (Watch.Response<V1Namespace> item : watch) {
                System.out.printf("%s : kind=%s,uid=%s,name=%s,meta=%s%n", item.type,item.object.getKind(), item.object.getMetadata().getUid(),item.object.getMetadata().getName(),new GsonBuilder().setPrettyPrinting().create().toJson(item.object.getMetadata()));
                System.out.printf("clusterName=%s,ownerReferences=%s%n",item.object.getMetadata().getClusterName(),item.object.getMetadata().getOwnerReferences());
            }
        } finally {
            watch.close();
        }
    }
}
