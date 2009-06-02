package net.anotheria.anosite.content.variables;

import junit.framework.TestCase;
import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceException;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author: h3llka
 */
@SuppressWarnings({"ALL"})
public class TextResourceProcessorTestCase extends TestCase {
    private TextResourceProcessor processor;
    Mockery context = new Mockery();
    private final IASResourceDataService mockedService = context.mock(IASResourceDataService.class);
    private final TextResource mockedResource = context.mock(TextResource.class);
    private Logger log = Logger.getLogger(TextResourceProcessorTestCase.class);

    public void setUp() throws Exception {
        processor = new TextResourceProcessor();
        processor.setResourceDataService(mockedService);
    }

    /**
     * Simple Flow test with link.domain - variable
     *
     * @throws ASResourceDataServiceException
     */
    public void testWorkTest() throws ASResourceDataServiceException {
        final String expected = "c-date.de";
        final String variable = "link.domain";
        expectations(expected, variable);
        String result = processor.replace(TextResourceProcessor.PREFIX, variable, null, null);
        assertNotNull("Should not Be null", result);
        assertEquals("Should be same!", result, expected);

    }

    /**
     * Simple Flow test with mail.notificator - variable
     *
     * @throws ASResourceDataServiceException
     */
    public void testWorkTest2() throws ASResourceDataServiceException {
        final String expected = "notificator@anosite.de";
        final String variable = "mail.notificator";
        expectations(expected, variable);
        String result = processor.replace(TextResourceProcessor.PREFIX, variable, null, null);
        assertNotNull("Should not Be null", result);
        assertEquals("Should be same!", result, expected);

    }

    private void expectations(final String expected, final String variable) throws ASResourceDataServiceException {
        final List<TextResource> resources = new ArrayList<TextResource>();
        resources.add(mockedResource);
        context.checking(new Expectations() {{
            atLeast(1).of(mockedService).getTextResourcesByProperty(TextResource.PROP_NAME, variable);
            will(returnValue(resources));
        }});
        context.checking(new Expectations() {{
            atLeast(1).of(mockedResource).getValue();
            will(returnValue(expected));
        }});
    }

    /**
     * Processor should return   error-String. Cause Nothing is Founded in IASResourceDataService
     * @throws net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceException
     */
    public void testUnknownVariableCase() throws ASResourceDataServiceException {
        final String variable = "mail.unkNownMail";
        String expected = "Wrong or unsupported variable : " + variable;
        context.checking(new Expectations() {{
            atLeast(1).of(mockedService).getTextResourcesByProperty(TextResource.PROP_NAME, variable);
            will(returnValue(Collections.EMPTY_LIST));
        }});
        String result = processor.replace(TextResourceProcessor.PREFIX, variable, null, null);
        assertNotNull("Should not Be null", result);
        assertEquals("Should be same!", result, expected);

    }

    /**
     * Processor should return   error-String. Exception throws
     * @throws net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceException
     */
    public void testUnknownVariableCaseWithException() throws ASResourceDataServiceException {
        final String variable = "mail.unkNownMail";
        String expected = "Wrong or unsupported variable : " + variable;
        context.checking(new Expectations() {{
            atLeast(1).of(mockedService).getTextResourcesByProperty(TextResource.PROP_NAME, variable);
            //noinspection ThrowableInstanceNeverThrown
            will(throwException(new ASResourceDataServiceException("Nothing")));
        }});

        String result = processor.replace(TextResourceProcessor.PREFIX, variable, null, null);
        assertNotNull("Should not Be null", result);
        assertEquals("Should be same!", result, expected);
    }

    /**
     * Case - when returned TextResource  - which is null.
     * @throws ASResourceDataServiceException
     */
     public void testUnknownVariableCaseWithNullableValue() throws ASResourceDataServiceException {
        final String variable = "mail.unkNownMail";
        String expected = "Wrong or unsupported variable : " + variable;
        final List<TextResource> resources = new ArrayList<TextResource>();
        resources.add(null);
        context.checking(new Expectations() {{
            atLeast(1).of(mockedService).getTextResourcesByProperty(TextResource.PROP_NAME, variable);
            will(returnValue(resources));
        }});

        String result = processor.replace(TextResourceProcessor.PREFIX, variable, null, null);
        assertNotNull("Should not Be null", result);
        assertEquals("Should be same!", result, expected);
    }

}
