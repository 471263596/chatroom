package com.chatroom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TestSockServer {
	List<Client> clientList = new ArrayList<Client>();// ����static���ڲ���������ಢ��

	public void init() {
		ServerSocket server = null;
		Socket socket = null;
		try {
			// �򿪶˿ڣ��ȴ����������ӡ�
			server = new ServerSocket(8888);
			System.out.println("�������ѿ�����");
			// �����ѭ�������������пͻ������ӣ�����˽��ܲ��Ҵ���һ���̣߳�������ͨ��socket������߳��У��������̡߳�
			while (true) {
				socket = server.accept();
				Client c = new Client(socket);// **************
				clientList.add(c);// �������ӵĿͻ��˴���list�У��Ա��ں������������Ϣ��
				c.start();
				updateUserList();// �����û��б���Ϣuserlist
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
		TestSockServer ts = new TestSockServer();// **************ֻʵ����һ�Σ�clientList�ǹ���ģ�����static
		ts.init();
	}

	// �ַ�������,aa,bb,cc,dd,
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
		// �����пͻ��˷��͸��º���û��б�
		for (int i = 0; i < clientList.size(); i++) {
			try {
				new DataOutputStream(clientList.get(i).socket.getOutputStream()).writeUTF(userStr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	// ��Ϊ��Ҫ���ö���߳�����ɿͻ��˵����ӣ���������ʹ���ڲ��ࡣ
	class Client extends Thread {
		Socket socket;
		String name;
		DataOutputStream dos;
		DataInputStream dis;

		// ʹ�ô������Ĺ��췽������ͨ�������߳��С�
		public Client(Socket socket) {
			this.socket = socket;
			name = "�û�" + socket.getPort();// ��ʼֵ
		}

		@Override
		public void run() {
			String str = null;
			try {
				dos = new DataOutputStream(socket.getOutputStream());
				dis = new DataInputStream(socket.getInputStream());
				// ѭ��������������ĳ���ͻ��˴�������Ϣ�����ҽ���Щ��Ϣ�������͸������ͻ��ˡ�
				while (true) {
					if ((str = dis.readUTF()) != null) {
						// �ж����ݸ�ʽ�����#��ʼ������name���ԣ�����ѭ�������Ϣ
						if (str.startsWith("#")) {
							name = str.substring(1);
							updateUserList();
						}
						// dos.writeUTF(socket.getPort() + "say:" + str);
						// ѭ���������յ��Ĵ˿ͻ�����Ϣ���͸����пͻ��ˡ�
						else {
							for (int i = 0; i < clientList.size(); i++) {
								// ��ÿ��ͨ������һ��
								new DataOutputStream(clientList.get(i).socket.getOutputStream())
										.writeUTF(name + ": " + str);
								// System.out.println(clientList.get(i).name +
								// ": " + str);//2��������յ������������������+�Լ�����˵�str
								// System.out.println(name + ": " +
								// str);//2��������յ�����˵���str�ķ�����������+��Ӧ��str
							}
						}

					}
				}
			} catch (IOException e) {
				System.out.println("�ͻ���" + socket.getPort() + "�˳�");
				clientList.remove(this);
				updateUserList();
				// e.printStackTrace();
			} finally {
				// �رո�����
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
