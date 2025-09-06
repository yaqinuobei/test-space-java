package code.traveler.test.space.java.sb.app.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@EnableScheduling
@Service
@Slf4j
public class CronJob {

    AtomicInteger runcount=new AtomicInteger(0);


    @Scheduled(cron = "0/1 * * * * ? ")
    public void testExec(){
        try {
            int num=runcount.addAndGet(1);
            Date startTime = new Date();
            log.info("time="+startTime.getTime()+",task"+num+" begin! threadName="+Thread.currentThread().getName());
            Thread.sleep(10000);
            Date endTime = new Date();
            log.info("time="+endTime.getTime()+",task"+num+" end! threadName="+Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
