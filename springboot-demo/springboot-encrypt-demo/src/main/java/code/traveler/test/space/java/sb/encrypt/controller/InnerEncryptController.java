package code.traveler.test.space.java.sb.encrypt.controller;

import code.traveler.test.space.java.sb.encrypt.config.TestProperties;
import code.traveler.test.space.java.sb.encrypt.util.EncryptUtil;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/innerencrypt")
public class InnerEncryptController {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Autowired
    private TestProperties testProperties;

    @GetMapping("/getOriginPassword")
    public String getOriginPassword(){
        return testProperties.getPassword();
    }

    @GetMapping("/getEncryptPassword")
    public String getEncryptPassword(){
        return testProperties.getPassword();
    }

    @GetMapping("/encrypt")
    public String encrypt(@RequestParam("plaintext") String plaintext){
        return stringEncryptor.encrypt(plaintext);
    }

    @GetMapping("/decrypt")
    public String decrypt(@RequestParam("ciphertext") String ciphertext){
        return stringEncryptor.decrypt(ciphertext);
    }
}
