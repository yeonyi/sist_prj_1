package project_1.View;

import project_1.Event.JButtonEvt;
import project_1.View.UserLogView;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ViewDialog extends JDialog implements ActionListener {

	private UserLogView ul;

	JTextField startLine = null;
	JTextField endLine = null;
	JButton jbtnConfirm = new JButton("Confirm");

	public ViewDialog(UserLogView ul) {
		this.ul = ul;

		Container contentPane = this.getContentPane();
		JPanel pane = new JPanel();
		startLine = new JTextField(5);
		JLabel startLabel = new JLabel("start line: ");
		endLine = new JTextField(5);
		JLabel endLabel = new JLabel("end line: ");

		pane.add(startLabel);
		pane.add(startLine);
		pane.add(endLabel);
		pane.add(endLine);
		pane.add(jbtnConfirm);
		contentPane.add(pane);

		setBounds(100, 100, 300, 200);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

		jbtnConfirm.addActionListener(this);

	}

	public void actionPerformed(ActionEvent actionEvent) {
		if (actionEvent.getSource() == jbtnConfirm) {
			int start = 1;
			int end = 0;
			boolean flag = true; // 0209

			if (startLine.getText().isEmpty() && endLine.getText().isEmpty()) {
				System.out.println("bothEmpty");
				try {
					ul.readFile();
					dispose();
					new JButtonEvt(ul).createDialog();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (!startLine.getText().isEmpty() && endLine.getText().isEmpty()) {
				System.out.println("endEmpty");

				try {
					start = Integer.parseInt(startLine.getText());
					if (start < 0) {
						JOptionPane.showMessageDialog(ul, "����� �Է����ּ���.");
					} else {
						ul.readFile(start);
						new JButtonEvt(ul).createDialog();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(ul, "���ڸ� �Է����ּ���.");
					System.err.println("���ڸ� �Է����ּ���.");
				}
			} else {
				if (startLine.getText().isEmpty() && !endLine.getText().isEmpty()) {
					System.out.println("startEmpty");
					try {
						end = Integer.parseInt(endLine.getText());
						if (end < 0) {
							JOptionPane.showMessageDialog(ul, "����� �Է����ּ���.");
							flag = false;
						}
					} catch (NumberFormatException e) { // 0209
						flag = false;
						JOptionPane.showMessageDialog(ul, "���ڸ� �Է����ּ���.");
						System.err.println("���ڸ� �Է����ּ���. endLine");
					}
				} else if (!startLine.getText().isEmpty() && !endLine.getText().isEmpty()) {
					System.out.println("bothFull");
					try {
						start = Integer.parseInt(startLine.getText());
						end = Integer.parseInt(endLine.getText());
						if (start < 0 || end < 0) {
							JOptionPane.showMessageDialog(ul, "����� �Է����ּ���.");
							flag = false;
						}
					} catch (NumberFormatException e) { // 0209
						flag = false;
						JOptionPane.showMessageDialog(ul, "���ڸ� �Է����ּ���.");
						System.err.println("���ڸ� �Է����ּ���. startLine, endLine");

					}
				}
				if (flag) { // 0209
					try {
						ul.readFile(start, end);
						new JButtonEvt(ul).createDialog();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (NumberFormatException e) {// 0209 �߰� - ���ڸ� �Է�

					} catch (NullPointerException e) {
						System.err.println("null");
						e.printStackTrace();
					}
				}
			}

		} // end if
	}// actionPerformed

}// end class