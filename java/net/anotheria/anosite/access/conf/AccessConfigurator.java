package net.anotheria.anosite.access.conf;

import net.anotheria.access.AccessService;
import net.anotheria.access.AccessServiceException;
import net.anotheria.access.SecurityObject;
import net.anotheria.access.impl.PermissionCollection;
import net.anotheria.access.impl.PermissionImpl;
import net.anotheria.access.impl.SecurityBox;
import net.anotheria.access.impl.StaticRole;
import net.anotheria.access.storage.persistence.SecurityBoxPersistenceService;
import net.anotheria.anoprise.dualcrud.CrudServiceException;
import net.anotheria.anoprise.dualcrud.SaveableID;
import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.access.constraint.ParametrizedConstraint;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.AccessOperation;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.AccessOperationBuilder;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.Constraint;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.Permission;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.PermissionBuilder;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.Role;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.RoleBuilder;
import net.anotheria.anosite.gen.anoaccessconfiguration.data.RoleSortType;
import net.anotheria.anosite.gen.anoaccessconfiguration.service.AnoAccessConfigurationServiceException;
import net.anotheria.anosite.gen.anoaccessconfiguration.service.IAnoAccessConfigurationService;
import net.anotheria.anosite.gen.asuserdata.data.UserDef;
import net.anotheria.anosite.gen.asuserdata.data.UserDefBuilder;
import net.anotheria.anosite.gen.asuserdata.service.ASUserDataServiceException;
import net.anotheria.anosite.gen.asuserdata.service.IASUserDataService;
import net.anotheria.anosite.gen.shared.util.ViewName;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.listener.IServiceListener;
import net.anotheria.util.StringUtils;
import net.anotheria.util.log.LogMessageUtil;
import net.anotheria.util.sorter.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Access configuration.
 * Creates needed data in CMS, configure access service.
 */
