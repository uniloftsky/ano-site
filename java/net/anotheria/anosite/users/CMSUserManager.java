package net.anotheria.anosite.users;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asuserdata.data.RoleDef;
import net.anotheria.anosite.gen.asuserdata.data.RoleDefBuilder;
import net.anotheria.anosite.gen.asuserdata.data.UserDef;
import net.anotheria.anosite.gen.asuserdata.data.UserDefBuilder;
import net.anotheria.anosite.gen.asuserdata.service.ASUserDataServiceException;
import net.anotheria.anosite.gen.asuserdata.service.IASUserDataService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author vbezuhlyi
 * @see CMSUser
 * @see LoginAction
 * @see net.anotheria.anosite.gen.shared.filter.CMSFilter
 * @see net.anotheria.anosite.gen.shared.action.BaseAnositeAction
 **/
public class CMSUserManager {
    private static Map<String, CMSUser> users;

    private static IASUserDataService userDataService;
    private static Logger log;
    private static CMSUserManager instance;
    private static boolean inited;

    static {
        log = Logger.getLogger(CMSUserManager.class);
    }

    private CMSUserManager() {
        if (!inited)
            throw new RuntimeException("Not inited");
    }

    public static CMSUserManager getInstance() {
        if (instance == null) {
            instance = new CMSUserManager();
        }
        return instance;
    }

    public boolean canLoginUser(String userId, String password) {
        CMSUser user = users.get(userId);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public boolean userInRole(String userId, String role) {
        return isKnownUser(userId) && users.get(userId).isUserInRole(role);
    }

    public boolean isKnownUser(String userId) {
        return users.containsKey(userId);
    }

    public static void init() {
        if (inited) {
            return;
        }

        users = new ConcurrentHashMap<String, CMSUser>();
        try {
            userDataService = MetaFactory.get(IASUserDataService.class);
            
            // initial creating of default roles and users
            if (defaultRolesAndUsersAreCorrupted()) {
                createDefaultRolesAndUsers();
            }
            
            // initial filling of map from CMS persistence files
            scanUsers();

            inited = true;
        } catch (MetaFactoryException e) {
            log.error("init", e);
        }
    }

    private static void createDefaultRolesAndUsers() {
        try {
            IASUserDataService userDataService = MetaFactory.get(IASUserDataService.class);

            // adding default roles
            RoleDefBuilder roleDefBuilder = new RoleDefBuilder();
            roleDefBuilder.name("admin");
            RoleDef adminRole = userDataService.createRoleDef(roleDefBuilder.build());
            roleDefBuilder.name("producer");
            userDataService.createRoleDef(roleDefBuilder.build());

            // adding default users
            UserDefBuilder userDefBuilder = new UserDefBuilder();
            userDefBuilder.login("root");
            userDefBuilder.password("root");
            List<String> roles = new ArrayList<String>();
            roles.add(adminRole.getId());
            userDefBuilder.status(roles);
            userDataService.createUserDef(userDefBuilder.build());

        } catch (MetaFactoryException e) {
           log.error("MetaFactory failed", e);
            throw new RuntimeException("MetaFactory failed", e);
        } catch (ASUserDataServiceException e) {
            log.error("ASUserDataService failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
    }

    private static boolean defaultRolesAndUsersAreCorrupted() {
        return true; // DEBUG
    }

    public static void scanUsers() {
        String username;
        String password;
        List<String> rolesIds;
        List<String> roles;

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
                            roles.add(roleDef.getName());
                        }
                    }

                    CMSUser userToAdd = new CMSUser(username, password, roles);
                    users.put(userToAdd.getUsername(), userToAdd);
                }
            }
        } catch (ASUserDataServiceException e) {
            log.error("scan users", e);
        }
    }

}
