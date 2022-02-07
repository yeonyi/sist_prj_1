package project1;

import java.io.IOException;

/**
 * main method arguments �ޱ�
 * @author user
 */
public class UserLogString {

	public static void main(String[] args) {

		try {
			UserLog ul = new UserLog();
			
			int fileLineLength = Integer.valueOf(ul.getFileString().size()); //���Ͼ��� ���� ����
			int args0 = Integer.valueOf(args[0]); 
			int args1 = Integer.valueOf(args[1]);

			if (args[0] != null && args[1] != null) {
				if (args0 > 0 && args1 < fileLineLength+1) {// text �� ������ ����� �ʴ´ٸ�
					ul.setStartLine(Integer.parseInt(args[0])); // ���۶��� : runConfig���� ���� �� �� �־��ּž� �������� �ʽ��ϴ�
					ul.setEndLine(Integer.parseInt(args[1]));
				}else if(ul.getFile().exists()!=true) { //������ �������� �ʰų� ��ҵǾ��� ��
					System.out.println("���� ����");
				}else {
					System.err.println("���� �� ���� ���� �ƴմϴ�.");
				}
				// end else
			} // end if
			
		} catch (IOException e) {
			e.printStackTrace();
		} // end catch

	}// main

}// class
