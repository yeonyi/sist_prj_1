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
	private JTextField  txtId, txtPw;
	private JButton logBtn;
	
	private UserRepository ur = UserRepository.getInstance();
	private User user = null;
	
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
		String id = user.getId();
		String pw = user.getPassword();
		
		if(ae.getSource()==logBtn) {
			if(id.equals(txtId) && pw.equals(txtPw)) {
				JOptionPane.showMessageDialog(null, "로그인 성공");
				}//end if
			}else{
				JOptionPane.showMessageDialog(null, "로그인 실패");
			}//end else
			
	}//actionPerformed


	public static void main(String[] args) {
		
		new LoginTest();

	}//main

}//class
