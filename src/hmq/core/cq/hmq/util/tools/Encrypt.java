package core.cq.hmq.util.tools;

import it.sauronsoftware.base64.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * 
 * md5加密出来的长度是32位
 * 
 * sha加密出来的长度是40位
 * 
 * base64加密可以指定字符集，可以解密
 * 
 * @author 孙宇
 * 
 */
public class Encrypt {

	public static void main(String[] args) {

		System.out.println();

		// md5加密测试
		String md5 = md5("pk007");
		System.out.println(md5);
		System.out.println(JM("c12e01f2a13ff5587e1e9e4aedb8242d"));
		// sha加密测试
		String sha = sha("1");
		System.out.println(sha);
		
		String base64=base64Encode("123");
		System.out.println(base64);
		System.out.println(base64Decode(base64));

	}

	public static String JM(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String k = new String(a);
		return k;
	}

	/**
	 * 使用Base64加密
	 * 
	 * @param inputText
	 * @param charset
	 *            字符集
	 * @return
	 */
	public static String base64Encode(String inputText, String... charset) {
		if (charset.length == 1) {
			return Base64.encode(inputText, charset[0]);
		} else {
			return Base64.encode(inputText);
		}
	}

	/**
	 * Base64解密
	 * 
	 * @param inputText
	 * @param charset
	 *            字符集
	 * @return
	 */
	public static String base64Decode(String inputText, String... charset) {
		if (charset.length == 1) {
			return Base64.decode(inputText, charset[0]);
		} else {
			return Base64.decode(inputText);
		}
	}

	/**
	 * 二次加密，应该破解不了了吧？
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5AndSha(String inputText) {
		return sha(md5(inputText));
	}

	/**
	 * md5加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String md5(String inputText) {
		return encrypt(inputText, "md5");
	}

	/**
	 * sha加密
	 * 
	 * @param inputText
	 * @return
	 */
	public static String sha(String inputText) {
		return encrypt(inputText, "sha-1");
	}

	/**
	 * md5或者sha-1加密
	 * 
	 * @param inputText
	 *            要加密的内容
	 * @param algorithmName
	 *            加密算法名称：md5或者sha-1，不区分大小写
	 * @return
	 */
	private static String encrypt(String inputText, String algorithmName) {
		if (inputText == null || "".equals(inputText.trim())) {
			throw new IllegalArgumentException("请输入要加密的内容");
		}
		if (algorithmName == null || "".equals(algorithmName.trim())) {
			algorithmName = "md5";
		}
		String encryptText = null;
		try {
			MessageDigest m = MessageDigest.getInstance(algorithmName);
			m.update(inputText.getBytes("UTF8"));
			byte s[] = m.digest();
			// m.digest(inputText.getBytes("UTF8"));
			return hex(s);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptText;
	}

	/**
	 * 返回十六进制字符串
	 * 
	 * @param arr
	 * @return
	 */
	private static String hex(byte[] arr) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; ++i) {
			sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1,
					3));
		}
		return sb.toString();
	}

	/**
	 * 16进制字符串转换为byte
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] hexChars = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) ("0123456789ABCDEF".indexOf(hexChars[pos]) << 4 | "0123456789ABCDEF"
					.indexOf(hexChars[pos + 1]));
		}
		return result;
	}
}
