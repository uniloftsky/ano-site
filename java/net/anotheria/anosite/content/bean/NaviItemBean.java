package net.anotheria.anosite.content.bean;

import java.util.ArrayList;
import java.util.List;

public class NaviItemBean {
	private boolean popup;
	private String name;
	private String link;
	private String title;
	private boolean selected;
	
	private List<NaviItemBean> subNavi;
	
	public NaviItemBean(){
		subNavi = new ArrayList<NaviItemBean>();
	}
	
	public void addNaviItem(NaviItemBean toAdd){
		subNavi.add(toAdd);
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isPopup() {
		return popup;
	}
	public void setPopup(boolean popup) {
		this.popup = popup;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String toString(){
		return getName()+": "+getLink();
	}

	public boolean isHasChilds() {
		return subNavi!=null && subNavi.size()>0;
	}

	public List<NaviItemBean> getSubNavi() {
		return subNavi;
	}

	public void setSubNavi(List<NaviItemBean> subNavi) {
		this.subNavi = subNavi;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
