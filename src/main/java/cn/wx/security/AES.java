package cn.wx.security;

import java.awt.Point;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.alibaba.fastjson.JSON;

import cn.wx.entiry.User;
import cn.wx.util.Serializor;

public class AES {
	private SecretKey secretKey;
	public AES(SecretKey secretKey) {
		this.secretKey=secretKey;
	}
	/**
	 * AES加密
	 * @param content 需要加密的字节数组
	 * @return 加密后的字节数组
	 */
	public byte[] encrypt(byte[] content) throws Exception{
		Cipher cipher;
		byte[] result = null;
		cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
		result=cipher.doFinal(content);//不使用update（不是一次全部解密）
		return result;
	}
	
	/**
	 * AES解密
	 * @param content
	 * @return 解密后的字节数组
	 */
	public byte[] decrypt(byte[] content) throws Exception {
		Cipher cipher;
		byte[] result=null;
		cipher=Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		result=cipher.doFinal(content);
		return result;
	}
	 public static void main(String[] args) throws Exception {
		  User user=new User("wangxiang", "23344", 13, true, "13862132540", new Point(2, 2));
		  String msg=JSON.toJSONString(user);
		  System.out.println("明文："+msg);
	  	  //生成AES密钥
		  System.out.println("生成AES密钥。。。。。。");
		  SecretKey key=ClientKeyManager.geneAESKey();
		  //生成AES加密器
		  System.out.println("生成AES加密器。。。。。。");
		  AES encrypt=new AES(key);
		  //AES加密
		  System.out.println("正在加密数据。。。。。\n");
		  byte[] cmsg=encrypt.encrypt(msg.getBytes());
		  System.out.println("密文："+new String(cmsg)+"\n");
		  //序列化密钥
		  System.out.println("序列化密钥。。。。。");
		  byte[] mkey=Serializor.serialzeTobytes(key);
		
		  System.out.println("传输密钥。。。");
		  //反序列化密钥
		  System.out.println("反序列化密钥。。。。。");//json化密钥后，无法反json，内部key和父类的一些信息无法被json，也就无法反json,使用序列化
		  SecretKey key1=Serializor.deSerialize(mkey, SecretKey.class);
		  //生成AES解密器
		  System.out.println("生成AES解密器。。。。");
		  AES decrypt=new AES(key1);
		  //解密
		  System.out.println("解密。。。。。");
		  byte[]mmsg=decrypt.decrypt(cmsg);
//		  System.out.println(key.equals(key1));
		  System.out.println("解密结果："+new String(mmsg));
		 
		
	}
}
