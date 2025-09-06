package code.traveler.test.space.java.sb.grpc.client.config;

import code.traveler.grpc.client.interceptor.GrpcGlobalClientInterceptor;
import io.grpc.ClientInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import code.traveler.test.space.java.sb.grpc.client.interceptor.LogGrpcInterceptor;

@Order(Ordered.LOWEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
public class GlobalClientInterceptorConfig {

    @GrpcGlobalClientInterceptor
    ClientInterceptor logClientInterceptor() {
        return new LogGrpcInterceptor();
    }
}
