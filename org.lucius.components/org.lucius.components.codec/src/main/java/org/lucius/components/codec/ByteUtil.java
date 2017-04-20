package org.lucius.components.codec;

import java.nio.charset.Charset;
/**
 * 
 * @author wangle
 * @2014-6-26
 */
public class ByteUtil {
	public static byte[] BigToLittleOrLittleToBig(byte[] src) {
		int lenght = src.length;
		byte[] ret = new byte[lenght];
		for (int i = lenght; i > 0; i--) {
			ret[i - 1] = src[lenght - i];
		}
		return ret;
	}

	public static byte[] join(byte[] src, byte[] desc) {
		byte[] result = new byte[src.length + desc.length];
		System.arraycopy(src, 0, result, 0, src.length);
		System.arraycopy(desc, 0, result, src.length, desc.length);
		return result;
	}
	
	/**
	 * 
	 * @param i
	 * @param type 1 : 1000  2 : 0001
	 * @return
	 */
	public static byte[] int2Bytes(int val,int type) {
		byte[] result = new byte[4];
		if(type == 1){
			result[0] = (byte) (val & 0xff);
			result[1] = (byte) ((val & 0xff00) >> 8);
			result[2] = (byte) ((val & 0xff0000) >> 16);
			result[3] = (byte) ((val & 0xff000000) >> 24);
				
		}else{
			result[0] = (byte) ((val >> 24) & 0xFF);
	        result[1] = (byte) ((val >> 16) & 0xFF);
	        result[2] = (byte) ((val >> 8) & 0xFF);
	        result[3] = (byte) (val & 0xFF);
		}
	        return result;
	}

	public static byte[] long2Bytes(long val) {
		byte[] result = new byte[8];
		result[0] = (byte) (val & 0xff);
		result[1] = (byte) ((val >> 8) & 0xff);
		result[2] = (byte) ((val >> 16) & 0xff);
		result[3] = (byte) ((val >> 24) & 0xff);
		result[4] = (byte) ((val >> 32) & 0xff);
		result[5] = (byte) ((val >> 40) & 0xff);
		result[6] = (byte) ((val >> 48) & 0xff);
		result[7] = (byte) ((val >> 56) & 0xff);
		return result;
	}

	public static int getInt(byte[] bytes) {
		return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8))
				| (0xff0000 & (bytes[2] << 16))
				| (0xff000000 & (bytes[3] << 24));
	}

	public static long getLong(byte[] bytes) {
		return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8))
				| (0xff0000L & ((long) bytes[2] << 16))
				| (0xff000000L & ((long) bytes[3] << 24))
				| (0xff00000000L & ((long) bytes[4] << 32))
				| (0xff0000000000L & ((long) bytes[5] << 40))
				| (0xff000000000000L & ((long) bytes[6] << 48))
				| (0xff00000000000000L & ((long) bytes[7] << 56));
	}

	public static byte[] string2Bytes(String val, String charsetName) {
		Charset charset = Charset.forName(charsetName);
		return val.getBytes(charset);
	}

	public static byte[] string2Bytes(String val) {
		return string2Bytes(val, "utf-8");
	}

	public static String getString(byte[] bytes) {
		return getString(bytes, "utf-8");
	}

	public static String getString(byte[] bytes, String charsetName) {
		return new String(bytes, Charset.forName(charsetName));
	}

	public static int getInt(byte[] source, int postion, int suffixLength) {
		if (source == null || source.length < postion + suffixLength)
			throw new IllegalArgumentException("invalid byte array");
		byte[] result = new byte[suffixLength];
		System.arraycopy(source, postion, result, 0, suffixLength);
		return getInt(result);
	}

	public static String getString(byte[] source, int postion, int suffixLength) {
		if (source == null || source.length < postion + suffixLength)
			throw new IllegalArgumentException("invalid byte array");
		byte[] result = new byte[suffixLength];
		System.arraycopy(source, postion, result, 0, suffixLength);
		return getString(result);
	}

	public static long getLong(byte[] source, int postion, int suffixLength) {
		if (source == null || source.length < postion + suffixLength)
			throw new IllegalArgumentException("invalid byte array");
		byte[] result = new byte[suffixLength];
		System.arraycopy(source, postion, result, 0, suffixLength);
		return getLong(result);
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	public static byte[] hex2byte(String str) { // 字符串转二进制
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 != 0)
			return null;
		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer
						.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}

	}
}
