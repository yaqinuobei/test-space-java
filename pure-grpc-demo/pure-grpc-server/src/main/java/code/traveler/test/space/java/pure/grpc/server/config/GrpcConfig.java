package code.traveler.test.space.java.pure.grpc.server.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.ServerServiceDefinition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import code.traveler.test.space.java.pure.grpc.lib.service.CatServiceGrpc;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class GrpcConfig {

    @Value("${support.port:9090}")
    private Integer grpcPort;

    @Bean
    public Server grpcServer(@Qualifier("catServiceImpl") CatServiceGrpc.CatServiceImplBase catServiceImpl,
                             @Qualifier("grpcServerInterceptor") ServerInterceptor grpcServerInterceptor) {
        ServerServiceDefinition grpcServerServiceDefinition = ServerInterceptors.intercept(catServiceImpl, grpcServerInterceptor);
        Server grpcServer = ServerBuilder
                .forPort(grpcPort)
                .addService(grpcServerServiceDefinition)
                .permitKeepAliveWithoutCalls(true)
                .executor(Executors.newFixedThreadPool(16))
                .build();
        try {
            grpcServer.start();
            Runtime
                    .getRuntime()
                    .addShutdownHook(new Thread() {
                        @Override
                        public void run() {
                            log.info("Shutting down gRPC server");
                            try {
                                grpcServer
                                        .shutdown()
                                        .awaitTermination(30, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                log.info("grpcServer shutdown exception:{}", e.getMessage());
                            }
                        }
                    });
            // grpcServer.awaitTermination();//当使用awaitTermination时，主线程会处于一种等待的状态，等待线程池中所有的线程都运行完毕后才继续运行
        } catch (IOException e) {
            log.error("grpcServer start exception:{}", e.getMessage());
        }

        return grpcServer;
    }
}
