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
 * 과제 1~7 1. 최다사용 키의 이름과 횟수 | java xx회 2. 브라우저별 접속횟수, 비율 IE - xx (xx%) 3. 서비스를
 * 성공적으로 수행한(200) 횟수,실패(404) 횟수 4. 요청이 가장 많은 시간 [10시] 5. 비정상적인 요청(403)이 발생한
 * 횟수,비율구하기 6. 요청에 대한 에러(500)가 발생한 횟수, 비율 구하기 7. 입력되는 라인에 해당하는 정보출력 (예
 * :1000~1500라인 이 입력되면) 1000~1500번째 라인에 해당하는 정보 중 최다사용 키의 이름과 횟수 | java/ xx회)
 * 
 * @author user
 */
@SuppressWarnings("serial")
public class UserLog extends JFrame {

	// 파일의 한 줄에서 parsing한 키워드가 저장되는 list
	private List<String> keyArr = new ArrayList<String>();
	private List<String> browserArr = new ArrayList<String>();
	private List<String> httpCodeArr = new ArrayList<String>();
	private List<String> reqTimeArr = new ArrayList<String>();

	/**
	 * 파일의 시작과 끝 라인을 인자로 받아 범위를 설정하고 그 범위에 해당하는 파일을 한 줄 씩 불러와 키워드를 parsing, 인스턴스 변수에
	 * 저장하는 메소드
	 * 
	 * @param int startLine //파일의 시작 라인
	 * @param int endLine //파일의 끝 라인
	 * @throws IOException
	 */
	public void readFile(int startLine, int endLine) throws IOException {
		// 파일을 열거나 저장할 때 사용되는 dialog
		FileDialog fd = new FileDialog(this, "파일선택", FileDialog.LOAD);
		fd.setVisible(true);

		// 읽어온 파일의 경로와 이름 저장
		String path = fd.getDirectory();
		String name = fd.getFile();

		// 파일 객체 생성
		File file = new File(path + name);
		if (file.exists()) {
			BufferedReader br = null;// 줄단위로 읽는 기능을 가진 스트림

			try {
				br = new BufferedReader(new FileReader(file)); // 파일을 읽어들여오는 16bit stream
				String data, key, code, time, browser; // data parsing을 위해 필요한 변수 및 한 줄을 저장하는 data
				data = key = code = time = browser = "";

				String keyStart = "key="; // 문자열에서 key값을 추출하기 위함
				String keyEnd = "&query";
				int line = 1; // 현재 가리키는 라인을 의미함

				// 과제7. 입력된 라인에 해당하는 정보 출력
				while (line < startLine) {
					br.readLine(); 
					line++;
				}
				while ((data = br.readLine()) != null && line < endLine + 1) { // 읽어들인 한줄이 null이 아니고 line의 범위에 맞다면
					String[] arr = data.replace("[", "").split("]");
					// [200][http://sist.co.kr/find/books?key=mongodb&query=sist][ie][2022-02-05 09:35:16]
					// 그 줄의 데이터를 배열로 만듬, arr[0] : httpCode , arr[1] : key , arr[2] : browser, arr[3] : requestTime

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

					line += 1;

					// 전체 키워드 출력 테스트
//					System.out.printf("%s %s %s %s\n", key, browser, code, time);

				} // end while

			} finally {
				if (br != null) {
					br.close();
				} // end if
			} // end finally
		} else {
			JOptionPane.showMessageDialog(this, file + "이 존재하지 않습니다");
			System.out.println("파일 없음");
		}

	}// readFile

