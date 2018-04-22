package cn.wx.simulation;

import java.awt.Point;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;

import javax.crypto.SecretKey;

import com.alibaba.fastjson.JSON;

import cn.wx.entiry.ClientMessageProcessor;
import cn.wx.entiry.Message;
import cn.wx.entiry.MessageTransfered;
import cn.wx.entiry.User;
import cn.wx.security.ClientKeyManager;
import cn.wx.util.FileOperation;
import cn.wx.util.Serializor;
public class ClientCenter {

	public static void main(String[] args) throws Exception{
		String serPublicKeyPath="D:/KeyCenter/serverPublicKey";
		//生成客户端需要的密钥
		PublicKey clientPublicKey=Serializor.deSerialize(serPublicKeyPath, PublicKey.class);
		KeyPair cKeyPair=ClientKeyManager.genKeyPair();
		SecretKey secretKey=ClientKeyManager.geneAESKey();
		ClientKeyManager clientKeyManager=new ClientKeyManager(secretKey, cKeyPair, clientPublicKey);
		//生成用户
		User user=new User("王翔", "123456", 24, true, "13862132540", new Point(0, 0));
		//生成交通数据
		ArrayList<Message> messages=new ArrayList<>();
		Message message=new Message(System.currentTimeMillis(), new Point(0, 0), Message.LOGIN);
		Message message2=new Message(System.currentTimeMillis(), new Point(2, 2), Message.SIGN);
		message2.setValue("禁止停车标志".getBytes());
		messages.add(message);
		messages.add(message2);
		
		//数据加密处理
		ClientMessageProcessor processor=new ClientMessageProcessor(user, messages, clientKeyManager);
		MessageTransfered messageTransfered=processor.doFinal();
		String json=JSON.toJSONString(messageTransfered);
		FileOperation.writeFile("D:/Messsage/message"+user.getUsername()+System.currentTimeMillis(), json.getBytes());
		System.out.println("finish");
	}

}
