package code.traveler.test.space.java.premain.app;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PremainApp {

    /**
     * 启动程序更新代码
     * 启动参数 -javaagent:/option/agent/agent.core-1.0-SNAPSHOT.jar
     *
     * @param args
     */
    public static void main(String[] args) {
        log.info("agentClient is run ");
    }

}
