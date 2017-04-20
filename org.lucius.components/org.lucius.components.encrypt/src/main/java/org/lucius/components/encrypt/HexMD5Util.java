package org.lucius.components.encrypt;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import org.apache.commons.lang3.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MD5加密解密及字符串对比工具类
 */

public class HexMD5Util {

    private static final Logger logger = LoggerFactory
            .getLogger(HexMD5Util.class);

    private static final String HEX_NUMS_STR = "0123456789ABCDEF";

    private static final Integer SALT_LENGTH = 12;
    
    @SuppressWarnings("unused")
	private static final int RADIX_16 = 16;

    /**
     * 将16进制字符串转换成数组
     * 
     * @param hex
     *            16進制字符串
     * @return byte[]
     * @author jacob
     * */
    public static byte[] hexStringToByte(String hex) {
        /*
         * len为什么是hex.length() / 2 ? 首先，hex是一个字符串，里面的内容是像16进制那样的char数组
         * 用2个16进制数字可以表示1个byte
         * ，所以要求得这些char[]可以转化成什么样的byte[]，首先可以确定的就是长度为这个char[]的一半
         */
        int len = hex.length() / 2;
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
                    .indexOf(hexChars[pos + 1]));
        }
        return result;
    }

    /**
     * 将数组转换成16进制字符串
     * 
     * @param salt
     *            salt
     * @return String
     * @author jacob
     * 
     * */
    public static String byteToHexString(byte[] salt) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < salt.length; i++) {
            String hex = Integer.toHexString(salt[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 密码验证
     * 
     * @param passwd
     *            用户输入密码(明文)
     * @param dbPasswd
     *            数据库保存的密码
     * @return
     * @throws NoSuchAlgorithmException
     *             NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     */
    public static boolean validPasswd(String passwd, String dbPasswd)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (StringUtils.isBlank(dbPasswd) || StringUtils.isBlank(passwd)) {
            return false;
        }
        byte[] pwIndb = hexStringToByte(dbPasswd);
        // 定义salt
        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(pwIndb, 0, salt, 0, SALT_LENGTH);
        // 创建消息摘要对象
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 将盐数据传入消息摘要对象
        md.update(salt);
        md.update(getMd5Password(passwd).getBytes("UTF-8"));
        byte[] digest = md.digest();
        // 声明一个对象接收数据库中的口令消息摘要
        byte[] digestIndb = new byte[pwIndb.length - SALT_LENGTH];
        // 获得数据库中口令的摘要
        System.arraycopy(pwIndb, SALT_LENGTH, digestIndb, 0, digestIndb.length);
        // 比较根据输入口令生成的消息摘要和数据库中的口令摘要是否相同
        if (Arrays.equals(digest, digestIndb)) {
            // 口令匹配相同
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取密文密码是否与数据库密码一致
     * 
     * @param passwd
     *            用户输入密码(半密文)
     * @param dbPasswd
     *            数据库中的密码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static boolean validEncrptyPasswd(String passwd, String dbPasswd)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (StringUtils.isBlank(dbPasswd) || StringUtils.isBlank(passwd)) {
            return false;
        }
        // 先从数据库中的密文密码处获取加密该密码所使用的salt盐值字节数组
        byte[] pwIndb = hexStringToByte(dbPasswd);
        // 定义salt
        byte[] salt = new byte[SALT_LENGTH];
        System.arraycopy(pwIndb, 0, salt, 0, SALT_LENGTH);
        // 根据特定的盐值字节数组加密半密文密码(passwd) 获取完整的密文密码
        String encryptPwd = getEncryptedPwdBySalt(passwd, salt);
        if (StringUtils.equals(encryptPwd, dbPasswd)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获得md5之后的16进制字符
     * 
     * @param passwd
     *            用户输入密码字符
     * @return String md5加密后密码字符
     * @throws NoSuchAlgorithmException
     *             NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     */
    public static String getEncryptedPwd(String passwd)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (StringUtils.isBlank(passwd)) {
            return null;
        }
        // 拿到一个随机数组，作为盐
        byte[] pwd = null;
        SecureRandom sc = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        sc.nextBytes(salt);

        // 声明摘要对象，并生成
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(getMd5Password(passwd).getBytes("UTF-8"));
        byte[] digest = md.digest();

        pwd = new byte[salt.length + digest.length];
        System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
        System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
        return byteToHexString(pwd);
    }

    /**
     * 根据某个特定的盐值获取密码密文
     * 
     * @param passwd
     *            用户输入密码字符
     * @return String salt盐值字节数组
     * @throws NoSuchAlgorithmException
     *             NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     */
    public static String getEncryptedPwdBySalt(String passwd, byte[] salt)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 拿到一个随机数组，作为盐
        if (StringUtils.isBlank(passwd)) {
            passwd = getMd5Password(passwd);
        }
        byte[] pwd = null;
        // 声明摘要对象，并生成
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(passwd.getBytes("UTF-8"));
        byte[] digest = md.digest();

        pwd = new byte[salt.length + digest.length];
        System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
        System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
        return byteToHexString(pwd);
    }

    /**
     * 获得md5之后的16进制字符
     * 
     * @param passwd
     *            用户输入密码字符
     * @return String md5加密后密码字符
     * @throws NoSuchAlgorithmException
     *             NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     */
    public static String getTempEncryptedPwd(String passwd)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 拿到一个随机数组，作为盐
        if (StringUtils.isBlank(passwd)) {
            passwd = getMd5Password(passwd);
        }

        byte[] pwd = null;
        SecureRandom sc = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        sc.nextBytes(salt);

        // 声明摘要对象，并生成
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(salt);
        md.update(passwd.getBytes("UTF-8"));
        byte[] digest = md.digest();

        pwd = new byte[salt.length + digest.length];
        System.arraycopy(salt, 0, pwd, 0, SALT_LENGTH);
        System.arraycopy(digest, 0, pwd, SALT_LENGTH, digest.length);
        return byteToHexString(pwd);
    }

    /**
     * 加密密码
     * 
     * @param password
     *            password
     * @return
     */
    private static String getMd5Password(String password) {
        // 默认密码
        if (StringUtils.isBlank(password)) {
            password = "123456";
        }
        Encrypt encrypt = new MD5Encrypt();
        try {
            password = CryptUtil.encodeBytes(encrypt.encrypt(
                    password.getBytes(StandardCharsets.UTF_8), null));
            Charset.availableCharsets();
        } catch (Exception e1) {
            logger.error("密码加密错误: " + password);
            e1.printStackTrace();
        }
        return password;
    }

    /**
     * 加密密码
     * 
     * @param password
     *            password
     * @return
     */
    public static String getMd5FromUnEncodePassword(String password) {
        // 默认密码
        if (StringUtils.isBlank(password)) {
            password = "123456";
        }
        try {
            password = CryptUtil.encodeBytes(HexString2Bytes(password));
        } catch (Exception e1) {
            logger.error("密码加密错误: " + password);
            e1.printStackTrace();
        }
        return password;
    }
    
    public static byte[] HexString2Bytes(String src) {  
        byte[] ret = new byte[16];  
        byte[] tmp = src.getBytes();  
        for (int i = 0; i < 16; i++) {  
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);  
        }  
        return ret;  
    }  
    
    public static byte uniteBytes(byte src0, byte src1) {  
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();  
        _b0 = (byte) (_b0 << 4);  
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();  
        byte ret = (byte) (_b0 ^ _b1);  
        return ret;  
    }

    public static void main(String[] args) throws NoSuchAlgorithmException,
            UnsupportedEncodingException {
        System.out.println(getTempEncryptedPwd("4QrcOUm6Wau+VuBX8g+IPg=="));
        System.out.println(getTempEncryptedPwd("4QrcOUm6Wau+VuBX8g+IPg=="));
        // A219B47A48D7E06CF9FAF694DD047752B7B21290F45ED0BADA880697
        // 3C18CABB60E14B98E352DAF709F36B0F92CC808534492740BE81AB03

        System.out.println(validEncrptyPasswd("4QrcOUm6Wau+VuBX8g+IPg==",
                "A219B47A48D7E06CF9FAF694DD047752B7B21290F45ED0BADA880697"));
        System.out.println(validEncrptyPasswd("4QrcOUm6Wau+VuBX8g+IPg==",
                "3C18CABB60E14B98E352DAF709F36B0F92CC808534492740BE81AB03"));
        System.out.println(getMd5FromUnEncodePassword(MD5Util.getMD5String("123456")));
        System.out.println(getMd5Password("123456"));
    }
}
