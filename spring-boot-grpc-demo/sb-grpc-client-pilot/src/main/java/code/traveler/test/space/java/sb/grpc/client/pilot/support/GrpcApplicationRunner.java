package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import code.traveler.test.space.java.sb.grpc.client.pilot.sender.PilotGrpcDataSender;

/**
 * @author panda
 * @description 启动与关闭入口
 * @time 2023/7/30 11:44
 */
@Component
public class GrpcApplicationRunner implements ApplicationRunner, DisposableBean {


    @Autowired
    public PilotGrpcDataSender pilotGrpcDataSender;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        pilotGrpcDataSender.start();
    }

    /**
     * @param
     * @return void
     * @description 加载方式：InitializingBean、@PostConstruct、以及制定init-method
     * 销毁方式：DisposableBean、@PreDestroy和destroy-method等方式
     * 执行顺序如下：
     * 1.PostConstruct name=chyang
     * 2.InitializingBean name=chyang
     * 3.initMethod name=chyang
     * 4.PreDestroy name=chyang
     * 5.DisposableBean name=chyang
     * 6.destroyMethod name=chyang
     * @author panda
     * @time 2023/7/30 10:10
     */
    @Override
    public void destroy() throws Exception {
//        pilotGrpcDataSender.shutdown();
        CloseRegistry
                .sortedCloseListeners()
                .forEach(listener -> listener.close());
    }
}
