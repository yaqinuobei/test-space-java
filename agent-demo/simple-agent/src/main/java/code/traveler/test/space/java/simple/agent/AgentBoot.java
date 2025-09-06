package code.traveler.test.space.java.simple.agent;

import lombok.extern.slf4j.Slf4j;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

@Slf4j
public class AgentBoot {

    /**
     * 该方法在main方法之前运行，与main方法运行在同一个JVM中
     *
     * @param agentArgs
     * @param instrumentation
     */
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("=========premain方法执行1========");
        System.out.println(agentArgs);
    }

    /**
     * 如果不存在 premain(String agentArgs, Instrumentation inst)
     * 则会执行 premain(String agentArgs)
     */
    public static void premain(String agentArgs) {
        System.out.println("=========premain方法执行2========");
        System.out.println(agentArgs);
    }


    public static void agentmain(String agentArgs, Instrumentation instrumentation) throws Exception {
        log.info("agentmain is run,agentArgs={}", agentArgs);

        log.info("Agent Main called");
        log.info("agentArgs : " + agentArgs);
        instrumentation.addTransformer(new ClassFileTransformer() {

            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) throws IllegalClassFormatException {
                System.out.println("agentmain load Class  :" + className);
                return classfileBuffer;
            }
        }, true);
        //你需要重新定义哪个类，需要指定，否则 JVM 不可能知道
        //instrumentation.retransformClasses(Account.class);
    }
}
