package net.anotheria.anosite.content.variables;

import junit.framework.TestCase;

/**
 * @author: h3llka
 */
public class ConditionalProcessorTest extends TestCase {

    private ConditionProcessor processor;

    public void setUp() {
        processor = new ConditionProcessor();
    }

    public void testSipleFlow() {

        String valueIf = processor.replace(ConditionalProcessorPrefixNames.iF.getPrefixName(), "true", "1;", null);
        assertTrue("should be true", valueIf.equals("1"));

        String valueNotIf = processor.replace(ConditionalProcessorPrefixNames.ifNot.getPrefixName(), "false", "1;", null);
        assertTrue("should be true", valueNotIf.equals("1;"));

        String valuePrS = processor.replace(ConditionalProcessorPrefixNames.present.getPrefixName(), "yeap", "test", null);
        assertTrue("shpud be true", valuePrS.equals("test"));

        String valueNotPrS = processor.replace(ConditionalProcessorPrefixNames.notPresent.getPrefixName(), "", "test", null);
        assertTrue("shpud be true", valueNotPrS.equals("test"));

        String valueEquals = processor.replace(ConditionalProcessorPrefixNames.equals.getPrefixName(), "", "", null);
        assertTrue("shpuld be true", Boolean.valueOf(valueEquals));

        String valueNotEquals = processor.replace(ConditionalProcessorPrefixNames.notEquals.getPrefixName(), "", "", null);
        assertTrue("shpuld be true", !Boolean.valueOf(valueNotEquals));


        String valueInRange1 = processor.replace(ConditionalProcessorPrefixNames.inRange.getPrefixName(), "10-15", "11", null);
        assertTrue("shpuld be true", Boolean.valueOf(valueInRange1));

        String valueInRange2 = processor.replace(ConditionalProcessorPrefixNames.inRange.getPrefixName(), "10-15", "0", null);
        assertTrue("shpuld be true", !Boolean.valueOf(valueInRange2));


        String valGreaterThan = processor.replace(ConditionalProcessorPrefixNames.greaterThan.getPrefixName(), "10", "1", null);
        assertTrue("shpuld be true", Boolean.valueOf(valGreaterThan));
        String valGreaterEq1 = processor.replace(ConditionalProcessorPrefixNames.greaterEqual.getPrefixName(), "10", "1", null);
        assertTrue("shpuld be true", Boolean.valueOf(valGreaterEq1));
        String valGreaterEq2 = processor.replace(ConditionalProcessorPrefixNames.greaterEqual.getPrefixName(), "10", "10", null);
        assertTrue("shpuld be true", Boolean.valueOf(valGreaterEq2));

        String valeLessThan = processor.replace(ConditionalProcessorPrefixNames.lessThan.getPrefixName(), "1", "100", null);
        assertTrue("shpuld be true", Boolean.valueOf(valeLessThan));
        String valeLessEq1 = processor.replace(ConditionalProcessorPrefixNames.lessEqual.getPrefixName(), "1", "100", null);
        assertTrue("shpuld be true", Boolean.valueOf(valeLessEq1));
        String valeLessEq2 = processor.replace(ConditionalProcessorPrefixNames.lessEqual.getPrefixName(), "100", "100", null);
        assertTrue("shpuld be true", Boolean.valueOf(valeLessEq2));
    }

    /**
     * trying to post bad data.
     */
    public void testWrongUse() {
        String reS = processor.replace("", ",", "", null);
        assertTrue(reS.isEmpty());
        reS = processor.replace(null, ",", "", null);
    }
}
