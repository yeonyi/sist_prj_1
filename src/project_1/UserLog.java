package project_1;

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
 * @author user
 */
@SuppressWarnings("serial")
public class UserLog extends JFrame {
	private Login login; //0208 �߰�

	// args ���� ��� ���� ���� ����
	private int startLine;
	private int endLine;

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

	// ������
	public UserLog(Login login) throws IOException {
		super("File �м�");
		this.login = login;

		// ������ ���ų� ������ �� ���Ǵ� FileDialog
		FileDialog fd = new FileDialog(this, "���ϼ���", FileDialog.LOAD);
		fd.setVisible(true);

		// �о�� ������ ��ο� �̸� ����
		String path = fd.getDirectory();
		String name = fd.getFile();

		// ���� ��ü ����
		file = new File(path + name);

		if (file.exists()) {
			BufferedReader br = null;// �ٴ����� �д� ����� ���� ��Ʈ��

			// ������ �����ϰ� �� ������ �����ϸ�
			// view��ư report��ư ����
			viewJbtn = new JButton("view");
			reportJbtn = new JButton("report");

			// ��ư �̺�Ʈ ���
			JButtonEvt jbe = new JButtonEvt(this);
			JButtonEvt jbe2 = new JButtonEvt(this,login);
			viewJbtn.addActionListener(jbe);
			reportJbtn.addActionListener(jbe2);

			add(viewJbtn);
			add(reportJbtn);

			setLayout(new FlowLayout());
			setBounds(300, 300, 300, 150);
			setVisible(true);

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			try {
				br = new BufferedReader(new FileReader(file)); // ������ �о�鿩���� 16bit stream
				String data, key, code, time, browser; // data parsing�� ���� �ʿ��� ���� �� �� ���� �����ϴ� data
				data = key = code = time = browser = "";

				String keyStart = "key="; // ���ڿ����� key���� �����ϱ� ����
				String keyEnd = "&query";

				while ((data = br.readLine()) != null) { // �о���� ������ null�� �ƴϰ� line�� ������ �´ٸ�

					fileString.add(data);
					String[] arr = data.replace("[", "").split("]");
					// [200][http://sist.co.kr/find/books?key=mongodb&query=sist][ie][2022-02-05
					// 09:35:16]
					// �� ���� �����͸� �迭�� ����, arr[0] : httpCode , arr[1] : key , arr[2] : browser, arr[3]
					// : requestTime

					// 1. �ִٻ�� Ű�� �̸��� Ƚ�� / errorCode 403�̸� key�� ����
					if (arr[1].contains("key")) {
						key = arr[1].substring(arr[1].indexOf(keyStart) + keyStart.length(), arr[1].indexOf(keyEnd));
						keyArr.add(key);
					}

					// 2. �������� ����Ƚ��, ����
					browser = arr[2];
					browserArr.add(browser);

					// 3,5,6. ���� ����(200)����(404) Ƚ�� / (500), (403)���� Ƚ���� ����
					code = arr[0];
					httpCodeArr.add(code);

					// 4. ��û�� ���� ���� �ð�
					time = arr[3].substring(arr[3].indexOf(":") - 2, arr[3].indexOf(":"));
					reqTimeArr.add(time);

					// ��ü Ű���� ��� �׽�Ʈ
//					System.out.printf("%s %s %s %s\n", key, browser, code, time);

				} // end while

			} finally {
				if (br != null) {
					br.close();
				} // end if
			} // end finally
		} else {
			JOptionPane.showMessageDialog(this, "������ �������� �ʽ��ϴ�");
//			System.out.println("���� ����");
		}

	}// UserLog

