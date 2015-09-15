package com.nifeng.uitl.cipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.Key;

/** 
 * AES Coder<br/> 
 * secret key length:   128bit, default:    128 bit<br/> 
 */  
public class AESCoder { 
	
	private static Logger logger = LoggerFactory.getLogger(AESCoder.class);
      
    //密钥算法
    private static final String KEY_ALGORITHM = "AES";
    //加密和解密过程中的编码
    private static final String ENCODEING = "UTF-8";
    //加密算法/工作模式/填充方式 
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";  
      
    /** 
     * 生成密钥 
     *  
     * @param key   密钥 
     * @return 加密密钥 
     * @throws UnsupportedEncodingException 
     */  
    private static Key getKey(String key) throws UnsupportedEncodingException{  
        return new SecretKeySpec(key.getBytes(ENCODEING), KEY_ALGORITHM);  
    }  
      
    /** 
     * 加密 
     *  
     * @param data  待加密数据 
     * @param encryptKey   密钥
     * @return 加密数据 
     */  
    public static String encrypt(String data, String encryptKey){  
    	String result = "";
    	try{
    		//生成加密key
    		Key key = getKey(encryptKey);
            //实例化  
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);  
            //使用密钥初始化，设置为加密模式  
            cipher.init(Cipher.ENCRYPT_MODE, key); 
            //获取加密结果
            result = Hex.encodeHexStr(cipher.doFinal(data.getBytes(ENCODEING)));
    	}catch (Exception e) {
    		logger.info("[encrypt] occur Exception data="+data, e);
		}
        return result;  
    }  
      
    /** 
     * 解密 
     *  
     * @param data  待解密数据 
     * @param encryptKey   密钥
     * @return byte[]   解密数据
     * @throws Exception 
     */  
    public static String decrypt(String data, String encryptKey) throws Exception{
    	String result = "";
    	try{
		    //实例化  
		    Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);  
		    //使用密钥初始化，设置为解密模式  
		    Key key = getKey(encryptKey);
		    cipher.init(Cipher.DECRYPT_MODE, key);  
		    //获取解密结果  
		    result = new String(cipher.doFinal(Hex.decodeHex(data.toCharArray())), ENCODEING);
    	}catch (Exception e) {
    		logger.info("[decrypt] occur Exception data="+data, e);
		}
        return result; 
    }  
      
    public static void main(String[] args) throws Exception {  
    	String key = "jfiawo849wfjaffw";
    	String data = "2604000";
    	System.out.println("加密前的字串是：" + data);
    	String enString = encrypt(data, key);
		System.out.println("加密后的字串是：" + enString);
		String DeString = decrypt(enString, key);
		System.out.println("解密后的字串是：" + DeString);
        System.out.println(System.currentTimeMillis());
    }  
}  
