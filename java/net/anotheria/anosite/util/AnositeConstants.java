package net.anotheria.anosite.util;

public class AnositeConstants {
	public static final String BEAN_PREFIX = "anosite.";
	//AA - application attribute
	public static final String AA_PREFIX = "aa.";
	
	//SA - session attribute
	public static final String SA_PREFIX = "sa.";
	
	//RA - request attribute
	public static final String RA_PREFIX = "ra.";
	
	public static final String AA_ANOSITE_RANDOM = BEAN_PREFIX+AA_PREFIX+".random";
	
	public static final String SA_LANGUAGE = BEAN_PREFIX+SA_PREFIX+".lang";
	public static final String SA_LOCALE = BEAN_PREFIX+SA_PREFIX+".locale";
	public static final String SA_EDIT_MODE_FLAG = BEAN_PREFIX+SA_PREFIX+".editModeFlag";
	
	public static final String RA_CURRENT_URI = BEAN_PREFIX+RA_PREFIX+".currentUri";
	
	
	public static final String PARAM_LANGUAGE = "lang";
	
	public static final String V_LANG_DE = "DE";
	public static final String V_LANG_EN = "EN";
	
	public static final String FLAG_XML_REQUEST = "xmlrequest";


	public static final String AS_MOSKITO_SUBSYSTEM = "ano-site";
}
