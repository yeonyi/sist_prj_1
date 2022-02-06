package project_1;

import java.util.Map;

/**
 * 로그인 인증 메서드
 * 레포트 생성 권한 : "root" 권한 없음
 * Map의 key와 value값 비교
 * @author yeonyi
 */
public class AuthorityTest {
	
	UserRepository ur = UserRepository.getInstance(); 
//	Map<String, User> map = UserRepository.getUserList();
	Map<String, User> map = ur.getUserList();
	
	
	User u = ur.findByKey("admin");//key값을 매개변수로 받음
	String pw = u.getPassword();
	
	public boolean loginAurthority() {
		
		System.out.println(map.containsKey("admin"));
		System.out.println(map.containsKey("root"));
		System.out.println(map.containsKey("adminstrator"));//UserRepository의 HashMap을 잘 불러왔는지 확인
		
		if(u.getId().equals("admin") && pw.equals("1234")) {
			System.out.println("로그인 인증 성공");
			return true;
		}//end if
		
		if(u.getId().equals("root") && pw.equals("1111")) {
			System.out.println("로그인 인증 성공");
			return true;
		}//end if
		
		if(u.getId().equals("administor") && pw.equals("12345")) {
			System.out.println("로그인 인증 성공");
			return true;
		}//end if
		
//		if(u.getId().equals("admin") || u.getId().equals("root") || u.getId().equals("administrator")) {
//			System.out.println("로그인 인증 성공");
//			return true;
//		}//end if
			System.out.println("로그인 정보가 없습니다.");
			return false;
		
	}//aurthorityTest
	
	public boolean reportProduce() {
		
		if(u.getId().equals("root")) {
			System.out.println("문서 생성권한 없음");
			return false;
		}//end if
			System.out.println("인증 성공");
			return true;
		
	}//reportProduce

	public static void main(String[] args) {
		AuthorityTest at = new AuthorityTest();
		
		boolean isAuth = at.loginAurthority();
		boolean isReportAuth = at.reportProduce();

		System.out.println("loginAurthority() : " + isAuth);
		System.out.println("reportProduce() : " + isReportAuth);
	
	}//main

}//class
