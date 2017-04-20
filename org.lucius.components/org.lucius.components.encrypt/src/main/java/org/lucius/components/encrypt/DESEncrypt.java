package org.lucius.components.encrypt;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES 算法 <br>
 * 可替换为以下任意一种算法，同时key值的size相应改变。
 * 
 * <pre>
 * DES                  key size must be equal to 56 
 * DESede(TripleDES)    key size must be equal to 112 or 168 
 * AES                  key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available 
 * Blowfish             key size must be multiple of 8, and can only range from 32 to 448 (inclusive) 
 * RC2                  key size must be between 40 and 1024 bits 
 * RC4(ARCFOUR)         key size must be between 40 and 1024 bits 
 * </pre>
*/


public class DESEncrypt implements Encrypt{

    public static final String ALGORITHM = "DES";
    @Override
    public byte[] encrypt(byte[] data, String key) throws Exception {
        Key k = toKey(CryptUtil.decode(key));
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * DES 算法转换密钥<br>
     * 
     * @param key key
     * @return
     * @throws Exception Exception
     */
    private Key toKey(byte[] key) throws Exception {
        SecretKey secretKey = null;
        if (ALGORITHM.equals("DES") || ALGORITHM.equals("DESede")) {
            DESKeySpec dks = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            secretKey = keyFactory.generateSecret(dks);
        } else {
            // 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
            secretKey = new SecretKeySpec(key, ALGORITHM);
        }
        return secretKey;
    }

     /**
     * DES 算法生成密钥
     * 
     * @return
     * @throws Exception Exception
*/
    public String initKey() throws Exception {
        return initKey(null);
    }

    /**
     * DES 算法生成密钥
     * 
     * @param seed seed
     * @return
     * @throws Exception Exception
*/
    public String initKey(String seed) throws Exception {
        SecureRandom secureRandom = null;
        if (seed != null) {
            secureRandom = new SecureRandom(CryptUtil.decode(seed));
        } else {
            secureRandom = new SecureRandom();
        }
        KeyGenerator kg = KeyGenerator.getInstance(ALGORITHM);
        kg.init(secureRandom);
        SecretKey secretKey = kg.generateKey();
        return CryptUtil.encodeBytes(secretKey.getEncoded());
    }
}
