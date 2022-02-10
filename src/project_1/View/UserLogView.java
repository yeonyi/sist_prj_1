package project_1.View;

import project_1.Event.JButtonEvt;
import project_1.View.LoginView;

import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * ������ log ���� �м� Ŭ����
 * 
 * @author user
 */
@SuppressWarnings("serial")
public class UserLogView extends JFrame {
	@SuppressWarnings("unused")
	private LoginView login; // 0208 �߰�

	// args ���� ��� ���� ���� ����
	// view, report��ư ����
	private JButton viewJbtn;
	private JButton reportJbtn;

	// ��ü ������ ��� ���� �����ϴ� List
	private List<String> fileString = new ArrayList<String>();

	// ������ �� �ٿ��� parsing�� Ű���尡 ����Ǵ� list
	private List<String> keyArr = new ArrayList<String>();
	private List<String> browserArr = new ArrayList<String>();
	private List<String> httpCodeArr = new ArrayList<String>();
	private List<String> reqTimeArr = new ArrayList<String>();

	private File file;
	private int lineCnt = 0;
	private int startPoint = 1;

	// ������
	public UserLogView(LoginView login) throws IOException {
		super("File �м�");
		this.login = login;

		// ������ �����ϰ� �� ������ �����ϸ�
		// view��ư report��ư ����
		viewJbtn = new JButton("view");
		reportJbtn = new JButton("report");

		// ��ư �̺�Ʈ ���
		JButtonEvt jbe = new JButtonEvt(this);
		JButtonEvt jbe2 = new JButtonEvt(this, login);
		viewJbtn.addActionListener(jbe);
		reportJbtn.addActionListener(jbe2);

		add(viewJbtn);
		add(reportJbtn);

		setLayout(new FlowLayout());
		setBounds(300, 300, 300, 150);
		setVisible(true);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}// UserLog

	public File fileChoose() {
		FileDialog fd = new FileDialog(this, "���ϼ���", FileDialog.LOAD);
		fd.setVisible(true);

		String path = fd.getDirectory();
		String name = fd.getFile();

		file = new File(path + name);

		keyArr.clear();
		browserArr.clear();
		httpCodeArr.clear();
		reqTimeArr.clear();
		fileString.clear();

		return file;

	}

	public void dataParsing(String data) {
		String keyStart = "key="; // ���ڿ����� key���� �����ϱ� ����
		String keyEnd = "&query";
		String key, code, time, browser; // data parsing�� ���� �ʿ��� ���� �� �� ���� �����ϴ� data
		key = code = time = browser = "";

		String[] arr = data.replace("[", "").split("]");

		if (arr[1].contains("key")) {
			key = arr[1].substring(arr[1].indexOf(keyStart) + keyStart.length(), arr[1].indexOf(keyEnd));
			keyArr.add(key);
		}

		browser = arr[2];
		browserArr.add(browser);

		code = arr[0];
		httpCodeArr.add(code);

		time = arr[3].substring(arr[3].indexOf(":") - 2, arr[3].indexOf(":"));
		reqTimeArr.add(time);

		fileString.add(data);
		lineCnt = Integer.valueOf(fileString.size());

	}

	/**
	 * ����7. args�� �Էµ� ���ο� �ش��ϴ� ���� ����� ���� �޼ҵ�
	 * 
	 * @param startLine
	 * @param endLine
	 * @throws IOException
	 */
	public void readFile() throws IOException {

		if (fileChoose().exists()) {
			BufferedReader br = null;// �ٴ����� �д� ����� ���� ��Ʈ��
			try {
				br = new BufferedReader(new FileReader(file)); // ������ �о�鿩���� 16bit stream
				String data;

				while ((data = br.readLine()) != null) { // �о���� ������ null�� �ƴϰ� line�� ������ �´ٸ�
					dataParsing(data);

				} // end while

			} finally {
				if (br != null) {
					br.close();
				} // end if
			} // end finally
		} else {
			JOptionPane.showMessageDialog(this, "������ �������� �ʽ��ϴ�");
		}

	}// readFile

