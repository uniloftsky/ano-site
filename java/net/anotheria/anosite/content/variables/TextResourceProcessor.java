package net.anotheria.anosite.content.variables;

import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.asg.metafactory.MetaFactory;
import net.anotheria.asg.metafactory.MetaFactoryException;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: h3llka
 */
public class TextResourceProcessor implements VariablesProcessor {
    private final Logger log = Logger.getLogger(TextResourceProcessor.class);
    private IASResourceDataService resourceDataService;
    public static String PREFIX = "text";
    private static final String ERROR_MESSAGE = "Wrong or unsupported variable : ";


    public TextResourceProcessor(){
        init();
    }

    //FIXME why does the processor need an init() method?
    /**
     * Init for service!
     */
    private void init() {
        try {
            resourceDataService = MetaFactory.get(IASResourceDataService.class);
        } catch (MetaFactoryException e) {
            log.fatal("TextResourceProcessor - init()",e);
        }
    }

    /**
     * Should be used in test Only! For mocking Using JMock
     * @param service - mocked service itself
     */
    @Deprecated
    protected void setResourceDataService(IASResourceDataService service) {
        this.resourceDataService = service;
    }

    @Override
    public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
        TextResource resource = getTextResourceByName(variable);
        if (resource == null) {
            log.error("TextResourceProcessor - replace method: Error! Missing key: " + variable);
            return ERROR_MESSAGE + variable;
        }
        return resource.getValue();
    }

    /**
     * Returns TextResource instance, where name equals to incoming var.
     *
     * @param var variable - name
     * @return TextResource instance.
     */
    private TextResource getTextResourceByName(String var) {
        TextResource result = null;
        try {
            List<TextResource> resources = resourceDataService.getTextResourcesByProperty(TextResource.PROP_NAME, var);
            if (resources != null && resources.size() > 0) {
                result = resources.get(0);
            }
        } catch (ASGRuntimeException e) {
            log.error("TextResourceProcessor - getTextResourceByName(" + var + ")", e);
        }
        return result;
    }
}
