package com.chat;

import java.io.Closeable;
import java.io.IOException;

/**
 * 关闭流方法
 * @author as Chenqingling
 *
 *下午7:45:50
 */
public class CloseUtil {
	public static void closeAll(Closeable... io) throws IOException{
		for(Closeable temp:io){
			if(null != temp){
				temp.close();
			}
		}
	}
}
