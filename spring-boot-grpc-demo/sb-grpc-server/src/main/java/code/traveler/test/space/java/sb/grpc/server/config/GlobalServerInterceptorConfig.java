package code.traveler.test.space.java.sb.grpc.server.config;

import code.traveler.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import io.grpc.ServerInterceptor;
import org.springframework.context.annotation.Configuration;

import code.traveler.test.space.java.sb.grpc.server.interceptor.LogGrpcInterceptor;

@Configuration(proxyBeanMethods = false)
public class GlobalServerInterceptorConfig {

    @GrpcGlobalServerInterceptor
    ServerInterceptor logServerInterceptor() {
        return new LogGrpcInterceptor();
    }
}
