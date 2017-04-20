package org.lucius.components.encrypt;

public interface Encrypt {

    /**
     * 
     * @param data 字节数组
     * @param key key
     * @return
     * @throws Exception Exception
     */
    byte[] encrypt(byte[] data, String key) throws Exception ;
}
