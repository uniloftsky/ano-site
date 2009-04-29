 package net.anotheria.anosite.content.variables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.anotheria.util.StringUtils;

public class _VariablesUtility {
	public static final String DEFAULT_VALUE  = "*UNSET*";
	public static final char LINE_DELIMITER = '\n';
	public static final char WORD_DELIMITER = ' ';
	
	public static final char TAG_START = '{';
	public static final char TAG_END = '}';
	
	public static final String QUOTE = "\"";
	public static final char ESCAPE_CHAR = '\\';
	
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
		
		p = new ConditionProcessor();
		defaultProcessors.put(DefinitionPrefixes.PREFIX_IF, p);
		defaultProcessors.put(DefinitionPrefixes.PREFIX_IF_NOT, p);
		defaultProcessors.put(DefinitionPrefixes.PREFIX_PRESENT, p);
		defaultProcessors.put(DefinitionPrefixes.PREFIX_NOT_PRESENT, p);
		defaultProcessors.put(DefinitionPrefixes.PREFIX_EQUALS, p);
		defaultProcessors.put(DefinitionPrefixes.PREFIX_NOT_EQUALS, p);
	}
	
	
	public static void addProcessor(String prefix, VariablesProcessor processor){
		defaultProcessors.put(prefix, processor);
	}

	public static Map<String, VariablesProcessor> getDefaultProcessors(){
		 HashMap<String, VariablesProcessor> ret = new HashMap<String, VariablesProcessor>();
		 ret.putAll(defaultProcessors);
		 return ret;
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
//		String lines[] = StringUtils.tokenize(myS, '\n');
//		Is tokenizing by line needed?
//		String lines[] = new String[]{myS};
//		String totalRet = "";
//		for (String s : lines){
			 
			List<String> vars = StringUtils.extractSuperTags(myS, '{', '}',ESCAPE_CHAR);
			Map<String, String> varValues = new HashMap<String, String>();
		
//			System.out.println("REPLACE VARIABLES:");
//			System.out.println("Source: " + src);
//			int i = 0;
			for (String var : vars){
//				i++;
//				System.out.println("var"+i+": " + var);
				String sVar = StringUtils.surroundWith(replaceVariables(req, StringUtils.strip(var, 1, 1), processors),'{','}');
//				System.out.println("var"+i+" with replaced subvariables: " + sVar);
				String varValue = replaceInWord(req, sVar, processors);
//				System.out.println("varValue"+i+":" + varValue);
				if (varValue!=null && !varValue.equals(var))
					varValues.put(var, varValue);
			}
		
			String ret = myS;
			for (String k : varValues.keySet()){
				String v = varValues.get(k);
				int i = 0;
				while(ret.indexOf(k)!=-1){
					i++;
					ret = StringUtils.replaceOnce(ret, k, v);
				}
				System.out.println("Replacing: " + k + " replaced with " + v + " " + i+" times!");
			}
			return ret;
//			if (totalRet.length()>0)
//				totalRet += "\n";
//			totalRet += ret;
//		}
	
////		System.out.println("Result: " + totalRet);
//		return totalRet;
	}

	private static String replaceInWord(HttpServletRequest req, String word, Map<String,VariablesProcessor> processors){
//		System.out.println("REPLACE IN WORD:");
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
		
		String tokens[] = StringUtils.tokenize(varName, ':',ESCAPE_CHAR);
//		System.out.println("===== VarName:  " + varName);
		if (tokens.length<2){
			return "Wrong format "+varName+" expected: {prefix:varname[:default value]}";
		}
		String prefix = tokens[0];
//		System.out.println("Prefix: " + prefix);
		String var = tokens[1];
//		System.out.println("Var: " + var);
		String defaultValue = DEFAULT_VALUE;
		if (tokens.length>2)
			defaultValue = tokens[2];
//		System.out.println("DEfault value: " + defaultValue);
		VariablesProcessor processor = processors.get(prefix);
		if (processor==null)
			return word;
		String ret = processor.replace(prefix, var, defaultValue, req);
//		System.out.println("Replaced word: " + ret);
		return ret;
	}
	
	
	
	public static void main(String a[]){
		
		String t1 = "Hallo mein {c:spacer} und {\"test\"} und {c:euro}";
		System.out.println(t1+" -> "+replaceVariables(null, t1));
		test2();
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
	
	public static void test2(){
		String src = "c{if:{present:1:false}:{present:2:present}}c";
		String res = _VariablesUtility.replaceVariables(null, src);
		System.out.println(src + "\t=====>\t" + res);
		System.out.println("Expected: cc");
		System.out.println("---------------------------------------");
		src = "c{if:{present:1:true}:{present:2:present}}c";
		res = _VariablesUtility.replaceVariables(null, src);
		System.out.println(src + "\t=====>\t" + res);
		System.out.println("Expected: cpresentc");
		System.out.println("---------------------------------------");
		src = "c{if:{present:1:true}:{present:2:mumu}}c";
		res = _VariablesUtility.replaceVariables(null, src);
		System.out.println(src + "\t=====>\t" + res);
		System.out.println("Expected: cmumuc");
		System.out.println("---------------------------------------");
		src = "c{if:{notPresent::true}:notPresent}c";
		res = _VariablesUtility.replaceVariables(null, src);
		System.out.println(src + "\t=====>\t" + res);
		System.out.println("Expected: cnotPresentc");
		System.out.println("---------------------------------------");
		src = "c{equals:{present:1:true}:{present:2:mumu}}c";
		res = _VariablesUtility.replaceVariables(null, src);
		System.out.println(src + "\t=====>\t" + res);
		System.out.println("Expected: cfalsec");
		System.out.println("---------------------------------------");
		src = "c{notEquals:{present:1:true}:{present:2:mumu}}c";
		res = _VariablesUtility.replaceVariables(null, src);
		System.out.println(src + "\t=====>\t" + res);
		System.out.println("Expected: ctruec");
		System.out.println("---------------------------------------");
		src = "{present:1:Some text when something\npresente!}";
		res = _VariablesUtility.replaceVariables(null, src);
		System.out.println(src + "\t=====>\t" + res);
		System.out.println("Expected: Some text when something\npresente!");
		System.out.println("---------------------------------------");
		src = "{if:{equals:2:2}:Hello3!\nI Like to meet with you!}";
		res = _VariablesUtility.replaceVariables(null, src);
		System.out.println(src + "\t=====>\t" + res);
		System.out.println("Expected: Hello3!\nI Like to meet with you!");
		
	}

}
