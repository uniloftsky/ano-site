package net.anotheria.anosite.content.variables;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: h3llka
 */
public class CalendarProcessor implements VariablesProcessor {
    private static final Logger log = Logger.getLogger(CalendarProcessor.class);
    private static final String SEPARATOR = ":";

    @Override
    public String replace(String prefix, String variable, String defValue, HttpServletRequest req) {
        try {
            String var = variable.contains(SEPARATOR) ? variable.substring(0, variable.indexOf(SEPARATOR)) : variable;
            String format = variable.contains(SEPARATOR) ? variable.substring(variable.indexOf(SEPARATOR) + 1, variable.length()) : null;
            CalendarVariableNames variableName = CalendarVariableNames.valueOf(CalendarVariableNames.class, var);
            return variableName.getVariableValue(format);
        } catch (NullPointerException e) {
            log.error("Excaprion has been occeread while trying to use CallendarProcessor replace method", e);
            log.debug("incoming variable = " + variable);
            return defValue;
        }
        catch (IllegalArgumentException e) {
            log.error("Excaprion has been occeread while trying to use CallendarProcessor replace method", e);
            log.debug("incoming variable = " + variable);
            return defValue;
        }
    }
}