public class AccessConfigurator {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccessConfigurator.class);

    private static IAnoAccessConfigurationService accessConfigurationService;

    private static IASUserDataService userDataService;

    private static AccessService accessService;

    private static SecurityBoxPersistenceService securityBoxPersistenceService;

    /**
     * Main configuration routine.
     */
    public static void configure() {
        try {
            accessConfigurationService = MetaFactory.get(IAnoAccessConfigurationService.class);
            userDataService = MetaFactory.get(IASUserDataService.class);
            accessService = MetaFactory.get(AccessService.class);
            securityBoxPersistenceService = MetaFactory.get(SecurityBoxPersistenceService.class);
        } catch (MetaFactoryException e) {
            throw new RuntimeException("MetaFactory failed", e);
        }

        try {
            configureCMSPermissions();
            configureCMSDefaultRoles();
            configureCMSPermissionsForAdminRole();
            configureCMSDefaultUsers();

            configureAccessService();

            configureListeners();
        } catch (ASUserDataServiceException | AnoAccessConfigurationServiceException | AccessConfiguratorException e) {
            LOGGER.error("CMS Permissions configuration failed", e);
            throw new RuntimeException("CMS Permissions configuration failed", e);
        }
    }

    /**
     * Check existing permissions for CMS. Create missing.
     */
    private static void configureCMSPermissions() throws AnoAccessConfigurationServiceException {
        List<String> requiredActionNameList = new ArrayList<>();
        for (ViewName viewName : ViewName.values()) {
            String section = viewName.getName().toLowerCase();
            requiredActionNameList.add("asg."+section+".read");
            requiredActionNameList.add("asg."+section+".write");
        }

        List<AccessOperation> accessOperationList =  accessConfigurationService.getAccessOperations();
        List<AccessOperation> newAccessOperationList = new ArrayList<>();
        for(String requiredActionName : requiredActionNameList) {
            boolean exist = false;
            for (AccessOperation accessOperation : accessOperationList) {
                if (accessOperation.getName().equals(requiredActionName)) {
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                AccessOperationBuilder accessOperationBuilder = new AccessOperationBuilder();
                accessOperationBuilder.name(requiredActionName);
                AccessOperation accessOperation = accessConfigurationService.createAccessOperation(accessOperationBuilder.build());
                newAccessOperationList.add(accessOperation);
            }
        }
        accessOperationList.addAll(newAccessOperationList);

        List<Permission> permissionList =  accessConfigurationService.getPermissions();
        for (AccessOperation accessOperation : accessOperationList) {
            boolean exist = false;
            for (Permission permission : permissionList) {
                if (permission.getAccessOperation().equals(accessOperation.getId())) {
                    exist = true;
                    break;
                }
            }

            if (!exist) {
                PermissionBuilder permissionBuilder = new PermissionBuilder();
                permissionBuilder.name(accessOperation.getName());
                permissionBuilder.accessOperation(accessOperation.getId());
                permissionBuilder.priority(5);
                permissionBuilder.constraints(Collections.<String>emptyList());
                accessConfigurationService.createPermission(permissionBuilder.build());
            }
        }
    }

    /**
     * Create default roles.
     */
    private static void configureCMSDefaultRoles() throws AnoAccessConfigurationServiceException {
        addRole("admin");
        addRole("developer");
        addRole("editor");
        addRole("cmsuser");
        addRole("producer");
    }

    /**
     * Create role with given name.
     */
    private static void addRole(String role) throws AnoAccessConfigurationServiceException {
        List<Role> roleList = accessConfigurationService.getRolesByProperty(Role.PROP_NAME, role);
        if (roleList == null || roleList.isEmpty()) { // check if such role does not exist
            RoleBuilder roleBuilder = new RoleBuilder();
            roleBuilder.name(role);
            roleBuilder.permissions(Collections.<String>emptyList());
            roleBuilder.contextInitializers(Collections.<String>emptyList());
            accessConfigurationService.createRole(roleBuilder.build());
        }
    }

    /**
     * Check admin user. Create if missing.
     */
    private static void configureCMSDefaultUsers() throws ASUserDataServiceException, AnoAccessConfigurationServiceException {
        String userName = "admin";
        String pass = "admin";
        String roleName = "admin";
        List<UserDef> userDefs = userDataService.getUserDefsByProperty(UserDef.PROP_LOGIN, userName);
        UserDef adminUser;
        if (userDefs == null || userDefs.isEmpty()) { // check if such user does not exist
            UserDefBuilder userDefBuilder = new UserDefBuilder();
            userDefBuilder.login(userName);
            userDefBuilder.password(pass);
            userDefBuilder.roles(Collections.<String>emptyList());
            adminUser = userDataService.createUserDef(userDefBuilder.build());
        } else {
            adminUser = userDefs.get(0);
        }

        List<Role> roleList = accessConfigurationService.getRolesByProperty(Role.PROP_NAME, roleName);
        if (roleList == null || roleList.isEmpty()) { // check if role for admin user does not exist
            LOGGER.error("There is no admin role for admin user in CMS");
            throw new RuntimeException("admin role for admin user is undefined");
        }
        Role adminRole = roleList.get(0);
        List<String> roleIds = adminUser.getRoles();
        if (!roleIds.contains(adminRole.getId())) {
            roleIds.add(adminRole.getId());
            adminUser.setRoles(roleIds);
            userDataService.updateUserDef(adminUser);
        }
    }

    /**
     * Grand ot admin all CMS permissions.
     */
    private static void configureCMSPermissionsForAdminRole() throws AnoAccessConfigurationServiceException {
        List<String> requiredActionNameList = new ArrayList<>();
        for (ViewName viewName : ViewName.values()) {
            String section = viewName.getName().toLowerCase();
            requiredActionNameList.add("asg."+section+".read");
            requiredActionNameList.add("asg."+section+".write");
        }

        List<Permission> permissionList =  accessConfigurationService.getPermissions();
        List<String> cmsOnlyPermissionIdList = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (requiredActionNameList.contains(permission.getName())) {
                cmsOnlyPermissionIdList.add(permission.getId());
            }
        }
        List<Role> roleList = accessConfigurationService.getRolesByProperty(Role.PROP_NAME, "admin");
        Role adminRole = roleList.get(0);
        adminRole.setPermissions(cmsOnlyPermissionIdList);
        accessConfigurationService.updateRole(adminRole);
    }

    /**
     * Configure {@link AccessService}
     */
    private static void configureAccessService() throws AccessConfiguratorException {
        accessService.reset(); // clearing current configuration

        try {
            configureAccessServiceRoles();
            configureAccessServiceUsers();
        } catch (Exception e) {
            String message = LogMessageUtil.failMsg(e) + " Error while configuring access service.";
            LOGGER.warn(message, e);
            throw new AccessConfiguratorException(message, e);
        }
    }

    /**
     * Configure roles for {@link AccessService}.
     */
    private static void configureAccessServiceRoles() throws AnoAccessConfigurationServiceException, AccessConfiguratorException {
        for (Role role : getRoles()) {
            StaticRole configuredRole = new StaticRole(role.getId());
            PermissionCollection permissionCollection = new PermissionCollection(configuredRole.getName());

            for (Permission permission : getPermissions(role.getPermissions())) {
                PermissionImpl configuredPermission = new PermissionImpl();
                configuredPermission.setName(permission.getId());
                AccessOperation accessOperation = accessConfigurationService.getAccessOperation(permission.getAccessOperation());
                configuredPermission.setAction(accessOperation.getName());
                configuredPermission.setAllow(!permission.getDeny());

                for (Constraint constraint : getConstraint(permission.getConstraints())) {
                    if (StringUtils.isEmpty(constraint.getClassName())) // skipping constraint if its class not configured
                        continue;

                    String clazz = constraint.getClassName();
                    try {
                        Class<?> undefinedClass = Class.forName(clazz); // loading class by name
                        if (!net.anotheria.access.impl.Constraint.class.isAssignableFrom(undefinedClass)) { // validating class type
                            String message = LogMessageUtil.failMsg(new RuntimeException()) + " Wrong constraint class[" + clazz + "] type.";
                            LOGGER.warn(message);
                            throw new AccessConfiguratorException(message);
                        }

                        @SuppressWarnings("unchecked")
                        Class<net.anotheria.access.impl.Constraint> constraintClass = (Class<net.anotheria.access.impl.Constraint>) undefinedClass;
                        net.anotheria.access.impl.Constraint instance = constraintClass.newInstance();

                        if (instance instanceof ParametrizedConstraint) {
                            ParametrizedConstraint parametrizedInstance = ParametrizedConstraint.class.cast(instance);
                            parametrizedInstance.setParameter1(constraint.getParameter1());
                            parametrizedInstance.setParameter2(constraint.getParameter2());
                            parametrizedInstance.setParameter3(constraint.getParameter3());
                            parametrizedInstance.setParameter4(constraint.getParameter4());
                            parametrizedInstance.setParameter5(constraint.getParameter5());
                        }

                        configuredPermission.addConstraint(instance);
                    } catch (ClassNotFoundException e) {
                        String message = LogMessageUtil.failMsg(e) + " Wrong constraint class[" + clazz + "].";
                        LOGGER.warn(message, e);
                        throw new AccessConfiguratorException(message, e);
                    } catch (InstantiationException e) {
                        String message = LogMessageUtil.failMsg(e) + " Can't instantiate constraint class[" + clazz + "].";
                        LOGGER.warn(message, e);
                        throw new AccessConfiguratorException(message, e);
                    } catch (IllegalAccessException e) {
                        String message = LogMessageUtil.failMsg(e) + " Can't instantiate constraint class[" + clazz + "].";
                        LOGGER.warn(message, e);
                        throw new AccessConfiguratorException(message, e);
                    }
                }

                // adding permission to role permission collection
                permissionCollection.add(configuredPermission);
            }

            // configuring access service with role and its permission collection
            accessService.addPermissionCollection(permissionCollection);
            configuredRole.setPermissionSetId(permissionCollection.getId());
            accessService.addRole(configuredRole);
        }
    }

    /**
     * Configure users for {@link AccessService}.
     */
    private static void configureAccessServiceUsers() throws ASUserDataServiceException, AccessServiceException {
        List<UserDef> userList = userDataService.getUserDefs();
        SecurityBox securityBox = null;

        for (UserDef user : userList) {
            try {
                SaveableID saveableID = new SaveableID();
                saveableID.setSaveableId(user.getLogin());
                saveableID.setOwnerId(user.getLogin());
                securityBox = securityBoxPersistenceService.read(saveableID);
            } catch (CrudServiceException e) {
                LOGGER.warn("SecurityBox with id=" + user.getLogin() + " not found. Creating new one");
            }

            if (securityBox == null) {
                securityBox = new SecurityBox(user.getLogin());
            }

            for (String roleId : user.getRoles()) {
                if (securityBox.hasRole(roleId)) {
                    continue;
                }
                net.anotheria.access.Role role = accessService.getRole(roleId);
                accessService.grantRole(new SecurityObject(user.getLogin()), role.getName());
            }
        }
    }

    /**
     * Get permissions from CMS by given ids.
     */
    private static List<Permission> getPermissions(final List<String> ids) throws AnoAccessConfigurationServiceException {
        List<Permission> result = new ArrayList<Permission>();

        if (ids == null || ids.isEmpty())
            return result;

        for (String id : ids)
            result.add(accessConfigurationService.getPermission(id));

        return result;
    }

    /**
     * Get constraints from CMS by given ids.
     */
    private static List<Constraint> getConstraint(final List<String> ids) throws AnoAccessConfigurationServiceException  {
        List<Constraint> result = new ArrayList<Constraint>();

        if (ids == null || ids.isEmpty())
            return result;

        for (String id : ids)
            result.add(accessConfigurationService.getConstraint(id));

        return result;

    }

    /**
     * Get roles list from CMS.
     */
    private static List<Role> getRoles() throws AnoAccessConfigurationServiceException {
        SortType sorting = new SortType(RoleSortType.SORT_BY_ID, RoleSortType.ASC);
        List<Role> roleList = accessConfigurationService.getRoles(sorting);
        if (roleList == null)
            roleList = new ArrayList<>();
        return roleList;
    }

    /**
     * Configure listeners.
     */
    private static void configureListeners() {
        accessConfigurationService.addServiceListener(new CMSDataChangeListener());
        userDataService.addServiceListener(new CMSDataChangeListener());
    }

    /**
     * Listener for CMS change data.
     */
    private static final class CMSDataChangeListener implements IServiceListener {
        private final Logger LOGGER = LoggerFactory.getLogger(CMSDataChangeListener.class);

        @Override
        public void documentUpdated(final DataObject oldVersion, final DataObject newVersion) {
            LOGGER.info("Update document. Old doc:{} New doc:{}", oldVersion, newVersion);
            updateConfiguration();
        }

        @Override
        public void documentDeleted(final DataObject doc) {
            LOGGER.info("Delete document. Doc:{}", doc);
            updateConfiguration();
        }

        @Override
        public void documentCreated(final DataObject doc) {
            LOGGER.info("Create document. Doc:{}", doc);
            updateConfiguration();
        }

        @Override
        public void documentImported(final DataObject doc) {
            LOGGER.info("Import document. Doc:{}", doc);
            updateConfiguration();
        }

        @Override
        public void persistenceChanged() {
            LOGGER.info("Persistence changed.");
            updateConfiguration();
        }

        /**
         * Update configuration in {@link AccessService}.
         */
        private void updateConfiguration() {
            try {
                LOGGER.debug("Access configuration changed. Re-Configuring AccessService...");
                configureAccessService();
                LOGGER.debug("Re-Configuration of AccessService finished.");
            } catch (AccessConfiguratorException e) {
                LOGGER.warn(LogMessageUtil.failMsg(e), e);
            }
        }
    }

}
