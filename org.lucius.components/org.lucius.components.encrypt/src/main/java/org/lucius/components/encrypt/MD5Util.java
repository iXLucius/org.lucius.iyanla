package org.lucius.components.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5Util {

    protected static MessageDigest messagedigest;

    private static final int RADIX_16 = 16;

    protected static char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    private static final Logger logger = LoggerFactory.getLogger(MD5Util.class);

    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            logger.error(MD5Util.class.getName()
                    + "初始化失败，MessageDigest不支持MD5Util。");
            nsaex.printStackTrace();
        }
    }

    /**
     * 
     * @param file
     *            文件
     * @return
     * @throws IOException
     *             IOException
     */
    @SuppressWarnings("resource")
    public static String getFileMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                file.length());
        messagedigest.update(byteBuffer);
        return bufferToHex(messagedigest.digest());
    }

    /**
     * 
     * @param s
     *            要加密的字符串
     * @return
     */
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    /**
     * 
     * @param bytes
     *            字节数组
     * @return
     */
    public static String getMD5String(byte[] bytes) {
        messagedigest.update(bytes);
        bytes = messagedigest.digest();
        return bufferToHex(bytes);
    }

    /**
     * 
     * @param bytes
     *            字节数组
     * @return
     */
    public static String bufferToHex(byte[] bytes) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    /**
     * 
     * @param bytes
     *            字节数组
     * @param m
     *            m
     * @param n
     *            n
     * @return
     */
    private static String bufferToHex(byte[] bytes, int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    /**
     * 
     * @param bt
     *            bt
     * @param stringbuffer
     *            stringbuffer
     */
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    /**
     * 
     * @param password
     *            原始密码
     * @param md5PwdStr
     *            密文
     * @return
     */
    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    /**
     * 
     * @param file
     *            文件
     * @param md5
     *            密文
     * @return
     * @throws IOException
     *             IOException
     */
    public static boolean checkFileMD5(File file, String md5)
            throws IOException {
        String fileMd5 = getFileMD5String(file);
        if (fileMd5.toUpperCase().equals(md5.toUpperCase())) {
            return true;
        } else {
            return false;
        }
    }

    public static byte[] byteFormHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            // 16进制字符转换成int->位运算（取int(32位)低8位,即位与运算 &0xFF）->强转成byte
            bytes[i] = (byte) (0xFF & Integer.parseInt(
                    hex.substring(i * 2, i * 2 + 2), RADIX_16));
        }
        return bytes;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

    @SuppressWarnings("unused")
	public static void main(String[] args) {
        System.out.println(MD5Util.getMD5String("123456"));
        //System.out.println(MD5Util.bufferToHex(hexStringToByte(MD5Util.getMD5String("123456"))));
        char c0 = hexDigits[(-31 & 0xf0) >> 4];
        char c1 = hexDigits[-31 & 0xf];
        System.out.println(c0 +""+ c1);
        String[] x = "e10adc3949ba59abbe56e057f20f883e".split("",16);
        //System.out.println("x = " + ToStringBuilder.reflectionToString(x));
        //byte devBin = (byte) Integer.parseInt("e1", 16);
        //System.out.println(devBin);
        System.out.println(ToStringBuilder.reflectionToString(HexString2Bytes("e10adc3949ba59abbe56e057f20f883e")));
        //[B@3dd6e4a4[{-31,10,-36,57,73,-70,89,-85,-66,86,-32,87,-14,15,-120,62}]
        //[B@68812517[{-31,10,-36,57,73,-70,89,-85}]
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
    
}
