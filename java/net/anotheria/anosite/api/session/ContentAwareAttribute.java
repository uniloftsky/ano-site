package net.anotheria.anosite.api.session;

/**
 * Should be implemented by all Objects which are a representation of a content object of some kind.
 * @author another
 *
 */
public interface ContentAwareAttribute {
	
	/**
	 * Called first, if this returns true, the object will be removed from any cache its in.
	 * @return
	 */
	public boolean deleteOnChange();
	
	/**
	 * If delete on change returns false this method is called.
	 * @param documentName
	 * @param documentId
	 */
	public void notifyContentChange(String documentName, String documentId);
}
