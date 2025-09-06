package code.traveler.test.space.java.sb.app.controller;

import code.traveler.test.space.java.sb.app.bo.PreachingActivity;
import code.traveler.test.space.java.sb.app.bo.PreachingPerson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/v1/test/locky")
public class TestController {

    private static List<String> validNames = new ArrayList(){{
        add("houmi");
        add("xuesongming");
        add("zhangtao");
        add("weichuansi");
        add("chenfengmin");
        add("maoyuting");
//        add("bianxiangdong");
//        add("zhangjifeng");
        add("zhouyuxiang");
        }};

    private static Map<String, PreachingActivity> preachingActivityMap = new HashMap();

    private static Map<String, PreachingPerson> PreachingPersonResultMap = new HashMap<>();

    private String retSuccessCont = "####2024年未来5期研发培训，演讲者抽选ing####\n\n，恭喜你抽中了，你的演讲顺序是第%s个，日期：%s,主题：%s";
    private String retFailCont = "####2024年未来5期研发培训，演讲者抽选ing####\n\n，很遗憾你没有抽中。。。";

    private Object lock = new Object();

    @GetMapping("/preaching/{name}")
    public String preaching(@PathVariable("name") String name){
        PreachingPerson pp = null;

        if(!validNames.contains(name)){
            return "不合法的用户！";
        }

        if(PreachingPersonResultMap.containsKey(name)){
            pp = PreachingPersonResultMap.get(name);

        }else{
            synchronized (lock){
                List<String> remainActList = new ArrayList<>();
                preachingActivityMap.forEach((order,ppi)->{
                    if(!ppi.isAllocated()){
                        remainActList.add(ppi.getOrder());
                    }
                });

                int remainNum = remainActList.size();
                Random random = new Random();
                int snum = random.nextInt(remainNum);
                String order = remainActList.get(snum);
                PreachingActivity pa = preachingActivityMap.get(order);
                pa.setAllocated(true);
                pp = PreachingPerson.builder().name(name).isPreaching(true).pa(pa).build();
                if(pa.isFake()){
                    pp.setPreaching(false);
                }
                PreachingPersonResultMap.put(name,pp);
            }
        }

        return resultStr(pp);
    }

    @GetMapping("/reset")
    public String preaching(){
        PreachingPersonResultMap.clear();
        preachingActivityMap.clear();
        initActMap();
        initPersonMap();
        return "reset success";
    }

    private String resultStr(PreachingPerson pp){
        if(pp.isPreaching()){
            return String.format(retSuccessCont,pp.getPa().getOrder(),pp.getPa().getDate(),pp.getPa().getTitle());
        }else{
            return String.format(retFailCont);
        }
    }

    private static void initActMap(){
        preachingActivityMap.put("1",
                PreachingActivity.builder().order("1").date("4月1日").title("微服务注册发现与配置管理").isAllocated(true).isFake(false).build());
        preachingActivityMap.put("2",
                PreachingActivity.builder().order("2").date("待定，预计2个月后").title("springcloud开发框架精讲").isAllocated(false).isFake(false).build());
        preachingActivityMap.put("3",
                PreachingActivity.builder().order("3").date("待定，预计4个月后").title("待定").isAllocated(false).isFake(false).build());
        preachingActivityMap.put("4",
                PreachingActivity.builder().order("4").date("待定，预计6个月后").title("待定").isAllocated(false).isFake(false).build());
        preachingActivityMap.put("5",
                PreachingActivity.builder().order("5").date("待定，预计8个月后").title("待定").isAllocated(false).isFake(false).build());
        preachingActivityMap.put("6",
                PreachingActivity.builder().order("6").date("待定，预计10个月后").title("待定").isAllocated(false).isFake(true).build());
        preachingActivityMap.put("7",
                PreachingActivity.builder().order("7").date("待定，预计12个月后").title("待定").isAllocated(false).isFake(true).build());

    }

    private static void initPersonMap(){

        PreachingPersonResultMap.put("maoyuting",
                PreachingPerson.builder().name("maoyuting").isPreaching(true).pa(preachingActivityMap.get("1")).build());
    }

    static{
        initActMap();
        initPersonMap();
    }
}
