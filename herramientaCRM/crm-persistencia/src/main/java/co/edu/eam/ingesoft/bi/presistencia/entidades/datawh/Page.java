package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;

public class Page implements Serializable {
	
	private int pageId;
	private String text;
	
	
	public Page() {
		// TODO Auto-generated constructor stub
	}
	
	public Page(int pageId, String text) {
		super();
		this.pageId = pageId;
		this.text = text;
	}
	
	public int getPageId() {
		return pageId;
	}
	public void setPageId(int pageId) {
		this.pageId = pageId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	

}
