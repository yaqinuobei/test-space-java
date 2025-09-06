package code.traveler.test.space.java.agentmain.app;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgentmainApp {

    /**
     * 演示热加载
     * 程序启动之后，更新代码
     *
     * @param args
     */
    public static void main(String[] args) {
            for(int i=0;i<1000;i++){
                log.info("i={}",i);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

//            String a ="";
//            List<String> ss = new ArrayList<String >(){
//                add("");
//
//        }
    }
}
