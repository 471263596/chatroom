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
		// dos.writeUTF(str);//�ͻ��������˷�������
		// String s=null;
		// if ((s=dis.readUTF())!=null) {//��ȡ�ͻ��˻�Ӧ
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
			// ����socket����ָ����������ip��ַ���ͷ����������Ķ˿ں�
			// �ͻ�����new��ʱ�򣬾ͷ������������󣬷������˾ͻ���д��������������û�п���������ô
			// ��ʱ��ͻ��Ҳ�������������ͬʱ�׳��쳣==��java.net.ConnectException: Connection
			// refused: connect
			Socket s1 = new Socket("127.0.0.1", 8888);
			// =========�ͻ��ˣ�������Ӧ���ȴ����������ڴ��������
			// =========��Ϊ�ͻ���ִ�еĲ�������˵��������˵������˵����.....
			// ��������
			InputStream is = s1.getInputStream();
			// ��װ������
			DataInputStream dis = new DataInputStream(is);
			// �������
			OutputStream os = s1.getOutputStream();
			// ��װ�����
			DataOutputStream dos = new DataOutputStream(os);
			// ���������������߳�
			new MyClientReader(dis).start();
			new MyClientWriter(dos).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

// ���ܲ���ӡ�������˴���������Ϣ
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

// �Ӽ��̻����������д����Ϣ����������
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
					System.out.println("�Լ����ߣ������˳�!");
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
