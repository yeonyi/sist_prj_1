package project1;

/**
 * 로그인 인증 메서드
 * 레포트 생성 권한 : "root" 권한 없음
 * Map의 key와 value값 비교
 * @author yeonyi
 */
public class AuthorityTest {
	Authority a = new Authority();
	public AuthorityTest() {
		System.out.println(a.loginAuthenticate("admin", "1234"));
		System.out.println(a.loginAuthenticate("root", "1111"));
		System.out.println(a.loginAuthenticate("adminstrator", "12345"));
		System.out.println(a.loginAuthenticate("admin", "123234"));
		System.out.println(a.loginAuthenticate("123", "1234"));
		
		System.out.println("------report authority------");
		System.out.println(a.reportAuthenticate("root"));
		
	}//AuthorityTest
	

	public static void main(String[] args) {
		new AuthorityTest();

	}//main

}//class 