	/**
	 * ����7. args�� �Էµ� ���ο� �ش��ϴ� ���� ����� ���� �޼ҵ�
	 * 
	 * @param startLine
	 * @param endLine
	 * @return
	 * @throws IOException
	 */
	public String[] readFile(int startLine, int endLine) throws IOException {

		BufferedReader br = null;// �ٴ����� �д� ����� ���� ��Ʈ��

		br = new BufferedReader(new FileReader(file)); // ������ �о�鿩���� 16bit stream
		String data, key; // data parsing�� ���� �ʿ��� ���� �� �� ���� �����ϴ� data
		data = key = "";

		List<String> keyArr2 = new ArrayList<String>();
		String[] mostFreKey = null; // �ִ� ��� Ű�� Ƚ���� �����ϱ� ���� String �迭

		try {

			String keyStart = "key="; // ���ڿ����� key���� �����ϱ� ����
			String keyEnd = "&query";
			int line = 1; // ���� ����Ű�� ������ �ǹ���

			while (line < startLine) {
				br.readLine();
				line++;
			}
			while ((data = br.readLine()) != null && line < endLine + 1) { // �о���� ������ null�� �ƴϰ� line�� ������ �´ٸ�
				String[] arr = data.replace("[", "").split("]");
				// �� ���� �����͸� �迭�� ����, arr[0] : httpCode , arr[1] : key , arr[2] : browser, arr[3]
				// arr[1] : key

				// 1. �ִٻ�� Ű�� �̸��� Ƚ�� / errorCode 403�̸� key�� ����
				if (arr[1].contains("key")) {
					key = arr[1].substring(arr[1].indexOf(keyStart) + keyStart.length(), arr[1].indexOf(keyEnd));
					keyArr2.add(key);
				}

				line += 1;

			} // end while

			mostFreKey = mostFreqReq(countKeyword(keyArr2));

		} finally {
			if (br != null) {
				br.close();
			} // end if
		} // end finally

		return mostFreKey;
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
//			System.out.printf("%s - %.2f\n", key, (calRatio(browserMap, key)));
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
//        HashMap<String, Integer> mostReq = new HashMap<String, Integer>();
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
	 * view��ư  Dialog�� ����� ����ϱ� String ��ȯ �޼ҵ�
	 * @return
	 * @throws IOException
	 */
	public String printFileAnalysis() throws IOException {

		// 1~6�� ����� ��� ���� ����
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
		HashMap<String, Integer> httpCodeMap = countKeyword(getHttpCodeArr());
		result3 = "3. ���񽺸� ���������� ������ Ƚ��(200), ����(404) \n" + "����(200): " + httpCodeMap.get("200") + "ȸ" + "\n"
				+ "����(404): " + httpCodeMap.get("404") + "ȸ\n" + line;

		// ��û�� ���� ���� �ð�
		HashMap<String, Integer> reqTimeMap = countKeyword(getReqTimeArr());
		String mostReqTime = mostFreqReq(reqTimeMap)[0];

		result4 = "4. ��û�� ���� ���� �ð� : " + mostReqTime + "��\n" + line;

		result5 = "5. ���������� ��û(403)�� �߻��� Ƚ��, ����\n" + "����(403): " + httpCodeMap.get("403") + "ȸ" + "\n" + "403: "
				+ calRatio(httpCodeMap, "403") + "%\n" + line;

		result6 = "6. ��û�� ���� ����(500)�� �߻��� Ƚ��, ����\n" + "����(500): " + httpCodeMap.get("500") + "ȸ\n" + "500: "
				+ calRatio(httpCodeMap, "500") + "%\n" + line;

		// �Է��� �ش� ������ �ִ� Ű ���ϴ� �޼ҵ� �ҷ�����
		readFile(startLine, endLine);
		String mostReqKey2 = readFile(getStartLine(), getEndLine())[0]; // Ű
		String mostReqCnt2 = readFile(getStartLine(), getEndLine())[1]; // Ű Ƚ��
		result7 = "7. ����" + Integer.toString(getStartLine()) + "~ " + Integer.toString(getEndLine())
				+ " �ִ� ��� Ű : " + mostReqKey2 + " | Ƚ��: " + mostReqCnt2 + "ȸ\n";

		// 1~7�� ����
		finalResult = result1 + result2 + result3 + result4 + result5 + result6 + result7;

		return finalResult;
	}//printFileAnalysis()
	
	
	/**
	 * report��ư ����� ����ϱ� ���� String ��ȯ �޼ҵ�
	 * @return
	 * @throws IOException
	 */
	public String printReport() throws IOException {
		
		//1~6�� ����� ��� ���� ����
		String result1, result2, result3, result4, result5, result6 = "";
		String line = "---------------------------------------------------------\n";
		String finalResult = "";

		// �ִٻ�� Ű�� Ƚ��
		String mostReqKey = mostFreqReq(countKeyword(keyArr))[0]; // Ű
		String mostReqCnt = mostFreqReq(countKeyword(keyArr))[1]; // Ű Ƚ��

		result1 = "1. �ִٻ�� Ű�� �̸��� Ƚ�� | " + mostReqKey + " " + mostReqCnt + "ȸ\n"+line;

		// �������� ���� Ƚ��
		result2 = "2. �������� ���� Ƚ��, ���� \n" +browserPerRatio() + "\n"+line;

		// http��û �߻� Ƚ��, ����
		HashMap<String, Integer> httpCodeMap = countKeyword(httpCodeArr);
		result3 = "3. ���񽺸� ���������� ������ Ƚ��(200), ����(404) \n ����(200): " + httpCodeMap.get("200") + "ȸ\n"
				+ "����(404): " + httpCodeMap.get("404") + "ȸ\n"+line;

		// ��û�� ���� ���� �ð�
		HashMap<String, Integer> reqTimeMap = countKeyword(reqTimeArr);
		String mostReqTime = mostFreqReq(reqTimeMap)[0];

		result4 = "4. ��û�� ���� ���� �ð� : " + mostReqTime + "��\n"+line;

		result5 = "5. ���������� ��û(403)�� �߻��� Ƚ��, ����\n����(403): " + httpCodeMap.get("403") + "ȸ\n403: "
				+calRatio(httpCodeMap, "403") + "%\n"+line;

		result6 = "6. ��û�� ���� ����(500)�� �߻��� Ƚ��, ����\n����(500): " + httpCodeMap.get("500") + "ȸ\n500: "
				+calRatio(httpCodeMap, "500") + "%\n"+line;

		//1~6�� ����
		finalResult = result1 + result2 + result3 + result4 + result5 + result6;

		return finalResult;
	}
	
	

	public List<String> getKeyArr() {
		return keyArr;
	}

	public List<String> getBrowserArr() {
		return browserArr;
	}

	public List<String> getHttpCodeArr() {
		return httpCodeArr;
	}

	public List<String> getReqTimeArr() {
		return reqTimeArr;
	}

	public JButton getViewJbtn() {
		return viewJbtn;
	}

	public JButton getReportJbtn() {
		return reportJbtn;
	}

	/**
	 * @return the startLine
	 */
	public int getStartLine() {
		return startLine;
	}

	/**
	 * @return the endLine
	 */
	public int getEndLine() {
		return endLine;
	}

	/**
	 * @param startLine the startLine to set
	 */
	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	/**
	 * @param endLine the endLine to set
	 */
	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}

	/**
	 * @return the fileString
	 */
	public List<String> getFileString() {
		return fileString;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}
	

}// class
