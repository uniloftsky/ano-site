package net.anotheria.anosite.access;

import net.anotheria.access.AccessService;
import net.anotheria.access.AccessServiceException;
import net.anotheria.access.SOAttribute;
import net.anotheria.access.SecurityObject;
import net.anotheria.anoplass.api.APIException;
import net.anotheria.anoplass.api.APIFinder;
import net.anotheria.anoplass.api.APIInitException;
import net.anotheria.anoplass.api.generic.login.LoginAPI;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.access.context.SecurityContextInitializer;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.AccessOperation;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.Constraint;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.ContextInitializer;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.Permission;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.PermissionSortType;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.Role;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.RoleSortType;
import net.anotheria.anosite.gen.anoaccessconfiguration.service.AccessOperationNotFoundInAnoAccessConfigurationServiceException;
import net.anotheria.anosite.gen.anoaccessconfiguration.service.AnoAccessConfigurationServiceException;
import net.anotheria.anosite.gen.anoaccessconfiguration.service.IAnoAccessConfigurationService;
import net.anotheria.anosite.gen.ascustomaction.data.CustomActionDef;
import net.anotheria.anosite.gen.ascustomaction.service.ASCustomActionServiceException;
import net.anotheria.anosite.gen.ascustomaction.service.IASCustomActionService;
import net.anotheria.anosite.gen.assitedata.data.NaviItem;
import net.anotheria.anosite.gen.assitedata.service.ASSiteDataServiceException;
import net.anotheria.anosite.gen.assitedata.service.IASSiteDataService;
import net.anotheria.anosite.gen.aswebdata.data.Box;
import net.anotheria.anosite.gen.aswebdata.data.Pagex;
import net.anotheria.anosite.gen.aswebdata.service.ASWebDataServiceException;
import net.anotheria.anosite.gen.aswebdata.service.IASWebDataService;
import net.anotheria.anosite.gen.aswizarddata.data.WizardDef;
import net.anotheria.anosite.gen.aswizarddata.service.ASWizardDataServiceException;
import net.anotheria.anosite.gen.aswizarddata.service.IASWizardDataService;
import net.anotheria.util.StringUtils;
import net.anotheria.util.log.LogMessageUtil;
import net.anotheria.util.sorter.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * {@link AnoSiteAccessAPI} implementation.
 * 
 * @author Alexandr Bolbat
 */
public class AnoSiteAccessAPIImpl implements AnoSiteAccessAPI {

	/**
	 * {@link Logger} instance.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AnoSiteAccessAPIImpl.class);

	/**
	 * {@link AccessService} instance.
	 */
	private AccessService accessService;

	/**
	 * Ano-site configuration for {@link AccessService}.
	 */
	private IAnoAccessConfigurationService accessConfigurationService;

	/**
	 * Pages and boxes configuration.
	 */
	private IASWebDataService pagesConfigurationPersistence;

	/**
	 * Navigation items configuration.
	 */
	private IASSiteDataService siteDataService;

	/**
	 * Custom actions configuration.
	 */
	private IASCustomActionService customActionsConfigurationPersistence;

	/**
	 * Wizards configuration.
	 */
	private IASWizardDataService wizardConfigurationPersistence;

	/**
	 * {@link LoginAPI} instance.
	 */
	private LoginAPI loginAPI;

	@Override
	public void init() throws APIInitException {
		try {
			accessService = MetaFactory.get(AccessService.class);
			accessConfigurationService = MetaFactory.get(IAnoAccessConfigurationService.class);
			pagesConfigurationPersistence = MetaFactory.get(IASWebDataService.class);
			siteDataService = MetaFactory.get(IASSiteDataService.class);
			customActionsConfigurationPersistence = MetaFactory.get(IASCustomActionService.class);
			wizardConfigurationPersistence = MetaFactory.get(IASWizardDataService.class);
		} catch (MetaFactoryException e) {
			String message = LogMessageUtil.failMsg(e) + " Can't initialize required services.";
			LOGGER.error(MarkerFactory.getMarker("FATAL"), message, e);
			throw new APIInitException(message, e);
		}

		loginAPI = APIFinder.findAPI(LoginAPI.class);

		try {
			configureAccessService();
		} catch (AnoSiteAccessAPIException e) {
			String message = LogMessageUtil.failMsg(e) + " Can't initialize access service with current configuration.";
			LOGGER.error(MarkerFactory.getMarker("FATAL"), message, e);
			throw new APIInitException(message, e);
		}
	}

