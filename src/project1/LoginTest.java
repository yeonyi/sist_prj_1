package project1;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginTest extends JFrame implements ActionListener{
	
	private JLabel jblLogin, jlbPassword;
	private JTextField  txtId;
	private JPasswordField txtPw;
	private JButton logBtn;
	
	Authority a = new Authority();
	
	public LoginTest() {
	
		super("로그인");
	
		jblLogin = new JLabel("ID : ");
		jlbPassword = new JLabel("PASSWORD : ");
		
		txtId = new JTextField(10); 
		txtPw = new JPasswordField(10);  
		logBtn = new JButton("Log in");
		logBtn.addActionListener(this);
		
		setLayout(new GridLayout(3, 3));
		add(jblLogin);
		add(txtId);
		add(jlbPassword);
		add(txtPw);
		add(logBtn);
		
		setSize(300, 200);
		
		setVisible(true);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}//LoginTest
	
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		String id = txtId.getText();
		String pw = new String(txtPw.getPassword());//getText()로 가져와도 되지만 보안상 getPassword() 의 사용을 권장
		
		if(a.loginAuthenticate(id, pw) == true) {
			JOptionPane.showMessageDialog(logBtn, "로그인 성공");
		}else {
			JOptionPane.showMessageDialog(logBtn, "로그인 실패");
		}//end else	
		
		System.out.println(id);
		System.out.println(pw);
			
	}//actionPerformed

	public static void main(String[] args) {
		
		new LoginTest();

	}//main

}//class
