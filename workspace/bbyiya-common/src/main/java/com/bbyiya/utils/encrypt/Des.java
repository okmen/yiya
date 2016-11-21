package com.bbyiya.utils.encrypt;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 
 *
 * 
 * 使用DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储.
 * 
 *
 * 
 * 方法: void getKey(String strKey)从strKey的字条生成一个Key
 * 
 *
 * 
 * String getEncString(String strMing)对strMing进行加密,返回String密文 String
 * 
 * getDesString(String strMi)对strMin进行解密,返回String明文
 * 
 *
 * 
 * byte[] getEncCode(byte[] byteS)byte[]型的加密 byte[] getDesCode(byte[]
 * 
 * byteD)byte[]型的解密
 */

public class Des {


	/**
	 * 
	 * 根据参数生成KEY
	 * 
	 *String strKey
	 * 
	 * @param strKey
	 */

	public static Key getKey(String strKeyString) {

		try {

			KeyGenerator _generator = KeyGenerator.getInstance("DES");

			_generator.init(new SecureRandom(strKeyString.getBytes()));

			Key key = _generator.generateKey();

			_generator = null;
			return key;

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 加密（过期，不可用）
	 * String明文输入,String密文输出
	 * 
	 * @param strMing
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public static String getEncString(String strMing,String strKey) {

		byte[] byteMi = null;

		byte[] byteMing = null;

		String strMi = "";

		try {

			return byte2hex(getEncCode(strMing.getBytes(),strKey));

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			byteMing = null;

			byteMi = null;

		}

		return strMi;

	}

	/**
	 * 解密（过期，不可用）
	 * 以String密文输入,String明文输出
	 * 
	 * @param strMi
	 * 
	 * @return
	 */

	@SuppressWarnings("unused")
	public static String getDesString(String strMi,String strKey) {

		byte[] byteMing = null;

		byte[] byteMi = null;

		String strMing = "";

		try {

			return new String(getDesCode(hex2byte(strMi.getBytes()),strKey));

			// byteMing = this.getDesCode(byteMi);

			// strMing = new String(byteMing,"UTF8");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			byteMing = null;

			byteMi = null;

		}

		return strMing;

	}

	/**
	 * （过期，不可用）
	 * 加密 以byte[]明文输入,byte[]密文输出
	 * 
	 *
	 * 
	 * @param byteS
	 * 
	 * @return
	 */

	private static byte[] getEncCode(byte[] byteS,String strkey) {

		byte[] byteFina = null;
		String stringKey="@ai8!lk5";
		if(!"".equals(strkey))
		{
			stringKey=strkey;
		}
		Cipher cipher;

		try {
			
			cipher = Cipher.getInstance("DES");
			Key key=getKey(stringKey);
			cipher.init(Cipher.ENCRYPT_MODE, key);

			byteFina = cipher.doFinal(byteS);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

			cipher = null;

		}

		return byteFina;

	}

	/**
	 * 
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 *
	 * 
	 * @param byteD
	 * 
	 * @return
	 */
	private static byte[] getDesCode(byte[] byteD,String strKey) {

		Cipher cipher;
		String stringKey="@ai8!lk5";
		if(!"".equals(strKey))
		{
			stringKey=strKey;
		}

		byte[] byteFina = null;

		try {

			cipher = Cipher.getInstance("DES");
			Key key=getKey(stringKey);
			cipher.init(Cipher.DECRYPT_MODE, key);

			byteFina = cipher.doFinal(byteD);

		} catch (Exception e) {

			e.printStackTrace();

		} finally {

			cipher = null;

		}

		return byteFina;

	}

	/**
	 * 
	 * 二行制转字符串
	 * @param b
	 * 
	 * @return
	 */

	public static String byte2hex(byte[] b) { // 一个字节的数，

		// 转成16进制字符串

		String hs = "";

		String stmp = "";

		for (int n = 0; n < b.length; n++) {

			// 整数转成十六进制表示

			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));

			if (stmp.length() == 1)

				hs = hs + "0" + stmp;

			else

				hs = hs + stmp;

		}

		return hs.toUpperCase(); // 转成大写

	}

	public static byte[] hex2byte(byte[] b) {

		if ((b.length % 2) != 0)

			throw new IllegalArgumentException("长度不是偶数");

		byte[] b2 = new byte[b.length / 2];

		for (int n = 0; n < b.length; n += 2) {

			String item = new String(b, n, 2);

			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个进制字节

			b2[n / 2] = (byte) Integer.parseInt(item, 16);

		}

		return b2;

	}
	
	
	/**
	 * 加密秘钥
	 */
	private static final String desKey="@ai8!lk5";
	/**
	 * 解密 (可用)
	 * @param message 需要加密的字符串
	 * @param key 加密秘钥，为空时用默认秘钥
	 * @return
	 * @throws Exception 
	 */
	public static String decrypt(String message, String key) {
		if("".equals(key))
			key=desKey;
		try{
			String encoding = "utf-8";
			byte[] bytesrc = convertHexString(message);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(encoding));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes(encoding));

			cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
			byte[] retByte = cipher.doFinal(bytesrc);
			return new String(retByte,encoding);
			
		}catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
		
	}

	/**
	 * 加密 (可用)
	 * @param message 需要加密的字符串
	 * @param key 加密秘钥，为空时用默认秘钥
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String message, String key) {
		if("".equals(key))
			key=desKey;
		String encoding = "utf-8";
		try{
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes(encoding));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes(encoding));
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

			byte[] buf = cipher.doFinal(message.getBytes(encoding));
			String a = toHexString(buf).toUpperCase();
			return a;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "";
		}
		
	}
	/**
	 * 字符串转换为16进制数组
	 * @param ss
	 * @return
	 */
	private static byte[] convertHexString(String ss) {
		byte digest[] = new byte[ss.length() / 2];
		for (int i = 0; i < digest.length; i++) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}
	

	/**
	 * 16进制数组转换为字符串
	 * @param b
	 * @return
	 */
	private static String toHexString(byte b[]) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String plainText = Integer.toHexString(0xff & b[i]);
			if (plainText.length() < 2)
				plainText = "0" + plainText;
			hexString.append(plainText);
		}
		return hexString.toString();
	}
	
}
