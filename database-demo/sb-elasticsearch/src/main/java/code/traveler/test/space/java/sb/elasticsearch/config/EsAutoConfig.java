package code.traveler.test.space.java.sb.elasticsearch.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter;
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext;

@Configuration
@AutoConfigureAfter({ ElasticsearchRestClientAutoConfiguration.class,
        ReactiveElasticsearchRestClientAutoConfiguration.class })
public class EsAutoConfig {

    @Bean
    @ConditionalOnMissingBean(value = ElasticsearchOperations.class, name = "elasticsearchTemplate")
    @ConditionalOnBean(RestHighLevelClient.class)
    ElasticsearchRestTemplate elasticsearchTemplate(@Qualifier("elasticsearchRestHighLevelClient") RestHighLevelClient client, ElasticsearchConverter converter) {
        return new ElasticsearchRestTemplate(client, converter);
    }

    @Bean
    @ConditionalOnMissingBean
    ElasticsearchConverter elasticsearchConverter(SimpleElasticsearchMappingContext mappingContext) {
        return new MappingElasticsearchConverter(mappingContext);
    }

    @Bean
    @ConditionalOnMissingBean
    SimpleElasticsearchMappingContext mappingContext() {
        return new SimpleElasticsearchMappingContext();
    }
}
