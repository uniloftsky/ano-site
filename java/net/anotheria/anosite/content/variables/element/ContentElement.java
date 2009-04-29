package net.anotheria.anosite.content.variables.element;

public abstract class ContentElement {
	
	private String elementText;
	
	public ContentElement(String elementText){
		this.elementText = elementText;
	}
	
	public String getElementText() {
		return elementText;
	}
	public void setElementText(String elementText) {
		this.elementText = elementText;
	}
	public abstract boolean isDynamic();
	
}
