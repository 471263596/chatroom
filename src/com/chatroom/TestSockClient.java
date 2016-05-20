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
			// �ͻ������ӷ�����
			socket = new Socket("127.0.0.1", 8888);
			System.out.println("�ͻ��������ӣ�");
			dos = new DataOutputStream(socket.getOutputStream());
			dis = new DataInputStream(socket.getInputStream());
			// �����ӳɹ��������߳�ʵ��ѭ�����շ���˷��͵���Ϣ�Ĺ��ܡ�
			// ��Ϊֻ��Ҫ����һ���߳̾Ϳ����������շ�������Ϣ�����������������ڲ��ࡣ
			new Thread() {
				@Override
				public void run() {
					String s = null;
					try {
						while (true) {
							//flag   while��������ִ�м���
							//�ȴ���������״̬
							if ((s = dis.readUTF()) != null)
								System.out.println(s);// ��ȡ�ͻ��˻�Ӧ
						}
					} catch (IOException e) {
						//e.printStackTrace();//flag����ȡ��  �����ע�͵� ��ν���while������ѭ��
					}
				}
			}.start();
			// �����߳���ʵ�ִӿ���̨�������ݲ��ҷ��͵��������Ĺ��ܡ�
			do {
				str = input.next();
				dos.writeUTF(str);// �ͻ��������˷�������
			} while (!str.equals("88"));
			//flag = false;
			System.out.println("client over!");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// �ر�����
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
