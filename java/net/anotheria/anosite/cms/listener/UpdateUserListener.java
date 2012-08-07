package net.anotheria.anosite.cms.listener;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.cms.user.CMSUserManager;
import net.anotheria.anosite.gen.asuserdata.data.UserDef;
import net.anotheria.anosite.gen.asuserdata.service.ASUserDataServiceException;
import net.anotheria.anosite.gen.asuserdata.service.IASUserDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.listener.IServiceListener;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Listener for checking user's updates
 * @author vbezuhlyi
 * @see CMSUserManager
 */

public class UpdateUserListener implements IServiceListener {

    private static Logger log;
    private static IASUserDataService userDataService;

    static {
        log = Logger.getLogger(UpdateUserListener.class);

        try {
            userDataService= MetaFactory.get(IASUserDataService.class);
        } catch(MetaFactoryException e) {
            log.error("MetaFactory failed", e);
            throw new RuntimeException("MetaFactory failed", e);
        }
    }

	@Override
    public void documentUpdated(DataObject dataObject, DataObject dataObject1) {
        updateUser(dataObject1);
    }

    @Override
    public void documentDeleted(DataObject dataObject) {
        updateUser(dataObject);
    }

    @Override
    public void documentCreated(DataObject dataObject) {
        updateUser(dataObject);
    }

    @Override
    public void documentImported(DataObject dataObject) {
        updateUser(dataObject);
    }

	@Override
	public void persistenceChanged() {}


    private void updateUser(DataObject dataObject) {
        UserDef userDef = (UserDef) dataObject;

        if(isTryingToCreateExistedLogin(userDef)) {
            userDef.setLogin(userDef.getLogin() + userDef.getId());
        }

        if(!userDef.getPassword().contains(CMSUserManager.getHashMarker())) {
            userDef.setPassword(CMSUserManager.hashPassword(userDef.getPassword()));
        }

        /* updating info as user probably has new login or password */
        CMSUserManager.scanUsers();

    }


    private boolean isTryingToCreateExistedLogin(UserDef userDef) {
        try {
            List<UserDef> existedUserDefs = userDataService.getUserDefs();

            if (existedUserDefs == null || existedUserDefs.isEmpty()) {
                return false;
            }

            boolean isTrying = false;

            for(UserDef existedUserDef : existedUserDefs)
                if(userDef.getLogin().equals(existedUserDef.getLogin())) {
                    if (!userDef.getId().equals(existedUserDef.getId())) { // additional comparing of ids to allow user to change his password
                        isTrying = true;
                    }
                }

            return isTrying;

        } catch(ASUserDataServiceException e) {
            log.error("ASUserDataService failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
    }

}
