package code.traveler.test.space.java.sb.grpc.client.pilot.support;

/**
 * @description
 * @author tiger
 * @time 2022/6/30 3:45 PM
 */
public interface THandler {

    /**
     * @description
     * @author tiger
     * @param
     * @return void
     * @time 2022/6/30 3:45 PM
     */
    void handle();

    void handle(Object arg1);

    void handle(Object arg1, Object arg2);

    void handle(Object arg1, Object arg2, Object arg3);

    void handle(Object arg1, Object arg2, Object arg3, Object arg4);

    void handle(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5);
}
