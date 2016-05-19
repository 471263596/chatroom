package com.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TestSockClient {
	static DataInputStream dis = null;
	public static void main(String[] args) {
		Socket socket = null;
		DataOutputStream dos = null;
		try {
			socket = new Socket("127.0.0.1", 5555);
			String str = null;
			do {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
				@SuppressWarnings("resource")
				Scanner input = new Scanner(System.in);
				new Thread(){//匿名内部类
					@Override
					public void run(){
						String s = null;
						try {
							if ((s = dis.readUTF()) != null) {// 读取客户端回应
								System.out.println(s);
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}.start();;
				str = input.nextLine();
				dos.writeUTF(str);// 客户端向服务端发送请求
			} while (!str.equals("88"));
			System.out.println("client over");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				socket.close();
				dos.close();
				dis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
