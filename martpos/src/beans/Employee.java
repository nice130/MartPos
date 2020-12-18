package beans;

public class Employee {
	private String emCode;
	private String emPwd;
	private String emName; 
	private String lastAccessTime;
	private int logType;
	private String jobCode;
	public String getEmCode() {
		return emCode;
	}
	public void setEmCode(String emCode) {
		this.emCode = emCode;
	}
	public String getEmPwd() {
		return emPwd;
	}
	public void setEmPwd(String emPwd) {
		this.emPwd = emPwd;
	}
	public String getEmName() {
		return emName;
	}
	public void setEmName(String emName) {
		this.emName = emName;
	}
	public String getLastAccessTime() {
		return lastAccessTime;
	}
	public void setLastAccessTime(String lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}
	public int getLogType() {
		return logType;
	}
	public void setLogType(int logType) {
		this.logType = logType;
	}
	public String getJobCode() {
		return jobCode;
	}
	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}
	
}