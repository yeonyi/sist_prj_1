package project_1;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
	
//	User admin = new User("admin","1234",true);
//	User root = new User ("root","1111",false);
//	User adminstrator = new User ("adminstrator","12345",true);
	
	private static Map<String,User> userList = new HashMap<String, User>();
	
	private static final UserRepository instance = new UserRepository();
	
	public static UserRepository getInstance() {
		userList.put("admin", new User("admin","1234",true));
		userList.put("root", new User("root","1111",false));
		userList.put("adminstrator", new User("adminstrator","12345",true));
		return instance;
	}//getInstance
	
	private UserRepository() {
	}//UseRepository
	
	public User findByKey(String id) {
		return userList.get(id);
	}//findByKey

	public static Map<String, User> getUserList() {
		return userList;
	}
	
	
	
}//class