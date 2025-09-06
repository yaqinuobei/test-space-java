package code.traveler.test.space.java.sb.encrypt;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
//@EnableEncryptableProperties
public class EncryptApplication {

    public static void main(String[] args){
        SpringApplication.run(EncryptApplication.class,args);
    }

}
