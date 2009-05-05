package net.anotheria.anosite.api.util.paging;

/**
 * A paging element is an element in the set of paging links returned by the PagingControl
 * @author lrosenberg
 */
public abstract class PagingElement {
	/**
	 * Returns true if the element is active (selected)
	 * @return
	 */
	public abstract boolean isActive();
	
	/**
	 * Returns true if the element is a separator and not a link.
	 * @return
	 */
	public abstract boolean isSeparator();
	
	/**
	 * Returns true if the element is a link
	 * @return
	 */
	public abstract boolean isLinked();
		
	/**
	 * Returns the paging parameter to apply to the link
	 * @return
	 */
	public abstract String getPagingParameter();
	
	/**
	 * Return the caption of the element.
	 * @return
	 */
	public abstract String getCaption();
	
}
