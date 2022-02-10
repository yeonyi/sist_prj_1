package project_1.User;


import java.util.HashMap;
import java.util.Map;

/**
 * UserRepository <br>
 * �䱸���׿� ��õ� ��������� ���� <br>
 * static�� HashMap�� �ϳ��� �����ؼ� <br>
 * UserRepository ��ü�� = UserRepository.getInstance(); �� ��� <br>
 * ������ ���� �Ұ�
 * @author User
 *
 */
public class UserRepository {
	
	private static Map<String, User> userList = new HashMap<String, User>(); //static
	
	private static final UserRepository instance = new UserRepository(); //static
	

	/**
	 * userList ��������ÿ��� �ʱⰪ �Է�<br>
	 * getInstance�θ� UserRepository �ҷ����� ����
	 * @return
	 */
	public static UserRepository getInstance() {
		if (userList.isEmpty()) {
			userList.put("admin", new User("admin","1234",true));
			userList.put("root", new User("root","1111",false));
			userList.put("adminstrator", new User("adminstrator","12345",true));
		}
		
		return instance;
		
	}
	
	//������ ��� �Ұ�
	private UserRepository() {
	}
	
	/**
	 * id�� ������ �ش� User��ü�� ��ȯ
	 * @param id
	 * @return
	 */
	public User findByKey(String id) {
		return userList.get(id);
	}

}
