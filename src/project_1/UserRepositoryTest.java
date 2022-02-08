package project_1;


public class UserRepositoryTest {
	UserRepository ur = UserRepository.getInstance();
	
	private void Test() {
		User u = ur.findByKey("admin");
		System.out.println
		(u.getId() + "/" + u.getPassword() + "/" + u.isReportAuthority());
		u = ur.findByKey("root");
		System.out.println
		(u.getId() + "/" + u.getPassword() + "/" + u.isReportAuthority());
		u = ur.findByKey("adminstrator");
		System.out.println
		(u.getId() + "/" + u.getPassword() + "/" + u.isReportAuthority());
	}
	
	public static void main(String[] args) {
		UserRepositoryTest urt = new UserRepositoryTest();
		urt.Test();	
	}

}
