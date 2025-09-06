package code.traveler.test.space.java.sb.elasticsearch.config;/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * {@link EnableAutoConfiguration Auto-configuration} for Elasticsearch REST clients.
 *
 * @author Brian Clozel
 * @author Stephane Nicoll
 * @since 2.1.0
 */
//@Configuration(proxyBeanMethods = false)
//@ConditionalOnClass(RestHighLevelClient.class)
//@ConditionalOnMissingBean(RestClient.class)
//@EnableConfigurationProperties(ElasticsearchRestClientsProperties.class)
public class ElasticsearchRestClientAutoConfiguration {

//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnMissingBean(RestClientBuilder.class)
//    static class RestClientBuilderConfiguration {
//
//        @Bean
//        RestClientBuilderCustomizer defaultRestClientBuilderCustomizer(ElasticsearchRestClientProperties properties) {
//            return new DefaultRestClientBuilderCustomizer(properties);
//        }
//
//        @Bean
//        Map<String,RestClientBuilder> elasticsearchRestClientBuilderMap(ElasticsearchRestClientsProperties clientsProperties,
//                                                         ObjectProvider<RestClientBuilderCustomizer> builderCustomizers) {
//            Map<String,RestClientBuilder> restClientBuilderMap = new HashMap<>();
//            clientsProperties.getRest().forEach((name,properties)->{
//
//                HttpHost[] hosts = properties.getUris().stream().map(this::createHttpHost).toArray(HttpHost[]::new);
//                RestClientBuilder builder = RestClient.builder(hosts);
//                builder.setHttpClientConfigCallback((httpClientBuilder) -> {
//                    builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(httpClientBuilder));
//                    return httpClientBuilder;
//                });
//                builder.setRequestConfigCallback((requestConfigBuilder) -> {
//                    builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(requestConfigBuilder));
//                    return requestConfigBuilder;
//                });
//                builderCustomizers.orderedStream().forEach((customizer) -> customizer.customize(builder));
//
//                restClientBuilderMap.putRestHighLevelClient(name,builder);
//            });
//            return restClientBuilderMap;
//        }
//
//        private HttpHost createHttpHost(String uri) {
//            try {
//                return createHttpHost(URI.create(uri));
//            }
//            catch (IllegalArgumentException ex) {
//                return HttpHost.create(uri);
//            }
//        }
//
//        private HttpHost createHttpHost(URI uri) {
//            if (!StringUtils.hasLength(uri.getUserInfo())) {
//                return HttpHost.create(uri.toString());
//            }
//            try {
//                return HttpHost.create(new URI(uri.getScheme(), null, uri.getHost(), uri.getPort(), uri.getPath(),
//                        uri.getQuery(), uri.getFragment()).toString());
//            }
//            catch (URISyntaxException ex) {
//                throw new IllegalStateException(ex);
//            }
//        }
//
//    }
//
//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnMissingBean(RestHighLevelClient.class)
//    static class RestHighLevelClientConfiguration {
//
//        @Bean
//        Map<String,RestHighLevelClient> elasticsearchRestHighLevelClient(Map<String,RestClientBuilder> elasticsearchRestClientBuilderMap) {
//            Map<String,RestHighLevelClient> restHighLevelClientMap = new HashMap<>();
//            elasticsearchRestClientBuilderMap.forEach((name,restClientBuilder)->restHighLevelClientMap.putRestHighLevelClient(name,new RestHighLevelClient
//            (restClientBuilder)));
//
//            return restHighLevelClientMap;
//        }
//
//    }
//
//    static class DefaultRestClientBuilderCustomizer implements RestClientBuilderCustomizer {
//
//        private static final PropertyMapper map = PropertyMapper.get();
//
//        private final ElasticsearchRestClientProperties properties;
//
//        DefaultRestClientBuilderCustomizer(ElasticsearchRestClientProperties properties) {
//            this.properties = properties;
//        }
//
//        @Override
//        public void customize(RestClientBuilder builder) {
//        }
//
//        @Override
//        public void customize(HttpAsyncClientBuilder builder) {
//            builder.setDefaultCredentialsProvider(new PropertiesCredentialsProvider(this.properties));
//        }
//
//        @Override
//        public void customize(RequestConfig.Builder builder) {
//            map.from(this.properties::getConnectionTimeout).whenNonNull().asInt(Duration::toMillis)
//                    .to(builder::setConnectTimeout);
//            map.from(this.properties::getReadTimeout).whenNonNull().asInt(Duration::toMillis)
//                    .to(builder::setSocketTimeout);
//        }
//
//    }
//
//    private static class PropertiesCredentialsProvider extends BasicCredentialsProvider {
//
//        PropertiesCredentialsProvider(ElasticsearchRestClientProperties properties) {
//            if (StringUtils.hasText(properties.getUsername())) {
//                Credentials credentials = new UsernamePasswordCredentials(properties.getUsername(),
//                        properties.getPassword());
//                setCredentials(AuthScope.ANY, credentials);
//            }
//            properties.getUris().stream().map(this::toUri).filter(this::hasUserInfo)
//                    .forEach(this::addUserInfoCredentials);
//        }
//
//        private URI toUri(String uri) {
//            try {
//                return URI.create(uri);
//            }
//            catch (IllegalArgumentException ex) {
//                return null;
//            }
//        }
//
//        private boolean hasUserInfo(URI uri) {
//            return uri != null && StringUtils.hasLength(uri.getUserInfo());
//        }
//
//        private void addUserInfoCredentials(URI uri) {
//            AuthScope authScope = new AuthScope(uri.getHost(), uri.getPort());
//            Credentials credentials = createUserInfoCredentials(uri.getUserInfo());
//            setCredentials(authScope, credentials);
//        }
//
//        private Credentials createUserInfoCredentials(String userInfo) {
//            int delimiter = userInfo.indexOf(":");
//            if (delimiter == -1) {
//                return new UsernamePasswordCredentials(userInfo, null);
//            }
//            String username = userInfo.substring(0, delimiter);
//            String password = userInfo.substring(delimiter + 1);
//            return new UsernamePasswordCredentials(username, password);
//        }
//
//    }

}
