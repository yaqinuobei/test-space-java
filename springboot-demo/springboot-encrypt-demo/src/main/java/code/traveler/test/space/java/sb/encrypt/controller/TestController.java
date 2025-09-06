package code.traveler.test.space.java.sb.encrypt.controller;

import code.traveler.test.space.java.sb.encrypt.config.TestProperties;
import code.traveler.test.space.java.sb.encrypt.util.EncryptUtil;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Deprecated
@RestController
@RequestMapping("/v1/encrypt")
public class TestController {

    @GetMapping("/encrypt")
    public String encrypt(String plaintext){
        return EncryptUtil.encrypt(plaintext);
    }

    @GetMapping("/decrypt")
    public String decrypt(String ciphertext){
        return EncryptUtil.decrypt(ciphertext);
    }
}
