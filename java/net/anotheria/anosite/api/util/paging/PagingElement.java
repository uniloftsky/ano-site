package net.anotheria.anosite.api.util.paging;

public abstract class PagingElement {
	public abstract boolean isActive();
	
	public abstract boolean isSeparator();
	
	public abstract boolean isLinked();
		
	public abstract String getPagingParameter();
	
	public abstract String getCaption();
	
}