	public void readFile(int startLine) throws IOException {

		if (fileChoose().exists()) {
			BufferedReader br = null;// �ٴ����� �д� ����� ���� ��Ʈ��
			try {
				br = new BufferedReader(new FileReader(file)); // ������ �о�鿩���� 16bit stream
				String data;

				int line = 1;
				startPoint = startLine;

				while (line < startLine) {
					br.readLine();
					line++;
				}

				while ((data = br.readLine()) != null) { // �о���� ������ null�� �ƴϰ� line�� ������ �´ٸ�
					dataParsing(data);

				} // end while

			} finally {
				if (br != null) {
					br.close();
					System.out.println("closed well");
				} // end if
			} // end finally
		} else {
			JOptionPane.showMessageDialog(this, "������ �������� �ʽ��ϴ�");
		}

	}// readFile

	public void readFile(int startLine, int endLine) throws IOException {

		if (fileChoose().exists()) {
			BufferedReader br = null;// �ٴ����� �д� ����� ���� ��Ʈ��

			try {
				br = new BufferedReader(new FileReader(file)); // ������ �о�鿩���� 16bit stream
				String data;
				int line = 1;
				startPoint = startLine;

				while (line < startLine) {
					br.readLine();
					line++;
				}

				while ((data = br.readLine()) != null && line < endLine + 1) { // �о���� ������ null�� �ƴϰ� line�� ������ �´ٸ�
					dataParsing(data);
					line += 1;

				} // end while

			} finally {
				if (br != null) {
					br.close();
				} // end if
			} // end finally
		} else {
			JOptionPane.showMessageDialog(this, "������ �������� �ʽ��ϴ�");
		}

	}// readFile

	/**
	 * parsing�� �����͵��� key, �����͵��� �� ������ value�� ������ HashMap�� ��ȯ�ϴ� method
	 * 
	 * @param List<String> keyword
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> countKeyword(List<String> keyword) {
		HashMap<String, Integer> dic = new HashMap<String, Integer>();

		int cnt = 0;
		for (int i = 0; i < keyword.size(); i++) {
			cnt = 1;
			// �迭�� ��� Ű���尡 dic�� �����ϴ��� Ȯ��
			if (dic.containsKey(keyword.get(i))) {
				// �����ϸ� �ش� Ű������ value�� 1 ����
				cnt = dic.get(keyword.get(i)) + 1;
			}
			// ������ �ش� Ű���带 key, value�� 1�� ����
			dic.put(keyword.get(i), cnt);
		}

		return dic;
	}// countKeyword

	/**
	 * Ư�� hashMap�� ��ûŰ���带 ���ڷ� �޾� ��û Ű���尡 ��ü ��û���� �����ϴ� ������ ���ϴ� �޼ҵ�
	 * 
	 * @param HashMap<String, Integer>
	 * @param String
	 * @return double
	 */
	public double calRatio(HashMap<String, Integer> dic, String req) {
		int allCnt = 0;
		int reqCnt = 0;
		for (String key : dic.keySet()) {
			allCnt += dic.get(key);
		} // end for

		if (dic.get(req) != null) {
			reqCnt = dic.get(req);
		} // end if

		double ratio = (double) reqCnt / (double) allCnt * 100.0;
		return ((double) Math.round(ratio * 100) / 100); // 20.0
	}// calRatio

	/**
	 * �������� ��� Ű���� ���� ��û������ ����ϴ� �޼ҵ�
	 */
	public String browserPerRatio() {
		HashMap<String, Integer> browserMap = countKeyword(browserArr);
		Iterator<String> keys = browserMap.keySet().iterator();
		String key = "";
		String test = "";
		while (keys.hasNext()) {
			key = keys.next();
			test += (String) key + " - " + String.valueOf((calRatio(browserMap, key)) + "% \n") + " | ����Ƚ�� : "
					+ String.valueOf(browserMap.get(key)) + "ȸ" + "\n";
		}
		return test;
	}// browserPerRatio

