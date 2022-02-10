package project_1.Authentication;

import project_1.User.User;
import project_1.User.UserRepository;

/**
 * 로그인 인증, 레포트 권한 인증 메소드
 * @author user
 *
 */
public class Authority {
	private UserRepository ur = UserRepository.getInstance();
	private User user = null;

	/**
	 * 로그인인증
	 * @param id
	 * @param password
	 * @return
	 */
	public boolean loginAuthenticate (String id, String password) {
		if((user = ur.findByKey(id))!=null) {
			return (user.getId().equals(id)) && (user.getPassword().equals(password));
		}//end if
		return false;
	}//loginAuthenticate

	/**
	 * 레포트 권한 인증
	 * @param id
	 * @return
	 */
	public boolean reportAuthenticate (String id) {
		if((user = ur.findByKey(id))!=null) {
			return user.isReportAuthority();
		}//end if
		return false;
	}//reportAuthenticate 
}//class