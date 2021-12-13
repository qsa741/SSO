package com.jyPage.common.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES256 {

	private String key = "junyoung.yang@myvtw.co.kr1234567"; // 32byte

	public static String alg = "AES/CBC/PKCS5Padding";

	private final String iv = key.substring(0, 16);

	// 암호화
	public String encrypt(String text) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
		SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

		byte[] encrypted = cipher.doFinal(text.getBytes("UTF-8"));

		return Base64.getEncoder().encodeToString(encrypted);
	}

	// 복호화
	public String decrypt(String text) throws Exception {
		Cipher cipher = Cipher.getInstance(alg);
		SecretKeySpec keySpec = new SecretKeySpec(iv.getBytes(), "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

		byte[] decodeBytes = Base64.getDecoder().decode(text);
		byte[] decrypted = cipher.doFinal(decodeBytes);

		return new String(decrypted, "UTF-8");

	}

}
