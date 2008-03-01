package net.anotheria.anosite.content.variables.element;

public class StaticElement extends ContentElement {

	public StaticElement(String elementText){
		super(elementText);
	}
	
	public boolean isDynamic() {
		return false;
	}

}
