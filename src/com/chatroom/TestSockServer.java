package com.chatroom;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestSockServer {
	public static void main(String[] args) {
		// Socket socket=null;
		// DataOutputStream dos=null;
		// DataInputStream dis=null;
		// try {
		// @SuppressWarnings("resource")
		// ServerSocket ss = new ServerSocket(5555);
		// System.out.println("����������");
		// socket=ss.accept();
		// while(true){
		// dos=new DataOutputStream(socket.getOutputStream());
		// dis=new DataInputStream(socket.getInputStream());
		// String str=null;
		// if((str=dis.readUTF())!=null){
		// System.out.println("�ͻ���˵��"+str);
		// }
		// dos.writeUTF("��������Hm..."+str);
		// }
		//
		//
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// //e.printStackTrace();
		// System.out.println("server over");
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
		// ����һ��socket����
		try {
			ServerSocket s = new ServerSocket(8888);
			// socket�������accept�������ȴ���������
			Socket s1 = s.accept();
			// =========�������ˣ�������Ӧ���ȴ���������ڴ���������
			// =========��Ϊ��������ִ�еĲ�������������˵������˵������˵.....
			// �������
			OutputStream os = s1.getOutputStream();
			// ��װ�����
			DataOutputStream dos = new DataOutputStream(os);
			// ��������
			InputStream is = s1.getInputStream();
			// ��װ������
			DataInputStream dis = new DataInputStream(is);
			// ���������������߳�
			new MyServerReader(dis).start();
			new MyServerWriter(dos).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// ���ܲ���ӡ�ͻ��˴���������Ϣ
class MyServerReader extends Thread {
	private DataInputStream dis;

	public MyServerReader(DataInputStream dis) {
		this.dis = dis;
	}

	public void run() {
		String info;
		try {
			while (true) {
				// ����Է������ͻ���û��˵������ô�ͻ����������
				// ���������������Ӱ�쵽�����߳�
				info = dis.readUTF();
				// ���״̬��������Ϊ����������ô�ʹ�ӡ���ܵ�����Ϣ
				System.out.println("�Է�˵: " + info);
				if (info.equals("bye")) {
					System.out.println("�Է����ߣ������˳�!");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// �Ӽ��̻����������д����Ϣ���ͻ���
class MyServerWriter extends Thread {
	private DataOutputStream dos;

	public MyServerWriter(DataOutputStream dos) {
		this.dos = dos;
	}

	public void run() {
		// ��ȡ����������
		InputStreamReader isr = new InputStreamReader(System.in);
		// ��װ����������
		BufferedReader br = new BufferedReader(isr);
		String info;
		try {
			while (true) {
				info = br.readLine();
				dos.writeUTF(info);
				if (info.equals("bye")) {
					System.out.println("�Լ����ߣ������˳�!");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
