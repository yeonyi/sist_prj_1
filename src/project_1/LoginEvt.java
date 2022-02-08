package project_1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class LoginEvt extends WindowAdapter implements ActionListener {
	private Login login;
	private String[] args;


	
	public LoginEvt(Login login,String[] args) {
		this.login = login;
		this.args = args;
		
	}


	//X������ dispose() ��Ű��
	@Override
	public void windowClosing(WindowEvent e) {
		login.dispose();
	}
	
	//1.jbtLogin ������ 2.jtfId,jpfPw �ҷ��ͼ� 3.Authority�� �α������� �����ϱ�
	@Override
	public void actionPerformed(ActionEvent e) {
		String id = login.getJtfId().getText();
		String pw = String.valueOf(login.getJpfPw().getPassword());
		
		if(new Authority().loginAuthenticate(id, pw)) {
			login.setVisible(false);
			new UserLogString(args,login);
		} else {
			System.out.println("Not Valid ID or Password");
		}
		
	}

}
