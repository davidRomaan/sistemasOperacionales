package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

	private int userId;
	private String userName;
	private String userRealName;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(int userId, String userName, String userRealName) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.userRealName = userRealName;
	}
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserRealName() {
		return userRealName;
	}
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}
	
	
	
}