	/**
	 * parsing한 데이터들을 key, 데이터들의 총 갯수를 value로 가지는 HashMap을 반환하는 method
	 * 
	 * @param List<String> keyword
	 * @return HashMap<String, Integer>
	 */
	public HashMap<String, Integer> countKeyword(List<String> keyword) {
		HashMap<String, Integer> dic = new HashMap<String, Integer>();
		int cnt;
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
	}

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
		}//end for
		
		if (dic.get(req) != null) {
			reqCnt = dic.get(req); 
		}//end if
		
		double ratio = (double) reqCnt / (double) allCnt * 100.0;
		return ((double) Math.round(ratio * 100) / 100);  //20.0
	}

	

	/**
	 * 가장 많이 요청된 요청 key와 해당 키가 요청된 횟수를 배열에 저장해 리턴하는 메소드
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
	 * 과제1 출력
	 * @return String
	 */
	public String printKey() {
		HashMap<String, Integer> keyMap = countKeyword(getKeyArr()); //키를 {키, 키횟수}로 변환
		String mostReqKey = mostFreqReq(keyMap)[0]; //키
		String mostReqCnt = mostFreqReq(keyMap)[1]; //키 횟수
		String keyNameCnt = "1. 최다사용 키의 이름과 횟수 \n"+mostReqKey+" | "+mostReqCnt+"회\n"+"--------------------------------";
		
		return keyNameCnt;
	}
	
	
	/**
	 * 과제2 출력
	 * 브라우저의 모든 키값에 대한 요청비율을 출력하는 메소드
	 * @return String
	 */
	public String printBrowser() {
		HashMap<String, Integer> browserMap = countKeyword(browserArr);
		Iterator<String> keys = browserMap.keySet().iterator();
		String key = "";
		String browserRatio = "2. 브라우저별 접속 횟수, 비율\n";
		while (keys.hasNext()) {
			key = keys.next();
			browserRatio += (String)key+" - "+String.valueOf((calRatio(browserMap, key))+" | 접속횟수 : "+String.valueOf(browserMap.get(key))+"회"+"\n");
		}
		return browserRatio+"--------------------------------";
	}
	
	
	/**
	 * 과제3 출력
	 * @return
	 */
	public String printHttpSucFail() {
		HashMap<String, Integer> httpCodeMap = countKeyword(getHttpCodeArr()); 
		String sucFailCnt = "";
		sucFailCnt = "3. 서비스를 성공적으로 수행한 횟수(200), 실패(404)\n"
				+ "성공(200): " + httpCodeMap.get("200") + "회\n"
				+"실패(404): " + httpCodeMap.get("404") + "회"
				+"\n--------------------------------";
		return sucFailCnt;
	}

	
	/**
	 * 과제4 출력
	 * @return
	 */
	public String printReqTime() {
			HashMap<String, Integer> reqTimeMap = countKeyword(getReqTimeArr());
			String mostReqTime = mostFreqReq(reqTimeMap)[0];
			String printReqTime = "4. 요청이 가장 많은 시간\n"+mostReqTime+"시\n"+"--------------------------------";
	
		return printReqTime;
	}

	
	/**
	 * 과제5 출력
	 * @return
	 */
	public String printErrCode403() {
		HashMap<String, Integer> httpCodeMap = countKeyword(getHttpCodeArr()); 
		String errCntRatio = "";
		Integer errCnt = httpCodeMap.get("403");
		if(httpCodeMap.get("403") == null)
			errCnt = 0;
		errCntRatio = "5. 비정상적인 요청(403)이 발생한 횟수, 비율\n"+
				"실패(403): " + String.valueOf(errCnt + "회"+ "\n"
						+"403: " + String.valueOf(calRatio(httpCodeMap, "403")) +"%")
							+"\n--------------------------------";
		return errCntRatio;
	}

	
	/**
	 * 과제6 출력
	 * @return String
	 */
	public String printErrCode500() {
		HashMap<String, Integer> httpCodeMap = countKeyword(getHttpCodeArr()); 
		String errCntRatio = "";
		Integer errCnt = httpCodeMap.get("500");
		if(httpCodeMap.get("500") == null)
			errCnt = 0;
		errCntRatio = "6. 요청에 대한 에러(500)가 발생한 횟수, 비율\n"+
				"실패(500): " + String.valueOf(errCnt + "회"+ "\n"
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
