package net.anotheria.anosite.cms.user;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asuserdata.data.RoleDef;
import net.anotheria.anosite.gen.asuserdata.data.RoleDefBuilder;
import net.anotheria.anosite.gen.asuserdata.data.UserDef;
import net.anotheria.anosite.gen.asuserdata.data.UserDefBuilder;
import net.anotheria.anosite.gen.asuserdata.service.ASUserDataServiceException;
import net.anotheria.anosite.gen.asuserdata.service.IASUserDataService;
import net.anotheria.util.crypt.CryptTool;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vbezuhlyi
 * @see CMSUser
 * @see net.anotheria.anosite.cms.action.LoginAction
 * @see net.anotheria.anosite.gen.shared.filter.CMSFilter
 * @see net.anotheria.anosite.gen.shared.action.BaseAnositeAction
 **/
public class CMSUserManager {
    
    private static final String CRYPT_KEY = "97531f6c04afcbd529028f3f45221cce";
    private static CryptTool crypt;

    private static Map<String, CMSUser> users;

    private static IASUserDataService userDataService;
    private static Logger log;
    private static CMSUserManager instance;
    private static boolean inited;

    static {
        log = Logger.getLogger(CMSUserManager.class);
        crypt = new CryptTool(CRYPT_KEY);
    }


    private CMSUserManager() {
        if (!inited)
            throw new RuntimeException("CMS user manager is not initialized");
    }


    public static CMSUserManager getInstance() {
        if (instance == null) {
            instance = new CMSUserManager();
        }
        return instance;
    }


    public boolean canLoginUser(String login, String password) {
        CMSUser user = users.get(login);
        password = encryptPassword(password);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }


    public boolean userInRole(String userId, String role) {
        String login = getUserDefLoginById(userId);
        return isKnownUser(login) && users.get(login).isUserInRole(role);
    }


    public boolean isKnownUser(String login) {
        return users.containsKey(login);
    }


    public static void init() {
        if (inited) {
            return;
        }

        users = new ConcurrentHashMap<String, CMSUser>();
        try {
            userDataService = MetaFactory.get(IASUserDataService.class);
            
            // initial creating of default roles and users
            createNecessaryDefaultRolesAndUsers();

            // initial filling of map from CMS persistence files
            scanUsers();

            inited = true;

        } catch (MetaFactoryException e) {
            log.error("init failed", e);
            throw new RuntimeException("MetaFactory failed", e);
        }
    }


    private static void createNecessaryDefaultRolesAndUsers() {
        try {
            // adding default roles
            // + developer if already not exists
            addDefaultRole("developer");
            // + cmsuser if already not exists
            addDefaultRole("cmsuser");
            // + editor if already not exists
            addDefaultRole("editor");
            // + producer if already not exists
            addDefaultRole("producer");
            // + admin if already not exists
            addDefaultRole("admin");

            // adding default users
            // + admin:admin if necessary
            addDefaultUser("admin", encryptPassword("admin"), "admin");

        } catch (ASUserDataServiceException e) {
            log.error("ASUserDataService failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
    }


    private static void addDefaultUser(String login, String password, String role) throws ASUserDataServiceException {
        List<UserDef> userDefs = userDataService.getUserDefsByProperty(UserDef.PROP_LOGIN, login);
        if (userDefs == null || userDefs.isEmpty()) { // check if such user does not exist
            List<RoleDef> roleDefs = userDataService.getRoleDefsByProperty(RoleDef.PROP_NAME, role);
            if (roleDefs == null || roleDefs.isEmpty()) { // check if role for admin user does not exist
                log.error("There is no admin role for admin user in CMS");
                throw new RuntimeException("admin role for admin user is undefined");
            }
            RoleDef adminRole = roleDefs.get(0);

            UserDefBuilder userDefBuilder = new UserDefBuilder();
            userDefBuilder.login(login);
            userDefBuilder.password(password);
            userDefBuilder.status(Arrays.asList(adminRole.getId()));
            userDataService.createUserDef(userDefBuilder.build());
        }
    }


    private static void addDefaultRole(String role) throws ASUserDataServiceException {
        List<RoleDef> roleDefs = userDataService.getRoleDefsByProperty(RoleDef.PROP_NAME, role);
        if (roleDefs == null || roleDefs.isEmpty()) { // check if such role does not exist
            RoleDefBuilder roleDefBuilder = new RoleDefBuilder();
            roleDefBuilder.name(role);
            userDataService.createRoleDef(roleDefBuilder.build());
        }
    }


    public static void changeUserPassword(String login, String newPassword) {
        try {
            UserDef user = getUserDefByLogin(login);
            user.setPassword(encryptPassword(newPassword));
            userDataService.updateUserDef(user);
        } catch (ASUserDataServiceException e) {
            log.error("change user password failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
    }


    public static String encryptPassword(String plainTextPassword) {
        return crypt.encryptToHex(plainTextPassword) + getCryptMarker();
    }

    public static String getCryptMarker() {
        return "//encrypted";
    }


    public static String getUserDefIdByLogin(String login) {
         return getUserDefByLogin(login).getId();
    }


    public static String getUserDefLoginById(String id) {
        try {
            return userDataService.getUserDef(id).getLogin();

        } catch (ASUserDataServiceException e) {
            log.error("get user def by login failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
    }


    private static UserDef getUserDefByLogin (String login) {
        try {
            List<UserDef> userDefs = userDataService.getUserDefsByProperty(UserDef.PROP_LOGIN, login);
            return userDefs.get(0);

        } catch (ASUserDataServiceException e) {
            log.error("get user def by login failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
    }


    public static void scanUsers() {
        String username, password;
        List<String> rolesIds, roles;

        try {
            List<UserDef> usersList = userDataService.getUserDefs();
            users.clear();

            for (UserDef userDef : usersList) {
                username = userDef.getLogin();
                password = userDef.getPassword();
                rolesIds = userDef.getStatus(); // getting ID of user role

                roles = new ArrayList<String>();
                if (rolesIds != null || !rolesIds.isEmpty()) {
                    for (String roleId : rolesIds) {
                        RoleDef roleDef = userDataService.getRoleDef(roleId);
                        if (roleDef != null) {
                            roles.add(roleDef.getName()); // getting name of user role
                        }
                    }

                    CMSUser userToAdd = new CMSUser(username, password, roles);
                    users.put(userToAdd.getUsername(), userToAdd);
                }
            }
        } catch (ASUserDataServiceException e) {
            log.error("scan users failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
    }

}
