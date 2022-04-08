package com.hhoss.shell;

import com.hhoss.util.crypto.DESCoder;


public class PropEncrypt {
	public static final String DESCODER_KEY = "ENCRYPT@ZHENGKEJUN";

	public static String decrypt(String enc){
		try {
			return DESCoder.decrypt(enc, DESCODER_KEY);
		} catch (Exception e) {
			System.out.println("解密属性值出错，密文为："	+ enc);
		}
		return "";

	}
	
	public static String encrypt(String src) throws Exception{
		return DESCoder.encrypt(src, DESCODER_KEY);
	}

	public static void main(String[] args) {
		try {
			String str = encrypt(args[0]);
			System.out.println("密文:\t" + str);
			System.out.println("解密:\t" + decrypt(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
