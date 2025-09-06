package code.traveler.test.space.java.sb.grpc.client.pilot.support;

import org.springframework.boot.ApplicationRunner;

import javax.annotation.PostConstruct;

/**
 *@description 关闭监听
 *@author panda
 *@time 2023/7/30 10:46
 */
public interface CloseListener {

    /**
     * @description 关闭执行
     * @author panda
     * @param
     * @return void
     * @time 2023/7/30 10:46
     */
    void close();

    /**
     * @description  返回关闭顺序，序号小先关闭
     * @author panda
     * @return
     * @time 2023/7/30 10:50
     */
    Integer getCloseOrder();


    /**
     * @description bean构造好以后就注册关闭监听
     * @author panda
     * @return void
     * @time 2023/7/30 11:41
     */
    @PostConstruct
    default void registryCloseListener() {
        CloseRegistry.registry(this);
    }

}
