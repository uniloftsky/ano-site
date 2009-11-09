package net.anotheria.anosite.content.variables;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ConditionProcessor implements VariablesProcessor {
    private final Logger log = Logger.getLogger(ConditionProcessor.class);

    @Override
    public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
        //sorry!  next If block -- is just the stupid hack!  due  to 'if' prefix name (
        if (ConditionalProcessorPrefixNames.iF.getPrefixName().equals(prefix)) {
            return ConditionalProcessorPrefixNames.iF.executeReplacement(variable, defValue);
        } else {
            try {
                return ConditionalProcessorPrefixNames.valueOf(ConditionalProcessorPrefixNames.class, prefix).executeReplacement(variable, defValue);
            } catch (Exception ignored) {
                log.error("An exceptions has been occured whicle trying to replace variable. where prefix=" + prefix + " variable=" + variable + " defaultvalue=" + defValue, ignored);
            }

        }

        return "";
    }

}
