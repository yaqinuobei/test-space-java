package code.traveler.test.space.java.sb.elasticsearch.config;

import code.traveler.test.space.java.sb.elasticsearch.util.RestHighLevelClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.elasticsearch.RestClientBuilderCustomizer;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * RestService根据配置自动注入Bean
 *
 * @author: xxx
 * @date: 2020-09-11 9:28
 * ApplicationContextAware 是为了重新获取bean而实现，不用可以不添加，后续会说明
 */
@Component
@Slf4j
public class ElasticsearchRestClientBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, EnvironmentAware,
        ApplicationContextAware {


    private Environment environment;

    private ApplicationContext applicationContext;

    //配置文件前缀
    private static final String APM_ELASTICSEARCH_URL_PREFIX = "apm.elasticsearch";

    private static final String REST_HIGH_LEVEL_CLIENT_SUFFIX = "RestHignLevelClient";

    private static final String DEFAULT_REST_HIGH_LEVEL_CLIENT_SUFFIX = "restHignLevelClient";

    private static final String ELASTICSEARCH_REST_TEMPLATE_SUFFIX = "ElasticsearchRestTemplate";

    private static final String DEFAULT_ELASTICSEARCH_REST_TEMPLATE_SUFFIX = "elasticsearchRestTemplate";


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        try {
            //获取配置文件配置，并绑定
            BindResult<ElasticsearchRestClientsProperties> apmElasticsearchRestClientBindResult = Binder
                    .get(environment)
                    .bind(APM_ELASTICSEARCH_URL_PREFIX, ElasticsearchRestClientsProperties.class);
            ElasticsearchRestClientsProperties elasticsearchRestClientsProperties = apmElasticsearchRestClientBindResult.get();
            Map<String, ElasticsearchRestClientProperties> elasticsearchRestClientPropertiesMap = elasticsearchRestClientsProperties.getRest();
            Map<String, RestClientBuilderCustomizer> restClientBuilderCustomizerMap = new HashMap<>();
            elasticsearchRestClientPropertiesMap.forEach((name, properties) -> {
                DefaultRestClientBuilderCustomizer builderCustomizers = new DefaultRestClientBuilderCustomizer(properties);
                restClientBuilderCustomizerMap.put(name, builderCustomizers);

                HttpHost[] hosts = properties
                        .getUris()
                        .stream()
                        .map(this::createHttpHost)
                        .toArray(HttpHost[]::new);
                RestClientBuilder restClientBuilder = RestClient.builder(hosts);
                restClientBuilder.setHttpClientConfigCallback((httpClientBuilder) -> {
                    builderCustomizers.customize(httpClientBuilder);
                    return httpClientBuilder;
                });
                restClientBuilder.setRequestConfigCallback((requestConfigBuilder) -> {
                    builderCustomizers.customize(requestConfigBuilder);
                    return requestConfigBuilder;
                });
                builderCustomizers.customize(restClientBuilder);
                // RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

                this.buildBean(beanDefinitionRegistry, name, restClientBuilder);
            });
        } catch (Exception e) {
            log.error("Init restHighLevelClient bean failed!,异常信息：{},异常详情：{}", e.getMessage(), e);
            throw new BeanCreationException("初始化restHighLevelClient失败!");
        }
    }

    private void buildBean(BeanDefinitionRegistry beanDefinitionRegistry, String name, RestClientBuilder restClientBuilder) {
        //restHighLevelClientBean
        BeanDefinitionBuilder restHighLevelClientBean = BeanDefinitionBuilder.rootBeanDefinition(RestHighLevelClient.class);
        //向bean中构造方法传入参数
        restHighLevelClientBean.addConstructorArgValue(restClientBuilder);
        //向bean中baseUrl参数中注入配置文件的配置内容
        //coreRestBean.addPropertyValue("baseUrl", coreUrl);
        if (name.equals("default")) {
            //注册beandefinition，并定义好bean的名称
            restHighLevelClientBean.setPrimary(true);
            beanDefinitionRegistry.registerBeanDefinition(DEFAULT_REST_HIGH_LEVEL_CLIENT_SUFFIX, restHighLevelClientBean.getBeanDefinition());
            //获取到环境中已注册的bean并放入map中
            RestHighLevelClientUtil.putRestHighLevelClient(name, (RestHighLevelClient) applicationContext.getBean(DEFAULT_REST_HIGH_LEVEL_CLIENT_SUFFIX));
        } else {
            //注册beandefinition，并定义好bean的名称
            beanDefinitionRegistry.registerBeanDefinition(name + REST_HIGH_LEVEL_CLIENT_SUFFIX, restHighLevelClientBean.getBeanDefinition());
            //获取到环境中已注册的bean并放入map中
            RestHighLevelClientUtil.putRestHighLevelClient(name, (RestHighLevelClient) applicationContext.getBean(name + REST_HIGH_LEVEL_CLIENT_SUFFIX));
        }


        //restHighLevelClientBean
        BeanDefinitionBuilder elasticsearchRestTemplateBean = BeanDefinitionBuilder.rootBeanDefinition(ElasticsearchRestTemplate.class);
        //向bean中baseUrl参数中注入配置文件的配置内容
        //coreRestBean.addPropertyValue("baseUrl", coreUrl);
        if (name.equals("default")) {
            //向bean中构造方法传入参数
            elasticsearchRestTemplateBean.addConstructorArgValue((RestHighLevelClient) applicationContext.getBean(DEFAULT_REST_HIGH_LEVEL_CLIENT_SUFFIX));
            elasticsearchRestTemplateBean.addConstructorArgValue(applicationContext.getBean(ElasticsearchConverter.class));
            //注册beandefinition，并定义好bean的名称
            beanDefinitionRegistry.registerBeanDefinition(DEFAULT_ELASTICSEARCH_REST_TEMPLATE_SUFFIX, elasticsearchRestTemplateBean.getBeanDefinition());
            //获取到环境中已注册的bean并放入map中
            RestHighLevelClientUtil.putElasticsearchRestTemplate(name,
                    (ElasticsearchRestTemplate) applicationContext.getBean(DEFAULT_ELASTICSEARCH_REST_TEMPLATE_SUFFIX));
        } else {
            //向bean中构造方法传入参数
            elasticsearchRestTemplateBean.addConstructorArgValue((RestHighLevelClient) applicationContext.getBean(name + REST_HIGH_LEVEL_CLIENT_SUFFIX));
            elasticsearchRestTemplateBean.addConstructorArgValue(applicationContext.getBean(ElasticsearchConverter.class));
            //注册beandefinition，并定义好bean的名称
            beanDefinitionRegistry.registerBeanDefinition(name + ELASTICSEARCH_REST_TEMPLATE_SUFFIX, elasticsearchRestTemplateBean.getBeanDefinition());
            //获取到环境中已注册的bean并放入map中
            RestHighLevelClientUtil.putElasticsearchRestTemplate(name,
                    (ElasticsearchRestTemplate) applicationContext.getBean(name + ELASTICSEARCH_REST_TEMPLATE_SUFFIX));
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setEnvironment(final Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private HttpHost createHttpHost(String uri) {
        try {
            return createHttpHost(URI.create(uri));
        } catch (IllegalArgumentException ex) {
            return HttpHost.create(uri);
        }
    }

    private HttpHost createHttpHost(URI uri) {
        if (!StringUtils.hasLength(uri.getUserInfo())) {
            return HttpHost.create(uri.toString());
        }
        try {
            return HttpHost.create(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(), uri.getQuery(), uri.getFragment()).toString());
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(ex);
        }
    }

    static class DefaultRestClientBuilderCustomizer implements RestClientBuilderCustomizer {

        private static final PropertyMapper map = PropertyMapper.get();

        private final ElasticsearchRestClientProperties properties;

        DefaultRestClientBuilderCustomizer(ElasticsearchRestClientProperties properties) {
            this.properties = properties;
        }

        @Override
        public void customize(RestClientBuilder builder) {
        }

        @Override
        public void customize(HttpAsyncClientBuilder builder) {
            builder.setDefaultCredentialsProvider(new PropertiesCredentialsProvider(this.properties));
        }

        @Override
        public void customize(RequestConfig.Builder builder) {
            map
                    .from(this.properties::getConnectionTimeout)
                    .whenNonNull()
                    .asInt(Duration::toMillis)
                    .to(builder::setConnectTimeout);
            map
                    .from(this.properties::getReadTimeout)
                    .whenNonNull()
                    .asInt(Duration::toMillis)
                    .to(builder::setSocketTimeout);
        }
    }

    private static class PropertiesCredentialsProvider extends BasicCredentialsProvider {

        PropertiesCredentialsProvider(ElasticsearchRestClientProperties properties) {
            if (StringUtils.hasText(properties.getUsername())) {
                Credentials credentials = new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword());
                setCredentials(AuthScope.ANY, credentials);
            }
            properties
                    .getUris()
                    .stream()
                    .map(this::toUri)
                    .filter(this::hasUserInfo)
                    .forEach(this::addUserInfoCredentials);
        }

        private URI toUri(String uri) {
            try {
                return URI.create(uri);
            } catch (IllegalArgumentException ex) {
                return null;
            }
        }

        private boolean hasUserInfo(URI uri) {
            return uri != null && StringUtils.hasLength(uri.getUserInfo());
        }

        private void addUserInfoCredentials(URI uri) {
            AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
            Credentials credentials = createUserInfoCredentials(uri.getUserInfo());
            setCredentials(authScope, credentials);
        }

        private Credentials createUserInfoCredentials(String userInfo) {
            int delimiter = userInfo.indexOf(":");
            if (delimiter == -1) {
                return new UsernamePasswordCredentials(userInfo, null);
            }
            String username = userInfo.substring(0, delimiter);
            String password = userInfo.substring(delimiter + 1);
            return new UsernamePasswordCredentials(username, password);
        }
    }
}