	/**
	 * ���� ���� ��û�� ��û key�� �ش� Ű�� ��û�� Ƚ���� �迭�� ������ �����ϴ� �޼ҵ�
	 * 
	 * @param HashMap<String, Integer>
	 * @return String[]
	 */
	public String[] mostFreqReq(HashMap<String, Integer> dic) {
		int mostVal = 0;
		String mostKey = "";
		for (String key : dic.keySet()) {
			if (mostVal < dic.get(key)) {
				mostVal = dic.get(key);
				mostKey = key;
			}
		}
		String[] mostReq = { mostKey, Integer.toString(dic.get(mostKey)) };
		return mostReq;
	}// mostFreqReq

	/**
	 * Ư�� �ڵ带 �Է¹޾� codeMap�� value(�ڵ� ����Ƚ��)�� �����ϴ� �޼ҵ� code key�� �������� �ʾ� ���� ���� �� null��
	 * 0���� ��ȯ
	 * 
	 * @param String httpCode
	 * @return int codeCnt
	 */
	public int httpCodeCnt(String httpCode) {
		HashMap<String, Integer> httpCodeMap = countKeyword(httpCodeArr);

		Integer codeCnt = httpCodeMap.get(httpCode);
		if (codeCnt == null)
			codeCnt = 0;

		return codeCnt;
	}

	/**
	 * view��ư Dialog�� ����� ����ϱ� String ��ȯ �޼ҵ�
	 * 
	 * @return
	 * @throws IOException
	 */
	public String printFileAnalysis() throws IOException {
		String result1, result2, result3, result4, result5, result6, result7 = "";
		String line = "---------------------------------------------------------\n";
		String finalResult = "";

		// �ִٻ�� Ű�� Ƚ��
		String mostReqKey = mostFreqReq(countKeyword(keyArr))[0]; // Ű
		String mostReqCnt = mostFreqReq(countKeyword(keyArr))[1]; // Ű Ƚ��

		result1 = "1. �ִٻ�� Ű�� �̸��� Ƚ�� | " + mostReqKey + " " + mostReqCnt + "ȸ" + "\n" + line;

		// �������� ���� Ƚ��
		result2 = "2. �������� ���� Ƚ��, ���� \n" + browserPerRatio() + "\n" + line;

		// http��û �߻� Ƚ��, ����
		result3 = "3. ���񽺸� ���������� ������ Ƚ��(200), ����(404) \n" + "����(200): " + httpCodeCnt("200") + "ȸ" + "\n" + "����(404): "
				+ httpCodeCnt("404") + "ȸ\n" + line;

		// ��û�� ���� ���� �ð�
		HashMap<String, Integer> reqTimeMap = countKeyword(reqTimeArr);
		String mostReqTime = mostFreqReq(reqTimeMap)[0];

		result4 = "4. ��û�� ���� ���� �ð� : " + mostReqTime + "��\n" + line;

		HashMap<String, Integer> httpCodeMap = countKeyword(httpCodeArr);
		result5 = "5. ���������� ��û(403)�� �߻��� Ƚ��, ����\n" + "����(403): " + httpCodeCnt("403") + "ȸ" + "\n" + "403: "
				+ calRatio(httpCodeMap, "403") + "%\n" + line;

		result6 = "6. ��û�� ���� ����(500)�� �߻��� Ƚ��, ����\n" + "����(500): " + httpCodeCnt("500") + "ȸ\n" + "500: "
				+ calRatio(httpCodeMap, "500") + "%\n" + line;

		result7 = "7. �м��� ���� �� " + lineCnt + " | �������� : " + startPoint + " | ������ : "
				+ String.valueOf(lineCnt + startPoint - 1);

		finalResult = result1 + result2 + result3 + result4 + result5 + result6 + result7;

		return finalResult;
	}

	public JButton getViewJbtn() {
		return viewJbtn;
	}

	public JButton getReportJbtn() {
		return reportJbtn;
	}

}// class
