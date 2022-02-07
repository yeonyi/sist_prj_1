package project1;

import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * ���� 1~7 1. �ִٻ�� Ű�� �̸��� Ƚ�� | java xxȸ 2. �������� ����Ƚ��, ���� IE - xx (xx%) 3. ���񽺸�
 * ���������� ������(200) Ƚ��,����(404) Ƚ�� 4. ��û�� ���� ���� �ð� [10��] 5. ���������� ��û(403)�� �߻���
 * Ƚ��,�������ϱ� 6. ��û�� ���� ����(500)�� �߻��� Ƚ��, ���� ���ϱ� 7. �ԷµǴ� ���ο� �ش��ϴ� ������� (��
 * :1000~1500���� �� �ԷµǸ�) 1000~1500��° ���ο� �ش��ϴ� ���� �� �ִٻ�� Ű�� �̸��� Ƚ�� | java/ xxȸ)
 * 
 * @author user
 */
@SuppressWarnings("serial")
public class UserLog extends JFrame {

	// ������ �� �ٿ��� parsing�� Ű���尡 ����Ǵ� list
	private List<String> keyArr = new ArrayList<String>();
	private List<String> browserArr = new ArrayList<String>();
	private List<String> httpCodeArr = new ArrayList<String>();
	private List<String> reqTimeArr = new ArrayList<String>();

	/**
	 * ������ ���۰� �� ������ ���ڷ� �޾� ������ �����ϰ� �� ������ �ش��ϴ� ������ �� �� �� �ҷ��� Ű���带 parsing, �ν��Ͻ� ������
	 * �����ϴ� �޼ҵ�
	 * 
	 * @param int startLine //������ ���� ����
	 * @param int endLine //������ �� ����
	 * @throws IOException
	 */
	public void readFile(int startLine, int endLine) throws IOException {
		// ������ ���ų� ������ �� ���Ǵ� dialog
		FileDialog fd = new FileDialog(this, "���ϼ���", FileDialog.LOAD);
		fd.setVisible(true);

		// �о�� ������ ��ο� �̸� ����
		String path = fd.getDirectory();
		String name = fd.getFile();

		// ���� ��ü ����
		File file = new File(path + name);
		if (file.exists()) {
			BufferedReader br = null;// �ٴ����� �д� ����� ���� ��Ʈ��

			try {
				br = new BufferedReader(new FileReader(file)); // ������ �о�鿩���� 16bit stream
				String data, key, code, time, browser; // data parsing�� ���� �ʿ��� ���� �� �� ���� �����ϴ� data
				data = key = code = time = browser = "";

				String keyStart = "key="; // ���ڿ����� key���� �����ϱ� ����
				String keyEnd = "&query";
				int line = 1; // ���� ����Ű�� ������ �ǹ���

				// ����7. �Էµ� ���ο� �ش��ϴ� ���� ���
				while (line < startLine) {
					br.readLine(); 
					line++;
				}
				while ((data = br.readLine()) != null && line < endLine + 1) { // �о���� ������ null�� �ƴϰ� line�� ������ �´ٸ�
					String[] arr = data.replace("[", "").split("]");
					// [200][http://sist.co.kr/find/books?key=mongodb&query=sist][ie][2022-02-05 09:35:16]
					// �� ���� �����͸� �迭�� ����, arr[0] : httpCode , arr[1] : key , arr[2] : browser, arr[3] : requestTime

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

					line += 1;

					// ��ü Ű���� ��� �׽�Ʈ
//					System.out.printf("%s %s %s %s\n", key, browser, code, time);

				} // end while

			} finally {
				if (br != null) {
					br.close();
				} // end if
			} // end finally
		} else {
			JOptionPane.showMessageDialog(this, file + "�� �������� �ʽ��ϴ�");
			System.out.println("���� ����");
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
		int cnt;
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
	}

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
		}//end for
		
		if (dic.get(req) != null) {
			reqCnt = dic.get(req); 
		}//end if
		
