package project_1;

/**
 * UserClass
 * @author °­¸íÁØ
 *
 */
public class User {
	private String id;
	private String password;
	private boolean reportAuthority; //report¹®¼­ »ý¼º ±ÇÇÑ
	
	
	
	public User(String id, String password, boolean reportAuthority) {
		this.id = id;
		this.password = password;
		this.reportAuthority = reportAuthority;
	}//User
	
	//getter
	public String getId() {
		return id;
	}//getId
	public String getPassword() {
		return password;
	}//getPassword
	public boolean isReportAuthority() {
		return reportAuthority;
	}//isReportAuthority


}//class