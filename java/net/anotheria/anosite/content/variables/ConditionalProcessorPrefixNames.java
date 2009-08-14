package net.anotheria.anosite.content.variables;

import net.anotheria.util.StringUtils;

/**
 * @author: h3llka
 */

/**
 * Represents enum with all prefix names which are used in Conditional processor.
 */
public enum ConditionalProcessorPrefixNames {

    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    //Currently  hacked! --  due to 'if' operator  use ..... sorry!
    iF(DefinitionPrefixes.PREFIX_IF) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            String[] ret = StringUtils.tokenize(defVal, ';');
            return Boolean.TRUE.toString().equals(variable) ? ret[0] : ret.length > 1 ? ret[1] : "";
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    ifNot(DefinitionPrefixes.PREFIX_IF_NOT) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return !Boolean.TRUE.toString().equals(variable) ? defVal : "";
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    present(DefinitionPrefixes.PREFIX_PRESENT) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return variable.length() > 0 ? defVal : "";
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    notPresent(DefinitionPrefixes.PREFIX_NOT_PRESENT) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return !(variable.length() > 0) ? defVal : "";
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    equals(DefinitionPrefixes.PREFIX_EQUALS) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return String.valueOf(!(variable == null || defVal == null) && variable.equals(defVal));
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    notEquals(DefinitionPrefixes.PREFIX_NOT_EQUALS) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return String.valueOf(!(!(variable == null || defVal == null) && variable.equals(defVal)));
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    inRange(DefinitionPrefixes.PREFIX_IN_RANGE) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return String.valueOf(inRange(variable, defVal));
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    greaterThan(DefinitionPrefixes.PREFIX_GREATERTHEN) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return String.valueOf(checkRelations(variable, defVal, Relations.greaterThanRelation));
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    greaterEqual(DefinitionPrefixes.PREFIX_GREATEREQUAL) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return String.valueOf(checkRelations(variable, defVal, Relations.greaterEqualRelation));
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    lessThan(DefinitionPrefixes.PREFIX_LESSTHAN) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return String.valueOf(checkRelations(variable, defVal, Relations.lessThanRelation));
        }},
    /**
     * @see ConditionalProcessorPrefixNames constructor
     */
    lessEqual(DefinitionPrefixes.PREFIX_LESSEQUAL) {
        @Override
        public String executeReplacement(String variable, String defVal) {
            return String.valueOf(checkRelations(variable, defVal, Relations.lessEqualRelation));
        }};

    private String prefixName;

    /**
     * Constructor
     *
     * @param stringValue string representation
     */
    ConditionalProcessorPrefixNames(String stringValue) {
        this.prefixName = stringValue;
    }

    public String getPrefixName() {
        return prefixName;
    }

    /**
     * Simply making variable replacement
     *
     * @param variable value
     * @param defVal   default value
     * @return replaced value
     */
    public abstract String executeReplacement(String variable, String defVal);

    private static boolean inRange(String s1, String s2) {
        if (s1 == null || s2 == null)
            return false;
        String[] rangeTokens = StringUtils.tokenize(s1, '-');
        if (rangeTokens.length != 2)
            return false;
        try {
            int rangeMin = Integer.parseInt(rangeTokens[0]);
            int rangeMax = Integer.parseInt(rangeTokens[1]);
            int value = Integer.parseInt(s2);
            return rangeMin <= value && value <= rangeMax;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    private static boolean checkRelations(String var, String var2, Relations option) {
        if ((var == null || var2 == null) && (!var.isEmpty() || !var2.isEmpty()))
            return false;
        try {
            int value = Integer.valueOf(var);
            int value2 = Integer.valueOf(var2);
            boolean result = false;
            switch (option) {
                case greaterThanRelation:
                    result = value > value2;
                    break;
                case greaterEqualRelation:
                    result = value >= value2;
                    break;
                case lessThanRelation:
                    result = value < value2;
                    break;
                case lessEqualRelation:
                    result = value <= value2;
            }
            return result;
        } catch (NumberFormatException ignored) {
            return false;
        }
    }

    /**
     * Simplest enum of relations between 2 numbers. etc.
     */
    private static enum Relations {
        greaterThanRelation, greaterEqualRelation, lessThanRelation, lessEqualRelation
    }
}
