package code.traveler.test.space.java.simple.agent.controller.attach;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.util.List;

public class AttachBoot {

    public static void main(String[] args){
        try {
            List<VirtualMachineDescriptor> list = VirtualMachine.list();
            for (VirtualMachineDescriptor vmd : list) {
                if (vmd.displayName().endsWith("AgentmainApp")) {
                    VirtualMachine virtualMachine = VirtualMachine.attach(vmd.id());
                    virtualMachine.loadAgent("E:\\WorkSpace\\code\\github\\test-space-java\\agent-demo\\simple-agent\\target\\simple-agent-1"
                            + ".0-SNAPSHOT.jar", "AgentmainApp");
                    System.out.println("ok");
                    virtualMachine.detach();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
