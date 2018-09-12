package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="DIMENSION_USER")
@NamedQuery(
		name = User.BUSCAR_USER, query="SELECT u FROM User u WHERE u.userName = ?1")
public class User implements Serializable {
	
	public static final String BUSCAR_USER = "User.buscar";

	@Id
	@Column(name="user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	
	@Column(name="user_name")
	private String userName;
	
	@Column(name="user_real_name")
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
