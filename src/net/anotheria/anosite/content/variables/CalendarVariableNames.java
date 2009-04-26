package net.anotheria.anosite.content.variables;


import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Represent Variable names for CallendarProcessor.
 */
public enum CalendarVariableNames {

    /**
     * @see net.anotheria.anosite.content.variables.CalendarVariableNames Constructor
     */
    currentDate("currentDate", CalendarVariableNames.DATE_DEFAULT_FORMAT) {

        @Override
        List<String> getAllowedFormats() {
            return null;
        }},

    /**
     * @see net.anotheria.anosite.content.variables.CalendarVariableNames Constructor
     */
    currentDay("currentDay", CalendarVariableNames.DAY_DEFAULT_FORMAT) {

        @Override
        List<String> getAllowedFormats() {
            return getAllowedDayFormats();
        }},

    /**
     * @see net.anotheria.anosite.content.variables.CalendarVariableNames Constructor
     */
    currentMonth("currentMonth", CalendarVariableNames.MONTH_DEFAULT_FORMAT) {
        @Override
        List<String> getAllowedFormats() {
            return getAllowedMonthFormats();
        }},

    /**
     * @see net.anotheria.anosite.content.variables.CalendarVariableNames Constructor
     */
    currentYear("currentYear", CalendarVariableNames.YEAR_DEFAULT_FORMAT) {

        @Override
        List<String> getAllowedFormats() {
            return getAllowedYearFormats();
        }},

    /**
     * @see net.anotheria.anosite.content.variables.CalendarVariableNames Constructor
     */
    currentTime("currentTime", CalendarVariableNames.TIME_DEFAULT_FORMAT) {

        @Override
        List<String> getAllowedFormats() {
            return getAllowedTimeFormats();
        }};

    private static final Logger log = Logger.getLogger(CalendarVariableNames.class);

    /**
     * Default Date format for currentDate
     */
    public static final String DATE_DEFAULT_FORMAT = "yyyy.MM.dd  HH:mm:ss";
    /**
     * Default Date format for currentDay
     */
    public static final String DAY_DEFAULT_FORMAT = "dd";
    /**
     * Default Date format for currentMonth
     */
    public static final String MONTH_DEFAULT_FORMAT = "MMMM";
    /**
     * Default Date format for currentYear
     */
    public static final String YEAR_DEFAULT_FORMAT = "yyyy";
    /**
     * Default Date format for currentTime
     */
    public static final String TIME_DEFAULT_FORMAT = "HH:mm:ss";


    /**
     * Represent List<String> which contains allowed formats for Day - formatting
     */
    private static List<String> allowedDayFormats;
    /**
     * Represent List<String> which contains allowed formats for Month - formatting
     */
    private static List<String> allowedMonthFormats;
    /**
     * Represent List<String> which contains allowed formats for Year - formatting
     */
    private static List<String> allowedYearFormats;
    /**
     * Represent List<String> which contains allowed formats for Time - formatting
     */
    private static List<String> allowedTimeFormats;


    static {
        /**
         *allowedDayFormats
         */

        allowedDayFormats = new ArrayList<String>();
        allowedDayFormats.add("dd");
        allowedDayFormats.add("d");
        /**
         *allowedMonthFormats
         */
        allowedMonthFormats = new ArrayList<String>();
        allowedMonthFormats.add("MMMMM");
        allowedMonthFormats.add("MMMM");
        allowedMonthFormats.add("MMM");
        allowedMonthFormats.add("MM");
        allowedMonthFormats.add("M");
        /**
         *allowedYearFormats
         */
        allowedYearFormats = new ArrayList<String>();
        allowedYearFormats.add("yyyy");
        allowedYearFormats.add("yyy");
        allowedYearFormats.add("yy");
        allowedYearFormats.add("y");
        /**
         *allowedTimeFormats
         */
        allowedTimeFormats = new ArrayList<String>();
        allowedTimeFormats.add("HH:mm:ss");
        allowedTimeFormats.add("HH:mm");
        allowedTimeFormats.add("HH:mm:ss, Z");
        allowedTimeFormats.add("h:mm a");
        allowedTimeFormats.add("hh:mm a");
        allowedTimeFormats.add("hh:mm a, Z");

    }

    /**
     * Variable name - itself
     */
    private final String variableName;
    /**
     * Default Date Format Pattern
     */
    private final String defaultFormatPattern;
    /**
     * List of allowed patterns
     */
    //private List<String> allowedFormats;

    /**
     * Constructor itself
     *
     * @param name   String representation of CalendarVariableNames instance (name)
     * @param format Default format pattern
     */
    CalendarVariableNames(String name, String format/*, List<String> allowedFormats*/) {
        this.variableName = name;
        this.defaultFormatPattern = format;

    }

    public String getVariableName() {
        return this.variableName;
    }

    public String getDefaultFormatPattern() {
        return defaultFormatPattern;
    }

    /**
     * Returns currentValue for selected variable.
     *
     * @param format incoming String param - which represent some date Format pattern
     * @return String variable value
     */
    public String getVariableValue(String format) {
        SimpleDateFormat dateFormat;
        format = isFormatValid(format) ? format : this.defaultFormatPattern;
        try {
            dateFormat = new SimpleDateFormat(format);
        } catch (IllegalArgumentException e) {
            log.error("Unsupported DateFormat pattern - " + format + " used for variable - " + this.getVariableName());
            dateFormat = new SimpleDateFormat(this.defaultFormatPattern);
        }
        return dateFormat.format(new java.util.Date());
    }

    /**
     * Checking incoming format pattern.
     *
     * @param format incoming String format - which represent some date Format
     * @return true - <a>if incoming  parameter - valid for current CalendarVariableNames instance </a>.
     *         else returns false.
     */
    private boolean isFormatValid(String format) {
        if (this.getAllowedFormats() == null || Collections.EMPTY_LIST.equals(this.getAllowedFormats()))
            return !isBlankOrNull(format);
        else
            return !isBlankOrNull(format) && this.getAllowedFormats().contains(format);
    }

    private static boolean isBlankOrNull(String str) {
        return str == null || "".equals(str);
    }

    public static List<String> getAllowedMonthFormats() {
        return allowedMonthFormats;
    }

    public static List<String> getAllowedTimeFormats() {
        return allowedTimeFormats;
    }

    public static List<String> getAllowedYearFormats() {
        return allowedYearFormats;
    }

    public static List<String> getAllowedDayFormats() {
        return allowedDayFormats;
    }

    abstract List<String> getAllowedFormats();
}
