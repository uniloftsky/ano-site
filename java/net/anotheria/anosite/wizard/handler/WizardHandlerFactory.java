package net.anotheria.anosite.wizard.handler;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.aswizarddata.data.WizardHandlerDef;
import net.anotheria.anosite.gen.aswizarddata.service.IASWizardDataService;
import net.anotheria.util.StringUtils;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Wizard handler factory.
 * Simplest utility for Wizard handler creation.
 *
 * @author h3ll
 */
public final class WizardHandlerFactory {

	/**
	 * Log4j logger.
	 */
	private static final Logger log = Logger.getLogger(WizardHandlerFactory.class);

	/**
	 * IASWizardDataService instance.
	 */
	private static IASWizardDataService wizardService;

	/**
	 * Producers map. (local cache).
	 */
	private static final Map<String, WizardHandlerProducer> wHProducers;

	/**
	 * Static init block.
	 */
	static {
		wHProducers = new HashMap<String, WizardHandlerProducer>();
		try {
			wizardService = MetaFactory.get(IASWizardDataService.class);
		} catch (MetaFactoryException e) {
			log.fatal("IASWizardDataService init failure", e);
		}
	}

	/**
	 * Constructor.
	 */
	private WizardHandlerFactory() {
		throw new IllegalAccessError("Can't be instantiated!");
	}

	/**
	 * Create method. Creates WizardHandler with given id.
	 *
	 * @param id wizard id
	 * @return {@link WizardHandler}
	 */
	public static WizardHandler createHandler(String id) {
		try {
			if (!StringUtils.isEmpty(id)) {
				WizardHandlerDef wHandlerDef = wizardService.getWizardHandlerDef(id);
				WizardHandlerProducer wProducer = getProducer(wHandlerDef.getClazz() + "-" + wHandlerDef.getId());
				return new WizardHandlerWrapper((WizardHandler) Class.forName(wHandlerDef.getClazz()).newInstance(), wProducer);
			}
			//Case when wizard works with default handler
			WizardHandler wHandler = new BaseWizardHandler();
			WizardHandlerProducer wProducer = getProducer("BaseWizardHandlerProducer-default");
			return new WizardHandlerWrapper(wHandler, wProducer);


		} catch (Exception e) {
			log.error("createWizardHandler(" + id + ")", e);
			throw new RuntimeException("WizardHandler instantiation failed - " + e.getMessage());
		}

	}

	/**
	 * Returns {@link WizardHandlerProducer}.
	 *
	 * @param producerId producer id
	 * @return {@link WizardHandlerProducer}
	 */
	private static synchronized WizardHandlerProducer getProducer(String producerId) {
		WizardHandlerProducer wProducer = wHProducers.get(producerId);
		if (wProducer == null) {
			synchronized (wHProducers) {
				wProducer = wHProducers.get(producerId);
				if (wProducer == null) {
					wProducer = new WizardHandlerProducer(producerId);
					wHProducers.put(producerId, wProducer);
				}
			}
		}
		return wProducer;
	}
}
