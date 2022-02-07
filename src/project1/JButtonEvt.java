package project1;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * ��ư �̺�Ʈ ó�� Ŭ����
 * @author user
 */
public class JButtonEvt implements ActionListener {

	private UserLog ul;

	Authority a = new Authority();
	
	public JButtonEvt(UserLog ul) {
		this.ul = ul;
	}// ButtonEvt

	@Override
	public void actionPerformed(ActionEvent ae)  {

		// view ��ư �̺�Ʈ ó��
		if (ae.getSource() == ul.getViewJbtn()) {

			try {
				new JButtonEvt(ul).creatDialog();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} // end if

		// report ��ư �̺�Ʈ ó��
		if (ae.getSource() == ul.getReportJbtn()) {
			
			try {
				new JButtonEvt(ul).creatFile();
			} catch (IOException e) {
				e.printStackTrace();
			}//end catch
			
		}//end if

	}// actionPerformed

	
	/**
	 * view��ư�� Dialog�� �����ϴ� �޼ҵ�
	 * @throws IOException
	 */
	public void creatDialog() throws IOException {
		
		//Dialog ����
		JDialog jd = new JDialog(ul, "view", true);
		
		//1~7�� ��� ���� Dialog�� textArea ����
		JTextArea jta =  new JTextArea(ul.printFileAnalysis()); ;
		JScrollPane jsp = new JScrollPane(jta);

		//TextArea �� ��Ʈ ����
		jta.setFont(new Font("���� ���", Font.BOLD, 15));
		jd.add(jsp);

		jd.setSize(500, 500);
		jd.setVisible(true);

		jd.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void creatFile() throws IOException {
		
		//���� ����
		File dir_C = new File("c:/dev/report");
		dir_C.mkdirs();

		Date date = new Date();
		String longDate = Long.toString(date.getTime()); //������¥

		
		//���� ����
		FileWriter fw = new FileWriter(new File(dir_C, "report_" + longDate + ".dat"));
		fw.write(ul.printReport());
		fw.flush();
		fw.close();
		
	} // creatFile
	
}// class
