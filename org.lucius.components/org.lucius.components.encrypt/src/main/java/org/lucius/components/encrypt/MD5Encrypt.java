package org.lucius.components.encrypt;

import java.security.MessageDigest;

public class MD5Encrypt implements Encrypt{

    private static final String KEY_MD5 = "MD5";
    @Override
    public byte[] encrypt(byte[] data, String key) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);

        return md5.digest();
    }

}
