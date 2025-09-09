package code.traveler.test.space.java.mcp.client.config;

import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionProperties;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeConnectionUtils;
import com.alibaba.cloud.ai.autoconfigure.dashscope.DashScopeVideoProperties;
import com.alibaba.cloud.ai.autoconfigure.dashscope.ResolvedConnectionProperties;
import com.alibaba.cloud.ai.dashscope.api.DashScopeVideoApi;
import com.alibaba.cloud.ai.dashscope.video.DashScopeVideoModel;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClient;

@Configuration
public class DashScopeConfig {

    @Bean
    @ConditionalOnMissingBean
    public DashScopeVideoModel dashScopeVideoModel(DashScopeVideoApi dashScopeVideoApi,
                                                   DashScopeVideoProperties videoProperties,
                                                   RetryTemplate retryTemplate) {
        return DashScopeVideoModel.builder()
                                  .videoApi(dashScopeVideoApi)
                                  .defaultOptions(videoProperties.getOptions())
                                  .retryTemplate(retryTemplate)
                                  .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public DashScopeVideoApi dashScopeVideoApi(DashScopeConnectionProperties commonProperties,
                                               DashScopeVideoProperties videoProperties,
                                               ObjectProvider<RestClient.Builder> restClientBuilderProvider,
                                               RetryTemplate retryTemplate, ResponseErrorHandler responseErrorHandler) {
        ResolvedConnectionProperties resolved = DashScopeConnectionUtils.resolveConnectionProperties(commonProperties
                , videoProperties, "image");
        DashScopeVideoApi dashScopeVideoApi = DashScopeVideoApi.builder()
                                                               .apiKey(resolved.apiKey())
                                                               .baseUrl(resolved.baseUrl())
                                                               .restClientBuilder((RestClient.Builder) restClientBuilderProvider.getIfAvailable())
                                                               .responseErrorHandler(responseErrorHandler)
                                                               .build();
        return dashScopeVideoApi;
    }
}
