package org.lucius.components.encrypt;

import java.security.MessageDigest;

public class SHAEncrypt implements Encrypt {

    private static final String KEY_SHA = "SHA";

    @Override
    public byte[] encrypt(byte[] data, String key) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

}
