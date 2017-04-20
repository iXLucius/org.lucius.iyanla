package org.lucius.components.encrypt;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class HMACEncrypt implements Encrypt {

    /**
     * MAC算法可选以下多种算法
     * 
     * <pre>
     *  
     * HmacMD5  
     * HmacSHA1  
     * HmacSHA256  
     * HmacSHA384  
     * HmacSHA512 
     * </pre>
*/
    public static final String KEY_MAC = "HmacMD5";

    @Override
    public byte[] encrypt(byte[] data, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(CryptUtil.decode(key), KEY_MAC);
        Mac mac = Mac.getInstance(secretKey.getAlgorithm());
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    /**
     * @return 初始化
     * @throws Exception Exception
     */
    public String initMacKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_MAC);
        SecretKey secretKey = keyGenerator.generateKey();
        return CryptUtil.encodeBytes(secretKey.getEncoded());
    }
}