	@Override
	public void deInit() {
	}

	@Override
	public boolean isAllowedForPage(final String pageId) throws AnoSiteAccessAPIException {
		if (!AnoSiteAccessAPIConfig.getInstance().isEnabled()) // allowing access if access control disabled by configuration
			return true;

		try {
			Pagex page = pagesConfigurationPersistence.getPagex(pageId);
			return isAllowed(page.getAccessOperation());
		} catch (ASWebDataServiceException e) {
			String message = LogMessageUtil.failMsg(e, pageId);
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	@Override
	public boolean isAllowedForBox(final String boxId) throws AnoSiteAccessAPIException {
		if (!AnoSiteAccessAPIConfig.getInstance().isEnabled()) // allowing access if access control disabled by configuration
			return true;

		try {
			Box box = pagesConfigurationPersistence.getBox(boxId);
			return isAllowed(box.getAccessOperation());
		} catch (ASWebDataServiceException e) {
			String message = LogMessageUtil.failMsg(e, boxId);
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	@Override
	public boolean isAllowedForNaviItem(String naviItemId) throws AnoSiteAccessAPIException {
		if (!AnoSiteAccessAPIConfig.getInstance().isEnabled()) // allowing access if access control disabled by configuration
			return true;

		try {
			NaviItem naviItem = siteDataService.getNaviItem(naviItemId);
			return isAllowed(naviItem.getAccessOperation());
		} catch (ASSiteDataServiceException e) {
			String message = LogMessageUtil.failMsg(e, naviItemId);
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	@Override
	public boolean isAllowedForAction(final String actionId) throws AnoSiteAccessAPIException {
		if (!AnoSiteAccessAPIConfig.getInstance().isEnabled()) // allowing access if access control disabled by configuration
			return true;

		try {
			CustomActionDef action = customActionsConfigurationPersistence.getCustomActionDef(actionId);
			return isAllowed(action.getAccessOperation());
		} catch (ASCustomActionServiceException e) {
			String message = LogMessageUtil.failMsg(e, actionId);
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	@Override
	public boolean isAllowedForWizard(final String wizardId) throws AnoSiteAccessAPIException {
		if (!AnoSiteAccessAPIConfig.getInstance().isEnabled()) // allowing access if access control disabled by configuration
			return true;

		try {
			WizardDef wizard = wizardConfigurationPersistence.getWizardDef(wizardId);
			return isAllowed(wizard.getAccessOperation());
		} catch (ASWizardDataServiceException e) {
			String message = LogMessageUtil.failMsg(e, wizardId);
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	/**
	 * Main API logic.
	 * 
	 * @param accessOperationId
	 *            - access operation id
	 * @return <code>true</code> if operation allowed or <code>false</code>
	 * @throws AnoSiteAccessAPIException
	 */
	private boolean isAllowed(final String accessOperationId) throws AnoSiteAccessAPIException {
		// if access operation not configured (not required)
		if (StringUtils.isEmpty(accessOperationId))
			return true;

		AccessOperation accessOperation = null;
		try {
			accessOperation = getAccessOperation(accessOperationId);
		} catch (AccessOperationNotFoundAPIException e) {
			LOGGER.warn(LogMessageUtil.failMsg(e, accessOperationId) + " AccessOperation not found. Skipping security check.");
			return true;
		}

		if (accessOperation == null) { // to be sure
			LOGGER.warn(LogMessageUtil.failMsg(new RuntimeException(), accessOperationId) + " AccessOperation not found. Skipping security check.");
			return true;
		}

		// if no logged user
		if (!isLoggedIn())
			return false;

		// loading context initializers required for current access operation
		Set<String> initializers = new HashSet<String>();
		for (Permission permission : getPermissions(accessOperationId))
			for (Constraint constraint : getConstraint(permission.getConstraints()))
				for (ContextInitializer contextInitializer : getContextInitializers(constraint.getContextInitializers()))
					if (contextInitializer != null && !StringUtils.isEmpty(contextInitializer.getClassName()))
						initializers.add(contextInitializer.getClassName());

		// prepare access security context
		SecurityObject securityObject = new SecurityObject(getCurrentUserId());

		for (String clazz : initializers) {
			try {
				Class<?> undefinedClass = Class.forName(clazz); // loading class by name
				if (!SecurityContextInitializer.class.isAssignableFrom(undefinedClass)) { // validating class type
					String message = LogMessageUtil.failMsg(new RuntimeException(), accessOperationId) + " Wrong security context class[" + clazz + "] type.";
					LOGGER.warn(message);
					throw new AnoSiteAccessAPIException(message);
				}

				@SuppressWarnings("unchecked")
				Class<SecurityContextInitializer> initializerClass = (Class<SecurityContextInitializer>) undefinedClass;

				// instantiating initializer
				SecurityContextInitializer initializer = initializerClass.newInstance();

				// retrieving security context values
				Map<String, String> initializedValues = initializer.initialize();

				// filling security object attributes with obtained values
				for (String key : initializedValues.keySet()) {
					SOAttribute attr = new SOAttribute(key, initializedValues.get(key));
					securityObject.addAttribute(attr);
				}
			} catch (ClassNotFoundException e) {
				String message = LogMessageUtil.failMsg(e, accessOperationId) + " Wrong security context class[" + clazz + "].";
				LOGGER.warn(message, e);
				throw new AnoSiteAccessAPIException(message, e);
			} catch (InstantiationException e) {
				String message = LogMessageUtil.failMsg(e, accessOperationId) + " Can't instantiate security context class[" + clazz + "].";
				LOGGER.warn(message, e);
				throw new AnoSiteAccessAPIException(message, e);
			} catch (IllegalAccessException e) {
				String message = LogMessageUtil.failMsg(e, accessOperationId) + " Can't instantiate security context class[" + clazz + "].";
				LOGGER.warn(message, e);
				throw new AnoSiteAccessAPIException(message, e);
			}
		}

		// executing main security logic
		try {
			return accessService.isAllowed(accessOperation.getId(), securityObject, null).isAllowed();
		} catch (AccessServiceException e) {
			LOGGER.warn(LogMessageUtil.failMsg(e, accessOperationId) + " Skipping exception and don't allow execution.", e);
			return false;
		}
	}

	/**
	 * Check is some user logged in.
	 * 
	 * @return <code>true</code> if logged or <code>false</code>
	 */
	private boolean isLoggedIn() {
		return loginAPI.isLogedIn();
	}

	/**
	 * Return currently logged user id.
	 * 
	 * @return {@link String} id of currently logged user
	 * @throws AnoSiteAccessAPIException
	 */
	private String getCurrentUserId() throws AnoSiteAccessAPIException {
		try {
			return loginAPI.getLogedUserId();
		} catch (APIException e) {
			throw new AnoSiteAccessAPIException("No logged user.");
		}
	}

	/**
	 * Return {@link AccessOperation} by given id.
	 * 
	 * @param id
	 *            - given access operation id
	 * @return {@link AccessOperation}, can't be <code>null</code>
	 * @throws AnoSiteAccessAPIException
	 */
	private AccessOperation getAccessOperation(final String id) throws AnoSiteAccessAPIException {
		try {
			return accessConfigurationService.getAccessOperation(id);
		} catch (AccessOperationNotFoundInAnoAccessConfigurationServiceException e) {
			throw new AccessOperationNotFoundAPIException("Access operation with given id[" + id + "] not found.", e);
		} catch (AnoAccessConfigurationServiceException e) {
			throw new AnoSiteAccessAPIException(LogMessageUtil.failMsg(e, id), e);
		}
	}

	/**
	 * Get {@link List} of {@link Role} by given id's.
	 * 
	 * @return {@link List} of {@link Role}, can be empty but not <code>null</code>
	 * @throws AnoSiteAccessAPIException
	 */
	private List<Role> getRoles() throws AnoSiteAccessAPIException {
		List<Role> result = new ArrayList<Role>();

		try {
			SortType sorting = new SortType(RoleSortType.SORT_BY_ID, RoleSortType.ASC);
			List<Role> loaded = accessConfigurationService.getRoles(sorting);
			if (loaded != null && !loaded.isEmpty())
				result.addAll(loaded);

			return result;
		} catch (AnoAccessConfigurationServiceException e) {
			String message = LogMessageUtil.failMsg(e) + " Can't load roles.";
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	/**
	 * Get {@link List} of {@link Permission} who have given access operation.
	 * 
	 * @param accessOperationId
	 *            - access operation id
	 * @return {@link List} of {@link Permission}, can be empty but not <code>null</code>
	 * @throws AnoSiteAccessAPIException
	 */
	private List<Permission> getPermissions(final String accessOperationId) throws AnoSiteAccessAPIException {
		List<Permission> result = new ArrayList<Permission>();

		try {
			SortType sorting = new SortType(PermissionSortType.SORT_BY_PRIORITY, PermissionSortType.DESC);
			List<Permission> loaded = accessConfigurationService.getPermissionsByProperty(Permission.LINK_PROP_ACCESS_OPERATION, accessOperationId, sorting);
			if (loaded != null && !loaded.isEmpty())
				result.addAll(loaded);

			return result;
		} catch (AnoAccessConfigurationServiceException e) {
			String message = LogMessageUtil.failMsg(e, accessOperationId) + " Can't load permissions.";
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	/**
	 * Get {@link List} of {@link Permission} by given id's.
	 * 
	 * @param ids
	 *            - id's
	 * @return {@link List} of {@link Permission}, can be empty but not <code>null</code>
	 * @throws AnoSiteAccessAPIException
	 */
	private List<Permission> getPermissions(final List<String> ids) throws AnoSiteAccessAPIException {
		List<Permission> result = new ArrayList<Permission>();

		if (ids == null || ids.isEmpty())
			return result;

		try {
			for (String id : ids)
				result.add(accessConfigurationService.getPermission(id));

			return result;
		} catch (AnoAccessConfigurationServiceException e) {
			String message = LogMessageUtil.failMsg(e, ids) + " Can't load permissions.";
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	/**
	 * Get {@link List} of {@link Constraint} by given id's.
	 * 
	 * @param ids
	 *            - id's
	 * @return {@link List} of {@link Constraint}, can be empty but not <code>null</code>
	 * @throws AnoSiteAccessAPIException
	 */
	private List<Constraint> getConstraint(final List<String> ids) throws AnoSiteAccessAPIException {
		List<Constraint> result = new ArrayList<Constraint>();

		if (ids == null || ids.isEmpty())
			return result;

		try {
			for (String id : ids)
				result.add(accessConfigurationService.getConstraint(id));

			return result;
		} catch (AnoAccessConfigurationServiceException e) {
			String message = LogMessageUtil.failMsg(e, ids) + " Can't load constraints.";
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	/**
	 * Get {@link List} of {@link ContextInitializer} by given id's.
	 * 
	 * @param ids
	 *            - id's
	 * @return {@link List} of {@link ContextInitializer}, can be empty but not <code>null</code>
	 * @throws AnoSiteAccessAPIException
	 */
	private List<ContextInitializer> getContextInitializers(final List<String> ids) throws AnoSiteAccessAPIException {
		List<ContextInitializer> result = new ArrayList<ContextInitializer>();

		if (ids == null || ids.isEmpty())
			return result;

		try {
			for (String id : ids)
				result.add(accessConfigurationService.getContextInitializer(id));

			return result;
		} catch (AnoAccessConfigurationServiceException e) {
			String message = LogMessageUtil.failMsg(e, ids) + " Can't load context initializers.";
			LOGGER.error(message, e);
			throw new AnoSiteAccessAPIException(message, e);
		}
	}

	/**
	 * Configuring {@link AccessService} instance with current configuration.
	 * 
	 * @throws AnoSiteAccessAPIException
	 */
	private synchronized void configureAccessService() throws AnoSiteAccessAPIException {

		createSecureBoxes();
	}

	/**
	 * Create secure boxes to store roles and permissions if they do not exists yet.
	 * */
	private void createSecureBoxes() {

	}

}
