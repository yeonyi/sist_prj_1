package project1;

public class Authority {
	private UserRepository ur = UserRepository.getInstance();
	private User user = null;

	public boolean loginAuthenticate (String id, String password) {
		if((user = ur.findByKey(id))!=null) {
			return (user.getId().equals(id)) && (user.getPassword().equals(password));
		}
		return false;
	}//loginAuthenticate

	public boolean reportAuthenticate (String id) {
		if((user = ur.findByKey(id))!=null) {
			return user.isReportAuthority();
		}
		return false;
	}//reportAuthenticate 
}//class