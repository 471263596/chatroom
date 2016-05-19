package com.chatroom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class TestSockClient {
	public static void main(String[] args) {
		// Socket socket = null;
		// DataInputStream dis=null;
		// DataOutputStream dos=null;
		// try {
		// socket = new Socket("127.0.0.1",5555);
		// String str=null;
		// do{
		// dis=new DataInputStream(socket.getInputStream());
		// dos=new DataOutputStream(socket.getOutputStream());
		// @SuppressWarnings("resource")
		// Scanner input=new Scanner(System.in);
		// str=input.nextLine();
		// dos.writeUTF(str);//客户端向服务端发送请求
		// String s=null;
		// if ((s=dis.readUTF())!=null) {//读取客户端回应
		// System.out.println(s);
		// }
		// }while(!str.equals("88"));
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// System.out.println("client over");
		// }catch(Exception e){
		// e.printStackTrace();
		// }finally{
		// try {
		// socket.close();
		// dos.close();
		// dis.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		try {
			// 创建socket对象，指定服务器的ip地址，和服务器监听的端口号
			// 客户端在new的时候，就发出了连接请求，服务器端就会进行处理，如果服务器端没有开启服务，那么
			// 这时候就会找不到服务器，并同时抛出异常==》java.net.ConnectException: Connection
			// refused: connect
			Socket s1 = new Socket("127.0.0.1", 8888);
			// =========客户端，在这里应该先打开输入流，在打开输出流，
			// =========因为客户端执行的操作是先说，再听，说，听，说，听.....
			// 打开输入流
			InputStream is = s1.getInputStream();
			// 封装输入流
			DataInputStream dis = new DataInputStream(is);
			// 打开输出流
			OutputStream os = s1.getOutputStream();
			// 封装输出流
			DataOutputStream dos = new DataOutputStream(os);
			// 创建并启用两个线程
			new MyClientReader(dis).start();
			new MyClientWriter(dos).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// 接受并打印服务器端传过来的信息
class MyClientReader extends Thread {
	private DataInputStream dis;

	public MyClientReader(DataInputStream dis) {
		this.dis = dis;
	}

	public void run() {
		String info;
		try {
			while (true) {
				info = dis.readUTF();
				System.out.println("对方说: " + info);
				if (info.equals("bye")) {
					System.out.println("对方下线，程序退出!");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// 从键盘获得输入流并写入信息到服务器端
class MyClientWriter extends Thread {
	private DataOutputStream dos;

	public MyClientWriter(DataOutputStream dos) {
		this.dos = dos;
	}

	public void run() {
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		String info;
		try {
			while (true) {
				info = br.readLine();
				dos.writeUTF(info);
				if (info.equals("bye")) {
					System.out.println("自己下线，程序退出!");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
