package project_1;

/**
 * UserClass
 * @author 강명준
 *
 */
public class User {
	private String id;
	private String password;
	private boolean reportAuthority; //report문서 생성 권한
	
	
	
	public User(String id, String password, boolean reportAuthority) {
		this.id = id;
		this.password = password;
		this.reportAuthority = reportAuthority;
	}
	
	//getter
	public String getId() {
		return id;
	}
	public String getPassword() {
		return password;
	}
	public boolean isReportAuthority() {
		return reportAuthority;
	}
	


}
