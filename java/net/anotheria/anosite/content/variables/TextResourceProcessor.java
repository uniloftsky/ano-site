package net.anotheria.anosite.content.variables;

import net.anotheria.anosite.content.variables.helper.TextResourceProcessorHelper;
import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.asg.exception.ASGRuntimeException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * TextResourceProcessor.
 *
 * @author: h3llka
 */
public class TextResourceProcessor implements VariablesProcessor {
	/**
	 * Logger instance.
	 */
	private final Logger log = Logger.getLogger(TextResourceProcessor.class);
	/**
	 * Processor prefix.
	 */
	public static final String PREFIX = "text";
	/**
	 * Error message.
	 */
	private static final String ERROR_MESSAGE = "Wrong or unsupported variable : ";

	/**
	 * Constructor.
	 */
	public TextResourceProcessor() {
	}

	@Override
	public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
		try {
			TextResource resource = TextResourceProcessorHelper.getTextResourceByName(variable);
			if (resource == null) {
				log.error("TextResourceProcessor - replace method: Error! Missing key: " + variable);
				return ERROR_MESSAGE + variable;
			}
			return resource.getValue();
		} catch (ASGRuntimeException e) {
			log.error("TextResourceProcessor - replace method: Error! Missing key: " + variable, e);
			return ERROR_MESSAGE + variable;
		}
	}


}
