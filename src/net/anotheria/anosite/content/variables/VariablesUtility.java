package net.anotheria.anosite.content.variables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.util.StringUtils;

public class VariablesUtility {
	public static final char LINE_DELIMITER = '\n';
	public static final char WORD_DELIMITER = ' ';
	
	public static final char TAG_START = '{';
	public static final char TAG_END = '}';
	
	public static final String QUOTE = "\"";
	
	private static Map<String, VariablesProcessor> defaultProcessors = new HashMap<String, VariablesProcessor>();
	
	static{
		defaultProcessors.put(DefinitionPrefixes.PREFIX_CONSTANT, new ConstantsProcessor());
		defaultProcessors.put(DefinitionPrefixes.PREFIX_PARAMETER, new ParameterProcessor());
		VariablesProcessor p = new AttributeProcessor();
		defaultProcessors.put(DefinitionPrefixes.PREFIX_REQUEST_ATTRIBUTE, p);
		defaultProcessors.put(DefinitionPrefixes.PREFIX_SESSION_ATTRIBUTE, p);
		defaultProcessors.put(DefinitionPrefixes.PREFIX_CONTEXT_ATTRIBUTE, p);
		
		defaultProcessors.put(DefinitionPrefixes.PREFIX_IMAGE_LINK, new ImageLinkProcessor());
		defaultProcessors.put(DefinitionPrefixes.PREFIX_FILE_LINK, new FileLinkProcessor());
		defaultProcessors.put(DefinitionPrefixes.PREFIX_IMAGE, new ImageProcessor());
	}
	
	
	public static void addProcessor(String prefix, VariablesProcessor processor){
		defaultProcessors.put(prefix, processor);
	}
	/**
	 * Replaces variables in string s with attributes from the request req. If dummy is not NULL it will be inserted instead of 
	 * variable value.
	 * @param req
	 * @param s
	 * @param dummy
	 * @return
	 */
	public static String replaceVariables(HttpServletRequest req, String src){
		return replaceVariables(req, src, defaultProcessors);
	}
	
	public static String replaceVariables(HttpServletRequest req, String src, Map<String,VariablesProcessor> processors){

		String myS = StringUtils.removeChar(src, '\r');
		String lines[] = StringUtils.tokenize(myS, '\n');
		
		String totalRet = "";
		for (String s : lines){
			 
			List<String> vars = StringUtils.extractTags(s, '{', '}');
			Map<String, String> varValues = new HashMap<String, String>();
		
			for (String var : vars){
				String varValue = replaceInWord(req, var, processors);
				if (varValue!=null && !varValue.equals(var))
					varValues.put(var, varValue);
			}
		
			String ret = s;
			for (String k : varValues.keySet()){
				String v = varValues.get(k);
				while(ret.indexOf(k)!=-1)
					ret = StringUtils.replaceOnce(ret, k, v);
			}

			if (totalRet.length()>0)
				totalRet += "\n";
			totalRet += ret;
		}
		return totalRet;
	}

	private static String replaceInWord(HttpServletRequest req, String word, Map<String,VariablesProcessor> processors){
		String varName = StringUtils.strip(word, 1, 1);
		if (varName!=null){
			char c = varName.charAt(0);
			if (c=='\t' || c==' ' || c=='\n' || c=='\r'){
				return null;
			}
		}
		
		if (varName!=null){
			if (varName.startsWith(QUOTE) && varName.endsWith(QUOTE))
				return StringUtils.strip(varName, 1, 1);
		}
		
		String defaultValue  = "*UNSET*";
		String tokens[] = StringUtils.tokenize(varName, ':');
		if (tokens.length<2){
			return "Wrong format "+varName+" expected: {prefix:varname[:default value]}";
		}
		String prefix = tokens[0];
		String var = tokens[1];
		if (tokens.length>2)
			defaultValue = tokens[2];
		VariablesProcessor processor = processors.get(prefix);
		if (processor==null)
			return word;
		String ret = processor.replace(prefix, var, defaultValue, req);
		return ret;
	}
	
	
	
	public static void main(String a[]){
		
		String t1 = "Hallo mein {c:spacer} und {\"test\"} und {c:euro}";
		System.out.println(t1+" -> "+replaceVariables(null, t1));
		
		//System.out.println(replaceVariables(null, test, "VAR"));
	}
	
	public static void test1(){
		String src =""+
		"function callShopUrl (mode) {\n"+
		"\n"+
	    "    var rdurl = '{interception.sourceURL}'; \n"+
		" var ctx = '{interception.context}'; \n"+
	    "    var link = '/interception/remember?ctx='+ctx+'&rdurl='+rdurl+'&save=false&'; \n"+
		" if ( mode ) { \n"+
		"	window.location.href		= link+'yes=ja'; \n"+
		"} else { \n"+
		"	window.location.href		= link+'no=nein'; \n"+
		"} \n"+ 
		"} \n";
		System.out.println(src);
		System.out.println(" ==                 =================== ");
		System.out.println(replaceVariables(null, src));
	}

}
