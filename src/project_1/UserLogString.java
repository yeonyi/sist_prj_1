package project_1;

import java.io.IOException;

/**
 * Login Class�� main method �̵� (0208)
 * login ��ü �ޱ����� �߰� (0208)
 * @author user
 */
public class UserLogString {
	private Login login; //0208

	public UserLogString(String[] args,Login login ) { //0208
		this.login = login; //0208
		
		try {
			UserLog ul = new UserLog(login); //0208
			
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
	}

}// class
