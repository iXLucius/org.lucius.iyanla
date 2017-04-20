package org.lucius.components.codec;

public class ByteToHex {

	
	/**
	 * 
	 * 将一个字节数组转换成十六进制字符串
	 * 
	 * @param b 二进制的字节数组
	 * 
	 * @return 十六进制字符串
	 * 
	 */
	public static String byte2hex(byte[] b) {
		String result = "";
		String stmp = "";
		for (int i = 0; i < b.length; i++) {
			stmp = java.lang.Integer.toHexString((b[i] & 0XFF));
			if(stmp.length() == 1) 
				result = result + "0" + stmp;
			else 
				result = result + stmp;
		}
		return result;
	}
	
	
	/**
	 * 
	 * 从source字节数组下标pos开始，截取len长的字节数并转换成十六进制字符串
	 * 
	 * @param source 源二进制字节数组
	 * 
	 * @param pos 源字节数组转换的开始位置
	 * 
	 * @param len  源字节数组中转换的长度
	 * 
	 * @return 十六进制字符串
	 * 
	 */
	public static String byte2hex(byte[] source, int pos, int len) {
		 if (source == null || source.length < pos + len)
	            throw new IllegalArgumentException("invalid byte array");
		 byte[] result = new byte[len];
	     System.arraycopy(source, pos, result, 0, len);
		return byte2hex(result);
	}
	
	
	/**
	 * 
	 * 将十六进制字符串转换成字节数组
	 * 
	 * @param hexStr 十六进制字符串
	 * 
	 * @return 二进制的字节数组
	 * 
	 */
	public static byte[] hex2byte(String hexStr) {
		if(hexStr == null) return null;
		hexStr = hexStr.trim();
		int len = hexStr.length();
		if(len == 0 || len % 2 != 0) return null;
		byte[] result = new byte[len / 2];
		try {
			for (int i = 0; i < hexStr.length(); i += 2) 
				result[i / 2] = (byte) Integer.decode("0x" + hexStr.substring(i, i + 2)).intValue();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
