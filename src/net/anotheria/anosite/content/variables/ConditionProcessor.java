package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.util.StringUtils;

public class ConditionProcessor implements VariablesProcessor{

	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		if(DefinitionPrefixes.PREFIX_IF.equals(prefix)){
			String[] ret = StringUtils.tokenize(defValue, ';');
			return TRUE.equals(variable)? ret[0] : ret.length > 1? ret[1]:"";
		}
		if(DefinitionPrefixes.PREFIX_IF_NOT.equals(prefix))
			return !TRUE.equals(variable)? defValue: "";
		
		if(DefinitionPrefixes.PREFIX_PRESENT.equals(prefix))
			return isPresent(variable)? defValue:"";
		if(DefinitionPrefixes.PREFIX_NOT_PRESENT.equals(prefix))
			return !isPresent(variable)? defValue:"";
		
		if(DefinitionPrefixes.PREFIX_EQUALS.equals(prefix))
			return isEquals(variable, defValue)? TRUE: FALSE;
		if(DefinitionPrefixes.PREFIX_NOT_EQUALS.equals(prefix))
			return !isEquals(variable, defValue)? TRUE: FALSE;
		if(DefinitionPrefixes.PREFIX_IN_RANGE.equals(prefix))
			return inRange(variable, defValue)? TRUE: FALSE;
		
		return "";
	}
	
	protected boolean isPresent(String variable){
		return variable.length() > 0; 
	}
	
	protected boolean isEquals(String s1, String s2){
		if(s1 == null || s2 == null)
			return false;
		return s1.equals(s2);
	}
	
	protected boolean inRange(String s1, String s2){
		if(s1 == null || s2 == null)
			return false;
		String[] rangeTokens = StringUtils.tokenize(s1, '-');
		if(rangeTokens.length != 2)
			return false;
		try{
			int rangeMin = Integer.parseInt(rangeTokens[0]);
			int rangeMax = Integer.parseInt(rangeTokens[1]);
			int value = Integer.parseInt(s2);
			return rangeMin <= value && value <= rangeMax;
		}catch(NumberFormatException e){
		}
		return false;
	}
}
