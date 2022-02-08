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
 * 선택한 log 파일 분석 클래스
 * @author user
 */
@SuppressWarnings("serial")
public class UserLog extends JFrame {
	private Login login; //0208 추가

	// args 값을 담기 위한 변수 생성
	private int startLine;
	private int endLine;

	// view, report버튼 생성
	private JButton viewJbtn;
	private JButton reportJbtn;

	// 전체 파일의 모든 줄을 저장하는 List
	private List<String> fileString = new ArrayList<String>();

	// 파일의 한 줄에서 parsing한 키워드가 저장되는 list
	private List<String> keyArr = new ArrayList<String>();
	private List<String> browserArr = new ArrayList<String>();
	private List<String> httpCodeArr = new ArrayList<String>();
	private List<String> reqTimeArr = new ArrayList<String>();

	private File file;

	// 생성자
	public UserLog(Login login) throws IOException {
		super("File 분석");
		this.login = login;

		// 파일을 열거나 저장할 때 사용되는 FileDialog
		FileDialog fd = new FileDialog(this, "파일선택", FileDialog.LOAD);
		fd.setVisible(true);

		// 읽어온 파일의 경로와 이름 저장
		String path = fd.getDirectory();
		String name = fd.getFile();

		// 파일 객체 생성
		file = new File(path + name);

		if (file.exists()) {
			BufferedReader br = null;// 줄단위로 읽는 기능을 가진 스트림

			// 파일을 선택하고 그 파일이 존재하면
			// view버튼 report버튼 생성
			viewJbtn = new JButton("view");
			reportJbtn = new JButton("report");

			// 버튼 이벤트 등록
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
				br = new BufferedReader(new FileReader(file)); // 파일을 읽어들여오는 16bit stream
				String data, key, code, time, browser; // data parsing을 위해 필요한 변수 및 한 줄을 저장하는 data
				data = key = code = time = browser = "";

				String keyStart = "key="; // 문자열에서 key값을 추출하기 위함
				String keyEnd = "&query";

				while ((data = br.readLine()) != null) { // 읽어들인 한줄이 null이 아니고 line의 범위에 맞다면

					fileString.add(data);
					String[] arr = data.replace("[", "").split("]");
					// [200][http://sist.co.kr/find/books?key=mongodb&query=sist][ie][2022-02-05
					// 09:35:16]
					// 그 줄의 데이터를 배열로 만듬, arr[0] : httpCode , arr[1] : key , arr[2] : browser, arr[3]
					// : requestTime

					// 1. 최다사용 키의 이름과 횟수 / errorCode 403이면 key가 없다
					if (arr[1].contains("key")) {
						key = arr[1].substring(arr[1].indexOf(keyStart) + keyStart.length(), arr[1].indexOf(keyEnd));
						keyArr.add(key);
					}

					// 2. 브라우저별 접속횟수, 비율
					browser = arr[2];
					browserArr.add(browser);

					// 3,5,6. 서비스 성공(200)실패(404) 횟수 / (500), (403)에러 횟수와 비율
					code = arr[0];
					httpCodeArr.add(code);

					// 4. 요청이 가장 많은 시간
					time = arr[3].substring(arr[3].indexOf(":") - 2, arr[3].indexOf(":"));
					reqTimeArr.add(time);

					// 전체 키워드 출력 테스트
//					System.out.printf("%s %s %s %s\n", key, browser, code, time);

				} // end while

			} finally {
				if (br != null) {
					br.close();
				} // end if
			} // end finally
		} else {
			JOptionPane.showMessageDialog(this, "파일이 존재하지 않습니다");
//			System.out.println("파일 없음");
		}

	}// UserLog

	/**
	 * 과제7. args로 입력된 라인에 해당하는 정보 출력을 위한 메소드
	 * 
	 * @param startLine
	 * @param endLine
	 * @return
	 * @throws IOException
	 */
	public String[] readFile(int startLine, int endLine) throws IOException {

		BufferedReader br = null;// 줄단위로 읽는 기능을 가진 스트림

		br = new BufferedReader(new FileReader(file)); // 파일을 읽어들여오는 16bit stream
		String data, key; // data parsing을 위해 필요한 변수 및 한 줄을 저장하는 data
		data = key = "";

		List<String> keyArr2 = new ArrayList<String>();
		String[] mostFreKey = null; // 최다 사용 키와 횟수를 저장하기 위한 String 배열

		try {

			String keyStart = "key="; // 문자열에서 key값을 추출하기 위함
			String keyEnd = "&query";
			int line = 1; // 현재 가리키는 라인을 의미함

			while (line < startLine) {
				br.readLine();
				line++;
			}
			while ((data = br.readLine()) != null && line < endLine + 1) { // 읽어들인 한줄이 null이 아니고 line의 범위에 맞다면
				String[] arr = data.replace("[", "").split("]");
				// 그 줄의 데이터를 배열로 만듬, arr[0] : httpCode , arr[1] : key , arr[2] : browser, arr[3]
				// arr[1] : key

				// 1. 최다사용 키의 이름과 횟수 / errorCode 403이면 key가 없다
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
	 * parsing한 데이터들을 key, 데이터들의 총 갯수를 value로 가지는 HashMap을 반환하는 method
	 * 
	 * @param List<String> keyword
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> countKeyword(List<String> keyword) {
		HashMap<String, Integer> dic = new HashMap<String, Integer>();

		int cnt = 0;
		for (int i = 0; i < keyword.size(); i++) {
			cnt = 1;
			// 배열에 담긴 키워드가 dic에 존재하는지 확인
			if (dic.containsKey(keyword.get(i))) {
				// 존재하면 해당 키워드의 value값 1 증가
				cnt = dic.get(keyword.get(i)) + 1;
			}
			// 없으면 해당 키워드를 key, value는 1로 생성
			dic.put(keyword.get(i), cnt);
		}

		return dic;
	}// countKeyword

	/**
	 * 특정 hashMap과 요청키워드를 인자로 받아 요청 키워드가 전체 요청에서 차지하는 비율을 구하는 메소드
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
	 * 브라우저의 모든 키값에 대한 요청비율을 출력하는 메소드
	 */
	public String browserPerRatio() {
		HashMap<String, Integer> browserMap = countKeyword(browserArr);
		Iterator<String> keys = browserMap.keySet().iterator();
		String key = "";
		String test = "";
		while (keys.hasNext()) {
			key = keys.next();
//			System.out.printf("%s - %.2f\n", key, (calRatio(browserMap, key)));
			test += (String) key + " - " + String.valueOf((calRatio(browserMap, key)) + "% \n") + " | 접속횟수 : "
					+ String.valueOf(browserMap.get(key)) + "회" + "\n";
		}
		return test;
	}// browserPerRatio

	/**
	 * 가장 많이 요청된 요청 key와 해당 키가 요청된 횟수를 배열에 저장해 리턴하는 메소드
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
	 * view버튼  Dialog에 결과를 출력하기 String 반환 메소드
	 * @return
	 * @throws IOException
	 */
	public String printFileAnalysis() throws IOException {

		// 1~6번 결과물 출력 위한 변수
		String result1, result2, result3, result4, result5, result6, result7 = "";
		String line = "---------------------------------------------------------\n";
		String finalResult = "";

		// 최다사용 키의 횟수
		String mostReqKey = mostFreqReq(countKeyword(keyArr))[0]; // 키
		String mostReqCnt = mostFreqReq(countKeyword(keyArr))[1]; // 키 횟수

		result1 = "1. 최다사용 키의 이름과 횟수 | " + mostReqKey + " " + mostReqCnt + "회" + "\n" + line;

		// 브라우저별 접속 횟수
		result2 = "2. 브라우저별 접속 횟수, 비율 \n" + browserPerRatio() + "\n" + line;

		// http요청 발생 횟수, 비율
		HashMap<String, Integer> httpCodeMap = countKeyword(getHttpCodeArr());
		result3 = "3. 서비스를 성공적으로 수행한 횟수(200), 실패(404) \n" + "성공(200): " + httpCodeMap.get("200") + "회" + "\n"
				+ "실패(404): " + httpCodeMap.get("404") + "회\n" + line;

		// 요청이 가장 많은 시간
		HashMap<String, Integer> reqTimeMap = countKeyword(getReqTimeArr());
		String mostReqTime = mostFreqReq(reqTimeMap)[0];

		result4 = "4. 요청이 가장 많은 시간 : " + mostReqTime + "시\n" + line;

		result5 = "5. 비정상적인 요청(403)이 발생한 횟수, 비율\n" + "실패(403): " + httpCodeMap.get("403") + "회" + "\n" + "403: "
				+ calRatio(httpCodeMap, "403") + "%\n" + line;

		result6 = "6. 요청에 대한 에러(500)가 발생한 횟수, 비율\n" + "실패(500): " + httpCodeMap.get("500") + "회\n" + "500: "
				+ calRatio(httpCodeMap, "500") + "%\n" + line;

		// 입력한 해당 라인의 최다 키 구하는 메소드 불러오기
		readFile(startLine, endLine);
		String mostReqKey2 = readFile(getStartLine(), getEndLine())[0]; // 키
		String mostReqCnt2 = readFile(getStartLine(), getEndLine())[1]; // 키 횟수
		result7 = "7. 라인" + Integer.toString(getStartLine()) + "~ " + Integer.toString(getEndLine())
				+ " 최다 사용 키 : " + mostReqKey2 + " | 횟수: " + mostReqCnt2 + "회\n";

		// 1~7번 조합
		finalResult = result1 + result2 + result3 + result4 + result5 + result6 + result7;

		return finalResult;
	}//printFileAnalysis()
	
	
	/**
	 * report버튼 결과를 출력하기 위한 String 반환 메소드
	 * @return
	 * @throws IOException
	 */
	public String printReport() throws IOException {
		
		//1~6번 결과물 출력 위한 변수
		String result1, result2, result3, result4, result5, result6 = "";
		String line = "---------------------------------------------------------\n";
		String finalResult = "";

		// 최다사용 키의 횟수
		String mostReqKey = mostFreqReq(countKeyword(keyArr))[0]; // 키
		String mostReqCnt = mostFreqReq(countKeyword(keyArr))[1]; // 키 횟수

		result1 = "1. 최다사용 키의 이름과 횟수 | " + mostReqKey + " " + mostReqCnt + "회\n"+line;

		// 브라우저별 접속 횟수
		result2 = "2. 브라우저별 접속 횟수, 비율 \n" +browserPerRatio() + "\n"+line;

		// http요청 발생 횟수, 비율
		HashMap<String, Integer> httpCodeMap = countKeyword(httpCodeArr);
		result3 = "3. 서비스를 성공적으로 수행한 횟수(200), 실패(404) \n 성공(200): " + httpCodeMap.get("200") + "회\n"
				+ "실패(404): " + httpCodeMap.get("404") + "회\n"+line;

		// 요청이 가장 많은 시간
		HashMap<String, Integer> reqTimeMap = countKeyword(reqTimeArr);
		String mostReqTime = mostFreqReq(reqTimeMap)[0];

		result4 = "4. 요청이 가장 많은 시간 : " + mostReqTime + "시\n"+line;

		result5 = "5. 비정상적인 요청(403)이 발생한 횟수, 비율\n실패(403): " + httpCodeMap.get("403") + "회\n403: "
				+calRatio(httpCodeMap, "403") + "%\n"+line;

		result6 = "6. 요청에 대한 에러(500)가 발생한 횟수, 비율\n실패(500): " + httpCodeMap.get("500") + "회\n500: "
				+calRatio(httpCodeMap, "500") + "%\n"+line;

		//1~6번 조합
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
