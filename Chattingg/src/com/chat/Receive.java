package com.chat;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 接收线程
 * @author as Chenqingling
 *
 *下午7:38:16
 */
public class Receive implements Runnable{
	
	//输入流
	private DataInputStream dis;
	//线程标识
	private boolean isRunning = true;
	
	public Receive(){
		
	}
	
	public Receive(Socket client) throws IOException{
		try {
			dis=new DataInputStream(client.getInputStream());
		} catch (IOException e) {
//			e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
	}

	public String receive() throws IOException{
		String msg = "" ;
		
		try {
			msg = dis.readUTF();
		} catch (IOException e) {
//			e.printStackTrace();
			isRunning = false;
			CloseUtil.closeAll(dis);
		}
		return msg ;
	}
	
	@Override
	public void run() {
		while(isRunning){
			try {
				System.out.println(receive());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
