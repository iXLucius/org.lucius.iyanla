package org.lucius.components.encrypt;

public interface Decrypt {
    
    /**
     * 反解密
     * @param data 字节数组
     * @param key key
     * @return
     * @throws Exception 异常
     */
    byte[] decrypt(byte[] data, String key) throws Exception;
}
