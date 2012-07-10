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
import java.util.Arrays;
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
            throw new RuntimeException("CMS user manager not inited");
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
            createNecessaryDefaultRolesAndUsers();

            // initial filling of map from CMS persistence files
            scanUsers();

            inited = true;
        } catch (MetaFactoryException e) {
            log.error("init", e);
        }
    }

    private static void createNecessaryDefaultRolesAndUsers() {
        try {
            IASUserDataService userDataService = MetaFactory.get(IASUserDataService.class);

            // adding default roles
            RoleDefBuilder roleDefBuilder = new RoleDefBuilder();

            //if
            roleDefBuilder.name("developer");
            userDataService.createRoleDef(roleDefBuilder.build());

            //if
            roleDefBuilder.name("cmsuser");
            userDataService.createRoleDef(roleDefBuilder.build());

            //if
            roleDefBuilder.name("editor");
            userDataService.createRoleDef(roleDefBuilder.build());

            // if
            roleDefBuilder.name("producer");
            userDataService.createRoleDef(roleDefBuilder.build());

            // if
            roleDefBuilder.name("admin");
            RoleDef adminRole = userDataService.createRoleDef(roleDefBuilder.build());


            // adding default users
            UserDefBuilder userDefBuilder = new UserDefBuilder();
            List<UserDef> userDefs;

            // adding admin:admin
            userDefs = userDataService.getUserDefsByProperty(UserDef.PROP_LOGIN, "admin");
            if (userDefs == null || userDefs.isEmpty()) {
                userDefBuilder.login("admin");
                userDefBuilder.password("admin");
                userDefBuilder.status(Arrays.asList(adminRole.getId()));
                userDataService.createUserDef(userDefBuilder.build());
            }

        } catch (MetaFactoryException e) {
            log.error("MetaFactory failed", e);
            throw new RuntimeException("MetaFactory failed", e);
        } catch (ASUserDataServiceException e) {
            log.error("ASUserDataService failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
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
                            roles.add(roleDef.getName()); // getting name of user role
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