		double ratio = (double) reqCnt / (double) allCnt * 100.0;
		return ((double) Math.round(ratio * 100) / 100);  //20.0
	}

	

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
	}

	
	/**
	 * ����1 ���
	 * @return String
	 */
	public String printKey() {
		HashMap<String, Integer> keyMap = countKeyword(getKeyArr()); //Ű�� {Ű, ŰȽ��}�� ��ȯ
		String mostReqKey = mostFreqReq(keyMap)[0]; //Ű
		String mostReqCnt = mostFreqReq(keyMap)[1]; //Ű Ƚ��
		String keyNameCnt = "1. �ִٻ�� Ű�� �̸��� Ƚ�� \n"+mostReqKey+" | "+mostReqCnt+"ȸ\n"+"--------------------------------";
		
		return keyNameCnt;
	}
	
	
	/**
	 * ����2 ���
	 * �������� ��� Ű���� ���� ��û������ ����ϴ� �޼ҵ�
	 * @return String
	 */
	public String printBrowser() {
		HashMap<String, Integer> browserMap = countKeyword(browserArr);
		Iterator<String> keys = browserMap.keySet().iterator();
		String key = "";
		String browserRatio = "2. �������� ���� Ƚ��, ����\n";
		while (keys.hasNext()) {
			key = keys.next();
			browserRatio += (String)key+" - "+String.valueOf((calRatio(browserMap, key))+" | ����Ƚ�� : "+String.valueOf(browserMap.get(key))+"ȸ"+"\n");
		}
		return browserRatio+"--------------------------------";
	}
	
	
	/**
	 * ����3 ���
	 * @return
	 */
	public String printHttpSucFail() {
		HashMap<String, Integer> httpCodeMap = countKeyword(getHttpCodeArr()); 
		String sucFailCnt = "";
		sucFailCnt = "3. ���񽺸� ���������� ������ Ƚ��(200), ����(404)\n"
				+ "����(200): " + httpCodeMap.get("200") + "ȸ\n"
				+"����(404): " + httpCodeMap.get("404") + "ȸ"
				+"\n--------------------------------";
		return sucFailCnt;
	}

	
	/**
	 * ����4 ���
	 * @return
	 */
	public String printReqTime() {
			HashMap<String, Integer> reqTimeMap = countKeyword(getReqTimeArr());
			String mostReqTime = mostFreqReq(reqTimeMap)[0];
			String printReqTime = "4. ��û�� ���� ���� �ð�\n"+mostReqTime+"��\n"+"--------------------------------";
	
		return printReqTime;
	}

	
	/**
	 * ����5 ���
	 * @return
	 */
	public String printErrCode403() {
		HashMap<String, Integer> httpCodeMap = countKeyword(getHttpCodeArr()); 
		String errCntRatio = "";
		Integer errCnt = httpCodeMap.get("403");
		if(httpCodeMap.get("403") == null)
			errCnt = 0;
		errCntRatio = "5. ���������� ��û(403)�� �߻��� Ƚ��, ����\n"+
				"����(403): " + String.valueOf(errCnt + "ȸ"+ "\n"
						+"403: " + String.valueOf(calRatio(httpCodeMap, "403")) +"%")
							+"\n--------------------------------";
		return errCntRatio;
	}

	
	/**
	 * ����6 ���
	 * @return String
	 */
	public String printErrCode500() {
		HashMap<String, Integer> httpCodeMap = countKeyword(getHttpCodeArr()); 
		String errCntRatio = "";
		Integer errCnt = httpCodeMap.get("500");
		if(httpCodeMap.get("500") == null)
			errCnt = 0;
		errCntRatio = "6. ��û�� ���� ����(500)�� �߻��� Ƚ��, ����\n"+
				"����(500): " + String.valueOf(errCnt + "ȸ"+ "\n"
						+"500: " + String.valueOf(calRatio(httpCodeMap, "500")) +"%")+
					"\n--------------------------------";
		return errCntRatio;
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

}// class
