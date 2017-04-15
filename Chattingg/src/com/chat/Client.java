package com.chat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 创建客户端：发送数据加接收数据
 * 发送数据：输出流
 * 读取数据：输入流
 * @author as Chenqingling
 *
 *下午7:39:02
 */
public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("请输入名称");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String name = br.readLine();
		if(name.equals("")){
			return ;
		}
//		if(!name.equals("")&&name.length()<=10){
//			
//		}
		
		Socket client = new Socket("localhost",8888);
		new Thread(new Send(client,name)).start();
		new Thread(new Receive(client)).start();
		
	}
}
