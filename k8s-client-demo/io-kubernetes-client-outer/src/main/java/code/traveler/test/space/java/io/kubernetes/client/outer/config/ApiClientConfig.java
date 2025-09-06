package code.traveler.test.space.java.io.kubernetes.client.outer.config;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class ApiClientConfig {

    @Value("${kube.config.path}")
    private String kubeConfigPath;

    @Bean
    public ApiClient apiClient(){
        ApiClient apiClient = null;
        try {
            Resource resource = new ClassPathResource(kubeConfigPath);
            apiClient = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(resource.getFile()))).build();
            OkHttpClient httpClient = apiClient.getHttpClient().newBuilder().readTimeout(0, TimeUnit.SECONDS).build();
            apiClient.setHttpClient(httpClient);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //设置默认apiClient
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(apiClient);
        return apiClient;
    }

    @Bean
    public CoreV1Api coreV1Api(){
        CoreV1Api api = new CoreV1Api();
        return api;
    }

}
