package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name="HECHO_RECENT_CHANGES")
public class RecentChanges implements Serializable{

	@Id
	@Column(name="rc_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int rcId;
	
	@Column(name="rc_timestamp")
	@Temporal(TemporalType.DATE)
	private Date rcTimestamp;
	
	@Column(name="rc_title")
	private String rcTitle;
	
	@Column(name="rc_comment")
	private String rcComment;
	
	@Column(name="rc_oldlen")
	private int rcOldLen;
	
	@Column(name="rc_newlen")
	private int rcNewLen;
	
	@Column(name="rc_new")
	private boolean rcNew;
	
	@Column(name="rc_pageid")
	private int pageId;
	
	@JoinColumn(name="id_user")
	@ManyToOne(cascade = {CascadeType.MERGE})
	private User user;
	
	public RecentChanges() {
		// TODO Auto-generated constructor stub
	}

	public RecentChanges(int rcId, Date rcTimestamp, String rcTitle, String rcComment, int rcOldLen, int rcNewLen,
			boolean rcNew, int pageId, User user) {
		super();
		this.rcId = rcId;
		this.rcTimestamp = rcTimestamp;
		this.rcTitle = rcTitle;
		this.rcComment = rcComment;
		this.rcOldLen = rcOldLen;
		this.rcNewLen = rcNewLen;
		this.rcNew = rcNew;
		this.pageId = pageId;
		this.user = user;
	}

	public int getRcId() {
		return rcId;
	}

	public void setRcId(int rcId) {
		this.rcId = rcId;
	}

	public Date getRcTimestamp() {
		return rcTimestamp;
	}

	public void setRcTimestamp(Date rcTimestamp) {
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

	public int getPageId() {
		return pageId;
	}

	public void setPageId(int pageId) {
		this.pageId = pageId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	
	
}
