package net.anotheria.anosite.content.variables.element;

import java.util.List;


public class DynamicElement extends ContentElement {

	private String prefix;
	
	private List<ContentElement> variableIndex;
	private List<ContentElement> defValueIndex;
	
	public DynamicElement(String elementText, String prefix, List<ContentElement> variableIndex, List<ContentElement> defValueIndex){
		super(elementText);
		this.prefix = prefix;
		this.variableIndex = variableIndex;
		this.defValueIndex = defValueIndex;
	}
	
	public boolean isDynamic() {
		return true;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public List<ContentElement> getVariableIndex() {
		return variableIndex;
	}

	public void setVariableIndex(List<ContentElement> variableIndex) {
		this.variableIndex = variableIndex;
	}

	public List<ContentElement> getDefValueIndex() {
		return defValueIndex;
	}

	public void setDefValueIndex(List<ContentElement> defValueIndex) {
		this.defValueIndex = defValueIndex;
	}

}
