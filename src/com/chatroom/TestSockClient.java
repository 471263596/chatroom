package com.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TestSockClient {
	private DataInputStream dis;
	private DataOutputStream dos;
	private Socket socket;
	//static boolean flag = true;

	@SuppressWarnings("resource")
	public void init(){
		String str = null;
		Scanner input = new Scanner(System.in);
		try {
			// 客户端连接服务器
			socket = new Socket("127.0.0.1", 8888);
			System.out.println("客户端已连接！");
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			// 当连接成功后开启子线程实现循环接收服务端发送的信息的功能。
			// 因为只需要另起一个线程就可以用来接收服务器信息，所以这里用匿名内部类。
			new Thread() {
				@Override
				public void run() {
					String s = null;
					try {
						while (true) {
							//flag   while放外面会多执行几次
							//等待服务器的状态
							if ((s = dis.readUTF()) != null)
								System.out.println(s);// 读取客户端回应
						}
					} catch (IOException e) {
						//e.printStackTrace();//flag可以取消  把这句注释掉 多次进入while，跳出循环
					}
				}
			}.start();
			// 在主线程中实现从控制台接收数据并且发送到服务器的功能。
			do {
				str = input.next();
				dos.writeUTF(str);// 客户端向服务端发送请求
			} while (!str.equals("88"));
			//flag = false;
			System.out.println("client over!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接
			try {
				dis.close();
				dos.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
	
	
	public static void main(String[] args) {
		TestSockClient tc = new TestSockClient();
		tc.init();
	}
}
