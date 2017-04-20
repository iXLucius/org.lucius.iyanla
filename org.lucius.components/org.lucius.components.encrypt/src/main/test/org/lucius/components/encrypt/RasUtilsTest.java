package org.lucius.components.encrypt;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.lucius.components.codec.Base64Utils;
import org.lucius.components.codec.ByteUtil;

public class RasUtilsTest {
	
	public static void main(String[] args) throws Exception {
		
        KeyPair keyPair = RSAUtils.genKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        
        byte[] publicKeyBytes = publicKey.getEncoded();
        byte[] privateKeyBytes = privateKey.getEncoded();
        
        String publicKeyStr = Base64Utils.encode(publicKeyBytes);
        String privateKeyStr = Base64Utils.encode(privateKeyBytes);
        
        System.out.println(publicKeyStr);
        System.out.println(privateKeyStr);
        
        publicKeyStr = ByteUtil.byte2hex(privateKeyBytes);
        privateKeyStr = ByteUtil.byte2hex(privateKeyBytes);
        
        System.out.println(publicKeyStr);
        System.out.println(privateKeyStr);
        
        
        
		
	}

}
