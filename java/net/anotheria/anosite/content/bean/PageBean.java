package net.anotheria.anosite.content.bean;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/**
 * Represents a CMS page for rendering.
 * @author lrosenberg
 */
public class PageBean {
	/**
	 * Title of the page.
	 */
	private String title;
	/**
	 * Keywords of the page if differ from site-wide keywords.
	 */
	private String keywords;
	/**
	 * Description of the page if differ from site-wide description.
	 */
	private String description;
	/**
	 * Name of the page.
	 */
	private String name;
	/**
	 * List of boxes in first column.
	 */
	private List<BoxBean> column1;
	/**
	 * List of boxes in second column.
	 */
	private List<BoxBean> column2;
	/**
	 * List of boxes in third column.
	 */
	private List<BoxBean> column3;
	/**
	 * List of boxes in the meta part (html head part).
	 */
	private List<BoxBean> metaBoxes;
	/**
	 * List of Media Links in the meta part (html head part).
	 */
	private Set<MediaLinkBean> mediaLinks;
	/**
	 * List of header boxes (before the start or the main content columns).
	 */
	private List<BoxBean> headerBoxes;
	/**
	 * List of footer boxes (after the main content columns).
	 */
	private List<BoxBean> footerBoxes;

	public PageBean() {
		column1 = new ArrayList<BoxBean>();
		column2 = new ArrayList<BoxBean>();
		column3 = new ArrayList<BoxBean>();
		
		metaBoxes = new ArrayList<BoxBean>();
		headerBoxes = new ArrayList<BoxBean>();
		footerBoxes = new ArrayList<BoxBean>();
		mediaLinks = new LinkedHashSet<MediaLinkBean>();
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

	@Override public String toString(){
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
	
	public void addMetaBoxes(List<BoxBean> boxes){
		metaBoxes.addAll(boxes);
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
	
	public List<BoxBean> getMetaBoxes() {
		return metaBoxes;
	}
	
	public void setMetaBoxes(List<BoxBean> headerBoxes) {
		this.metaBoxes = headerBoxes;
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

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<MediaLinkBean> getMediaLinks() {
		return new ArrayList<MediaLinkBean>(mediaLinks);
	}

	public void addMediaLinks(List<MediaLinkBean> mediaLinks) {
		this.mediaLinks.addAll(mediaLinks);
	}
}
