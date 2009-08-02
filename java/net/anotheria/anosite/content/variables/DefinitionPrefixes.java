package net.anotheria.anosite.content.variables;

/**
 * This helper class defines prefixes useable for variables in the variable processor.
 * @author lrosenberg
 *
 */
public final class DefinitionPrefixes {
	/**
	 * Http parameter.
	 */
	public static final String PREFIX_PARAMETER = "p";
	/**
	 * Http request attribute.
	 */
	public static final String PREFIX_REQUEST_ATTRIBUTE = "ra";
	/**
	 * Attribute in the internal scope of the api call context.
	 */
	public static final String PREFIX_API_CALL_CONTEXT_ATTRIBUTE = "api_cca";
	/**
	 * (Http) Session attribute.
	 */
	public static final String PREFIX_SESSION_ATTRIBUTE = "sa";
	/**
	 * (Http) Session attribute which is one time readable.
	 */
	public static final String PREFIX_SESSION_AND_DELETE_ATTRIBUTE = "sda";
	/**
	 * Servlet context attribute.
	 */
	public static final String PREFIX_CONTEXT_ATTRIBUTE = "ca";
	/**
	 * Api session attribute.
	 */
	public static final String PREFIX_API_SESSION_ATTRIBUTE = "api_sa";
	/**
	 * Box attribute.
	 */
	public static final String PREFIX_BOX_ATTRIBUTE = "ba";
	/**
	 * Constant, performed by constants processor.
	 */
	public static final String PREFIX_CONSTANT = "c";
	/**
	 * Link to an image.
	 */
	public static final String PREFIX_IMAGE_LINK ="imagelink";
	/**
	 * Link to a file.
	 */
	public static final String PREFIX_FILE_LINK ="filelink";
	/**
	 * Image (including tags).
	 */
	public static final String PREFIX_IMAGE = "image";
	
	public static final String PREFIX_IF = "if";
	public static final String PREFIX_IF_NOT = "ifNot";
	public static final String PREFIX_PRESENT = "present";
	public static final String PREFIX_NOT_PRESENT = "notPresent";
	public static final String PREFIX_EQUALS = "equals";
	public static final String PREFIX_NOT_EQUALS = "notEquals";
	public static final String PREFIX_IN_RANGE = "inRange";
	
	private DefinitionPrefixes(){
		//prevent instantiation.
	}
}
