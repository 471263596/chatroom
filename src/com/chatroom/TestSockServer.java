package com.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestSockServer {
	List<Client> clientList = new ArrayList<Client>();// 不用static，内部类和其他类并排

	public void init() {
		ServerSocket server = null;
		Socket socket = null;
		try {
			// 打开端口，等待服务器连接。
			server = new ServerSocket(8888);
			System.out.println("服务器已开启！");
			// 服务端循环监听，若是有客户端连接，服务端接受并且创建一个线程，将连接通道socket放入该线程中，并启动线程。
			while (true) {
				socket = server.accept();
				Client c = new Client(socket);// **************
				clientList.add(c);// 将新连接的客户端存入list中，以便于后面遍历发送信息。
				c.start();
				updateUserList();// 发送用户列表信息userlist
			}

		} catch (IOException e) {
			System.out.println("server over!");
			// e.printStackTrace();
		} finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		TestSockServer ts = new TestSockServer();// **************只实例化一次，clientList是共享的，不用static
		ts.init();
	}

	// 字符串规则,aa,bb,cc,dd,
	public String getNameStr() {
		String str = ",";
		for (int i = 0; i < clientList.size(); i++) {
			str += clientList.get(i).name + ",";
			System.out.println(str);
		}
		return str;
	}

	public void updateUserList() {
		String userStr = getNameStr();
		// 向所有客户端发送更新后的用户列表
		for (int i = 0; i < clientList.size(); i++) {
			try {
				new DataOutputStream(clientList.get(i).socket.getOutputStream()).writeUTF(userStr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// 因为需要启用多个线程来完成客户端的连接，所以这里使用内部类。
	class Client extends Thread {
		Socket socket;
		String name;
		DataOutputStream dos;
		DataInputStream dis;

		// 使用带参数的构造方法来将通道放入线程中。
		public Client(Socket socket) {
			this.socket = socket;
			name = "用户" + socket.getPort();// 初始值
		}

		@Override
		public void run() {
			String str = null;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				// 循环遍历用来接收某个客户端传来的信息，并且将这些信息遍历发送给各个客户端。
				while (true) {
					if ((str = dis.readUTF()) != null) {
						// 判断数据格式，如果#开始，更新name属性，否则循环输出信息
						if (str.startsWith("#")) {
							name = str.substring(1);
							updateUserList();
						}
						// dos.writeUTF(socket.getPort() + "say:" + str);
						// 循环遍历将收到的此客户端信息发送给所有客户端。
						else {
							for (int i = 0; i < clientList.size(); i++) {
								// 给每个通道都发一次
								new DataOutputStream(clientList.get(i).socket.getOutputStream())
										.writeUTF(name + ": " + str);
								// System.out.println(clientList.get(i).name +
								// ": " + str);//2个服务端收到的是他们自身的名字+自己或别人的str
								// System.out.println(name + ": " +
								// str);//2个服务端收到的是说这句str的服务器的名字+对应的str
							}
						}

					}
				}
			} catch (IOException e) {
				System.out.println("客户端" + socket.getPort() + "退出");
				clientList.remove(this);
				updateUserList();
				// e.printStackTrace();
			} finally {
				// 关闭各种流
				try {
					dis.close();
					dos.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
	}
}
