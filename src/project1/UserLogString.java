package project1;

import java.io.IOException;

/**
 * main method arguments 받기
 * @author user
 */
public class UserLogString {

	public static void main(String[] args) {

		try {
			UserLog ul = new UserLog();
			
			int fileLineLength = Integer.valueOf(ul.getFileString().size()); //파일안의 줄의 개수
			int args0 = Integer.valueOf(args[0]); 
			int args1 = Integer.valueOf(args[1]);

			if (args[0] != null && args[1] != null) {
				if (args0 > 0 && args1 < fileLineLength+1) {// text 줄 개수를 벗어나지 않는다면
					ul.setStartLine(Integer.parseInt(args[0])); // 시작라인 : runConfig에서 변수 두 개 넣어주셔야 에러나지 않습니다
					ul.setEndLine(Integer.parseInt(args[1]));
				}else if(ul.getFile().exists()!=true) { //파일이 존재하지 않거나 취소되었을 때
					System.out.println("파일 없음");
				}else {
					System.err.println("범위 내 라인 수가 아닙니다.");
				}
				// end else
			} // end if
			
		} catch (IOException e) {
			e.printStackTrace();
		} // end catch

	}// main

}// class
