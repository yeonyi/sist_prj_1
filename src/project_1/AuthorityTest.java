package project_1;

/**
 * Map의 key와 value값 비교
 * @author yeonyi
 */
public class AuthorityTest extends UserRepository{//HashMap을 불러오기 위해 UserRepository을 상속
	//UserRepository의 private접근지정자를 public으로 수정
	
	UserRepository ur = UserRepository.getInstance();
	
	public void aurthorityTest() {
		
		System.out.println(userList.containsKey("admin"));
		System.out.println(userList.containsKey("root"));
		System.out.println(userList.containsKey("adminstrator"));//UserRepository의 HashMap을 잘 불러왔는지 확인
		
		User u = ur.findByKey("admin");//key값을 매개변수로 받음
		
		if(u.getId() == "root") {
			System.out.println("문서 생성권한 없음");
		}else {
			System.out.println("인증 성공");
		}//end else
		
	}//aurthorityTest

	public static void main(String[] args) {
		AuthorityTest at = new AuthorityTest();
		at.aurthorityTest();
	
	}//main

}//class
