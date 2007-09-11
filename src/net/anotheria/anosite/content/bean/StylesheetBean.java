package net.anotheria.anosite.content.bean;

public class StylesheetBean {
	private String link;
	
	public StylesheetBean(String styleObjectId){
		link = "styles/"+styleObjectId+".css";
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
