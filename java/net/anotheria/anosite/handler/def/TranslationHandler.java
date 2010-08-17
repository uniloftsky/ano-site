package net.anotheria.anosite.handler.def;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.anotheria.anoplass.api.APICallContext;
import net.anotheria.anosite.content.bean.BoxBean;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.handler.AbstractBoxHandler;
import net.anotheria.anosite.handler.BoxHandlerResponse;
import net.anotheria.anosite.handler.exception.BoxProcessException;
import net.anotheria.anosite.util.AnositeConstants;
import net.anotheria.util.StringUtils;

public class TranslationHandler extends AbstractBoxHandler{

	public static final String TRANSLATION_PREFIX = "BoxTranslation.";	
	
	@Override
	public BoxHandlerResponse process(HttpServletRequest req, HttpServletResponse res, Box box, BoxBean bean) throws BoxProcessException {
		String toParse  = box.getContent();
		String[] lines = StringUtils.tokenize(toParse, '\n');
		for (String l : lines) {
			String[] bundle = StringUtils.tokenize(l, '=');
			if(bundle.length !=2)
				throw new BoxProcessException("Invalid message bundle format <" + l + "> Expected <key=message>");
				
			APICallContext.getCallContext().setAttribute(AnositeConstants.ACA_BOX_TRANSLATION_PREFIX + bundle[0], bundle[1]);
		}
		return super.process(req, res, box, bean);
	}
	
	
}
