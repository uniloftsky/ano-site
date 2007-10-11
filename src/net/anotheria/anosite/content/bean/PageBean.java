package net.anotheria.anosite.content.bean;

import java.util.ArrayList;
import java.util.List;

public class PageBean {
	private String title;
	private String name;
	private List<BoxBean> column1;
	private List<BoxBean> column2;
	private List<BoxBean> column3;
	
	private List<BoxBean> headerBoxes;
	private List<BoxBean> footerBoxes;

	public PageBean() {
		column1 = new ArrayList<BoxBean>();
		column2 = new ArrayList<BoxBean>();
		column3 = new ArrayList<BoxBean>();
		
		headerBoxes = new ArrayList<BoxBean>();
		footerBoxes = new ArrayList<BoxBean>();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addBoxToColumn1(BoxBean aBox){
		column1.add(aBox);
	}
	
	public void addBoxToColumn2(BoxBean aBox){
		column2.add(aBox);
	}

	public void addBoxToColumn3(BoxBean aBox){
		column3.add(aBox);
	}

	public String toString(){
		return getName()+" c1: "+column1.size()+", c2: "+column2.size()+", c3: "+column3.size()+" boxes.";
	}

	public List<BoxBean> getColumn1() {
		return column1;
	}

	public void addColumn1(List<BoxBean> boxes) {
		column1.addAll(boxes);
	}

	public void addColumn2(List<BoxBean> boxes) {
		column2.addAll(boxes);
	}

	public void addColumn3(List<BoxBean> boxes) {
		column3.addAll(boxes);
	}
	
	public void addHeaderBoxes(List<BoxBean> boxes){
		headerBoxes.addAll(boxes);
	}

	public void addFooterBoxes(List<BoxBean> boxes){
		footerBoxes.addAll(boxes);
	}

	public void setColumn1(List<BoxBean> column1) {
		this.column1 = column1;
	}

	public List<BoxBean> getColumn2() {
		return column2;
	}

	public void setColumn2(List<BoxBean> column2) {
		this.column2 = column2;
	}

	public List<BoxBean> getColumn3() {
		return column3;
	}

	public void setColumn3(List<BoxBean> column3) {
		this.column3 = column3;
	}

	public List<BoxBean> getFooterBoxes() {
		return footerBoxes;
	}

	public void setFooterBoxes(List<BoxBean> footerBoxes) {
		this.footerBoxes = footerBoxes;
	}

	public List<BoxBean> getHeaderBoxes() {
		return headerBoxes;
	}

	public void setHeaderBoxes(List<BoxBean> headerBoxes) {
		this.headerBoxes = headerBoxes;
	}
}
