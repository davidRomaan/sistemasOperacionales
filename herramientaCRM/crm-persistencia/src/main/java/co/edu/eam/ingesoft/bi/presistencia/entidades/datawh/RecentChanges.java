package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Date;

public class RecentChanges implements Serializable{

	private int rcId;
	private String rcTimestamp;
	private String rcTitle;
	private String rcComment;
	private int rcOldLen;
	private int rcNewLen;
	private boolean rcNew;
	private Page page;
	private User user;
	
	public RecentChanges() {
		// TODO Auto-generated constructor stub
	}
	
	public RecentChanges(int rcId, String rcTimestamp, String rcTitle, String rcComment, int rcOldLen, int rcNewLen,
			boolean rcNew, Page page, User user) {
		super();
		this.rcId = rcId;
		this.rcTimestamp = rcTimestamp;
		this.rcTitle = rcTitle;
		this.rcComment = rcComment;
		this.rcOldLen = rcOldLen;
		this.rcNewLen = rcNewLen;
		this.rcNew = rcNew;
		this.page = page;
		this.user = user;
	}
	
	
	public int getRcId() {
		return rcId;
	}
	public void setRcId(int rcId) {
		this.rcId = rcId;
	}
	public String getRcTimestamp() {
		return rcTimestamp;
	}
	public void setRcTimestamp(String rcTimestamp) {
		this.rcTimestamp = rcTimestamp;
	}
	public String getRcTitle() {
		return rcTitle;
	}
	public void setRcTitle(String rcTitle) {
		this.rcTitle = rcTitle;
	}
	public String getRcComment() {
		return rcComment;
	}
	public void setRcComment(String rcComment) {
		this.rcComment = rcComment;
	}
	public int getRcOldLen() {
		return rcOldLen;
	}
	public void setRcOldLen(int rcOldLen) {
		this.rcOldLen = rcOldLen;
	}
	public int getRcNewLen() {
		return rcNewLen;
	}
	public void setRcNewLen(int rcNewLen) {
		this.rcNewLen = rcNewLen;
	}
	public boolean isRcNew() {
		return rcNew;
	}
	public void setRcNew(boolean rcNew) {
		this.rcNew = rcNew;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
