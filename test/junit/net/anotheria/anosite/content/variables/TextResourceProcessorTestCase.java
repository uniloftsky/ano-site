package net.anotheria.anosite.content.variables;

import junit.framework.Assert;
import net.anotheria.anodoc.util.context.ContextManager;
import net.anotheria.anoprise.metafactory.Extension;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asresourcedata.data.TextResource;
import net.anotheria.anosite.gen.asresourcedata.data.TextResourceDocument;
import net.anotheria.anosite.gen.asresourcedata.service.ASResourceDataServiceException;
import net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService;
import net.anotheria.anosite.gen.asresourcedata.service.fixture.ASResourceDataServiceFixtureFactory;
import net.anotheria.anosite.gen.util.AnositeCallContext;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Junit for TExtResourceProcessor.
 *
 * @author: h3llka
 */
public class TextResourceProcessorTestCase {
	private static TextResourceProcessor testProcessor;

	private static IASResourceDataService dataService;

	private static TextResource testResource;

	private static String testLanguage = "DE";
	public static final String TEST_RESOURCE_NAME = "testResource";
	public static final String TEST_RESOURCE_VALUE = "testValue!!!";

	@BeforeClass
	public static void setUp() throws MetaFactoryException, ASResourceDataServiceException {

		MetaFactory.addFactoryClass(IASResourceDataService.class, Extension.CMS, ASResourceDataServiceFixtureFactory.class);
		MetaFactory.addFactoryClass(IASResourceDataService.class, Extension.LOCAL, ASResourceDataServiceFixtureFactory.class);
		MetaFactory.addFactoryClass(IASResourceDataService.class, Extension.NONE, ASResourceDataServiceFixtureFactory.class);
		ContextManager.setCallContext(new AnositeCallContext());
		ContextManager.getCallContext().setCurrentLanguage("DE");
		dataService = MetaFactory.get(IASResourceDataService.class);
		testResource = new TextResourceDocument("1");
		testResource.setName(TEST_RESOURCE_NAME);
		testResource.setValue(TEST_RESOURCE_VALUE);
		dataService.createTextResource(testResource);

		testProcessor = new TextResourceProcessor();
	}

	@Test
	public void testProcessor() {
		String defaultVal = "default";
		String result = testProcessor.replace("", "", defaultVal, null);
		Assert.assertNotNull("Should not be null", result);

		result = testProcessor.replace(TextResourceProcessor.PREFIX, "", defaultVal, null);
		Assert.assertNotNull("Should not be null", result);

		result = testProcessor.replace(TextResourceProcessor.PREFIX, "123", defaultVal, null);
		Assert.assertNotNull("Should not be null", result);

		result = testProcessor.replace(TextResourceProcessor.PREFIX, "not Exist!", defaultVal, null);
		Assert.assertNotNull("Should not be null", result);

		result = testProcessor.replace(TextResourceProcessor.PREFIX, TEST_RESOURCE_NAME, defaultVal, null);
		Assert.assertEquals("Should be equals to default", TEST_RESOURCE_VALUE, result);
	}
}
