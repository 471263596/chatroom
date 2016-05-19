package com.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestSockServer {
	int i;
	static List<Client> clientlist=new ArrayList<Client>();
	public static void main(String[] args) {
		Socket socket = null;
		try {
			@SuppressWarnings("resource")
			ServerSocket ss = new ServerSocket(5555);
			System.out.println("����������");
			while (true) {
				socket = ss.accept();
				TestSockServer tss = new TestSockServer();
				Client client = tss.new Client(socket);
				clientlist.add(client);
				client.start();
				// new TestSockServer().new Client(socket).start();
			    
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			System.out.println("server over");
		}

	}

	class Client extends Thread {
		Socket socket = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;

		public Client(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			try {
				while (true) {
					dos = new DataOutputStream(socket.getOutputStream());
					dis = new DataInputStream(socket.getInputStream());
					String str = null;
					if ((str = dis.readUTF()) != null) {
						System.out.println("�ͻ���˵��" + str);
					}
					//dos.writeUTF(socket.getPort()+"��������Hm..." + str);
					for (int i = 0; i < clientlist.size(); i++) {
				    	new DataOutputStream(clientlist.get(i).socket.getOutputStream()).writeUTF(socket.getPort()+"�ͻ���˵��: "+str);;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				System.out.println("server over");
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
}
