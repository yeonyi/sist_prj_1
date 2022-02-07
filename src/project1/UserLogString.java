package project1;

import java.io.IOException;

public class UserLogString {
	
	public static void main(String[] args) {
		UserLog ul = new UserLog();
		int startLine = Integer.parseInt(args[0]); //시작라인 : runConfig에서 변수 두 개 넣어주셔야 에러나지 않습니다
		int endLine = Integer.parseInt(args[1]); //끝라인
		try {
			ul.readFile(startLine, endLine);
		} catch (IOException e) {
			e.printStackTrace();
		} // end catch
		ul.dispose(); //중요!!

		//모두 String형을 반환합니다. 
		//1. 최다사용 키의 횟수
		System.out.println(ul.printKey());
		//2.브라우저별 접속 횟수
		System.out.println(ul.printBrowser()); 
		//3.http요청 발생 횟수, 비율
		System.out.println(ul.printHttpSucFail());
		//4.요청이 가장 많은 시간
		System.out.println(ul.printReqTime());
		//5.에러코드403
		System.out.println(ul.printErrCode403());
		//6.에러코드500
		System.out.println(ul.printErrCode500());

	}// main

}
