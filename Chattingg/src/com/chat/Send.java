package com.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.zip.InflaterInputStream;

/**
 * 发送线程
 * @author as Chenqingling
 *
 *下午7:37:24
 */
public class Send implements Runnable{
	
	//输入流
	private BufferedReader console;
	//输出流
	private DataOutputStream dos;
	//控制线程标识
	boolean isRunning = true ;
	//名称
	private String name ;
	
	public Send(){
		console = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public Send(Socket client,String name) throws IOException{
		this();
		try {
			dos = new DataOutputStream(client.getOutputStream());
			this.name = name ;
			send(this.name);
		} catch (IOException e) {
//			e.printStackTrace();
			isRunning = false ;
			CloseUtil.closeAll(dos,console);
		}
	}
	
	/**
	 * 接收数据
	 * @return
	 */
	private String getMessage(){
		
		try {
			return console.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
	 * 1.接收数据
	 * 2.发送数据
	 * @throws IOException 
	 */
	public void send(String msg) throws IOException{
		if(null != msg && !msg.equals("")){
			try {
				dos.writeUTF(msg);
				//强制刷新
				dos.flush();
			} catch (IOException e) {
//				e.printStackTrace();
				isRunning = false ;
				CloseUtil.closeAll(dos,console);
			}
		}
	}

	@Override
	public void run() {
		while(isRunning){
			try {
				send(getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
