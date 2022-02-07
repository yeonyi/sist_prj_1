package project1;

public class Authority {
	private UserRepository ur = UserRepository.getInstance();
	private User user = null;

	public boolean loginAuthenticate (String id, String password) {
		if((user = ur.findByKey(id))!=null) {
			return (user.getId() == id) && (user.getPassword()==password);
		}//end if
		return false;
	}//loginAuthenticate

	public boolean reportAuthenticate (String id) {
		if((user = ur.findByKey(id))!=null) {
			return user.isReportAuthority();
		}//end if
		return false;
	}//reportAuthenticate 
}//class