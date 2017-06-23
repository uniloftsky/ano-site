package net.anotheria.anosite.decorator;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.anoaccessconfiguration.service.IAnoAccessConfigurationService;
import net.anotheria.asg.exception.ASGRuntimeException;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

/**
 * Decorator for roles list.
 * @author vbezuhlyi
 */
public class RolesListCMS2Decorator extends LinksListCMS2Decorator {

    /**
     * As site data service for script retrieval.
     */
    private static IAnoAccessConfigurationService service;

    /**
     * Init.
     */
    static {
        try {
            service = MetaFactory.get(IAnoAccessConfigurationService.class);
        } catch (MetaFactoryException e) {
            LoggerFactory.getLogger(IAnoAccessConfigurationService.class).error(MarkerFactory.getMarker("FATAL"), "IAnoAccessConfigurationService  init failure", e);
        }
    }
    @Override
    protected String getLinkTargetName(String targetId) throws ASGRuntimeException {
        return service.getRole(targetId).getName();
    }
}
