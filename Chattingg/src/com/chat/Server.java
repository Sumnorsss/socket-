package com.chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建服务器
 * 写数据：输出流
 * 读取数据：输入流吧
 * @author as Chenqingling
 *
 *下午7:39:55
 */
public class Server {
	
	private List<MyChannel> all = new ArrayList<>();
	
	public static void main(String[] args) throws IOException{
		new Server().start();
	}
	
	
	public void start() throws IOException{
		ServerSocket server = new ServerSocket(8888);
		while(true){
			Socket client = server.accept();
			MyChannel channel = new MyChannel(client);
			//一条道路，来一个客户端加一个管道与服务器相连
			//服务端统一管理
			all.add(channel);
			new Thread(channel).start();
		}
	}
	
	
	/**
	 * 内部类
	 * 一个客户端一条道路
	 * 输入流：接收数据
	 * 输出流：发送数据
	 * @author as Chenqingling
	 *
	 */
	private class MyChannel implements Runnable{

		private DataInputStream dis ;
		private DataOutputStream dos ;
		private boolean isRunning = true;
		private String name ;
		
		public MyChannel(Socket client) throws IOException{
			try {
				dis = new DataInputStream(client.getInputStream());
				dos = new DataOutputStream(client.getOutputStream());
				
				this.name = dis.readUTF();
				this.send("欢迎进入聊天室");
				sendOthers(this.name+"进入聊天室",true);
				
				
			} catch (IOException e) {
//				e.printStackTrace();
				CloseUtil.closeAll(dis,dos);
				isRunning = false;
			}
			
		}
		
		
		/**
		 * 接收数据
		 * @return
		 * @throws IOException 
		 */
		private String receive() throws IOException{
			
			String msg = "" ;
			try {
				msg = dis.readUTF();
			} catch (IOException e) {
				CloseUtil.closeAll(dis);
				isRunning = false;
				all.remove(this);//移除自身
			}
			return msg;
			
		}
		
		/**
		 * 发送数据
		 * @param msg
		 * @throws IOException 
		 */
		private void send(String msg) throws IOException{
			if(null == msg || msg.equals("")){
				return;
			}
			try {
				dos.writeUTF(msg);
				dos.flush();
			} catch (IOException e) {
//				e.printStackTrace();
				CloseUtil.closeAll(dos);
				isRunning = false;
				
			}
		}
		
		/**
		 * 将自己的信息发送给服务端，服务端发送给其他客户端
		 * @throws IOException 
		 */
		public void sendOthers(String msg,boolean sys) throws IOException{
			
			System.out.println(msg+"=============");
			//是否为私聊 自己约定
			if(msg.startsWith("@")&&msg.indexOf(":")>-1){
				//获取name
				String name = msg.substring(1, msg.indexOf(":"));
				String  content = msg.substring(msg.indexOf(":")+1);
				for (MyChannel others : all) {
					if(others.name.equals(name)){
						others.send(this.name + "对您悄悄地说：" + content);
					}
				}
			}else{
				
				//遍历列表
				for (MyChannel others : all) {
					//如果发送到自身，跳过
					if(others == this){
						continue;
					}
					
					if(sys){//系统信息
						others.send("系统信息: "+msg);
						
					}
					//发送给其他客户端
					else{
						others.send(this.name+"对所有人说: "+msg);
					}
				}
			}
			
			
		}
		
		@Override
		public void run() {
			while(isRunning){
				try {
					sendOthers(receive(),false);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}
