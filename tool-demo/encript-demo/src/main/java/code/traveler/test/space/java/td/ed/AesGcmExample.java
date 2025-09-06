package code.traveler.test.space.java.td.ed;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AesGcmExample {

    public static final String ALGRITHM = "AES";

    public static final String TRANSFORMATION = "AES/GCM/NOPadding";

    public static final int AES_KEY_SIZE = 256;

    public static final int GCM_TAG_LENGTH = 16;

    public static final int GCM_IV_LENGTH = 12;

    //生成aes密钥
    public static SecretKey generateKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGRITHM);
        keyGen.init(AES_KEY_SIZE);
        return keyGen.generateKey();
    }

    //加密明文
    public static String encrypt(String plainText,SecretKey key) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        byte[] iv = new byte[GCM_IV_LENGTH];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);

        byte[] cipherText = cipher.doFinal(plainText.getBytes());
        byte[] encrypted = new byte[iv.length+cipherText.length];
        System.arraycopy(iv,0,encrypted,0,iv.length);
        System.arraycopy(cipherText,0,encrypted,iv.length,cipherText.length);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    //解密密文
    public static String decrypt(String encrypted, SecretKey key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decoded = Base64.getDecoder().decode(encrypted);
        byte[] iv = new byte[GCM_IV_LENGTH];
        System.arraycopy(decoded,0,iv,0,GCM_IV_LENGTH);

        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        byte[] cipherText = new byte[decoded.length-GCM_IV_LENGTH];
        System.arraycopy(decoded,GCM_IV_LENGTH,cipherText,0,cipherText.length);
        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }
    public static void main(String[] args) {
        SecretKey key = null;
        try {
            key = generateKey();
            System.out.println("Key encode:"+Base64.getEncoder().encodeToString(key.getEncoded()));
            String plainText = "hello";
            String encrypted = encrypt(plainText, key);
            System.out.println("Encrypted:"+encrypted);

            String decrypted = decrypt(encrypted, key);
            System.out.println("Decrypted:"+decrypted);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }
}
