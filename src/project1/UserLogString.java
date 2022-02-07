package project1;

import java.io.IOException;

public class UserLogString {
	
	public static void main(String[] args) {
		UserLog ul = new UserLog();
		int startLine = Integer.parseInt(args[0]); //���۶��� : runConfig���� ���� �� �� �־��ּž� �������� �ʽ��ϴ�
		int endLine = Integer.parseInt(args[1]); //������
		try {
			ul.readFile(startLine, endLine);
		} catch (IOException e) {
			e.printStackTrace();
		} // end catch
		ul.dispose(); //�߿�!!

		//��� String���� ��ȯ�մϴ�. 
		//1. �ִٻ�� Ű�� Ƚ��
		System.out.println(ul.printKey());
		//2.�������� ���� Ƚ��
		System.out.println(ul.printBrowser()); 
		//3.http��û �߻� Ƚ��, ����
		System.out.println(ul.printHttpSucFail());
		//4.��û�� ���� ���� �ð�
		System.out.println(ul.printReqTime());
		//5.�����ڵ�403
		System.out.println(ul.printErrCode403());
		//6.�����ڵ�500
		System.out.println(ul.printErrCode500());

	}// main

}
