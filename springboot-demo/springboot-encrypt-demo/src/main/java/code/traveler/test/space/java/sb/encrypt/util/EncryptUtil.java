package code.traveler.test.space.java.sb.encrypt.util;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

@Deprecated
@Slf4j
public class EncryptUtil {

    private static StringEncryptor stringEncryptor = jasyptStringEncryptor();

    private static final String SECRET_KEY = "Y118&#5b";
    //3.0.5支持三种算法，默认：PBEWithHMACSHA512AndAES_256、PBEWithHMACSHA512AndAES_128、PBEWithMD5AndDES、PBEWithMD5AndTripleDES
    private static final String ALGORITHM = "PBEWithHMACSHA512AndAES_128";
    private static final String ITERATION = "1000";
    private static final String POOL_SIZE = "1";
    private static final String PROVIDER_NAME = "SunJCE";
    private static final String SALT_CLASS = "org.jasypt.salt.RandomSaltGenerator";
    private static final String IV = "org.jasypt.iv.NoIvGenerator";
    private static final String OUTPUT_TYPE = "base64";


    /**
     * @param password
     * @return java.lang.String
     * @description 加密
     * @author zhouyuxiang
     * @time 2023/8/16 22:37
     */
    public static String encrypt(String password) {
        String result = stringEncryptor.encrypt(password);
        log.info("password加密结果为：{}", result);
        return result;
    }

    /**
     * @param password
     * @return java.lang.String
     * @description 解密
     * @author zhouyuxiang
     * @time 2023/8/16 22:37
     */
    public static String decrypt(String password) {
        String result = stringEncryptor.decrypt(password);
        log.info("password解密结果为：{}", result);
        return result;
    }

    private static StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(SECRET_KEY);
        config.setAlgorithm(ALGORITHM);
        config.setKeyObtentionIterations(ITERATION);
        config.setPoolSize(POOL_SIZE);
        config.setProviderName(PROVIDER_NAME);
        config.setSaltGeneratorClassName(SALT_CLASS);
        config.setIvGeneratorClassName(IV);
        config.setStringOutputType(OUTPUT_TYPE);
        encryptor.setConfig(config);
        return encryptor;
    }
}
