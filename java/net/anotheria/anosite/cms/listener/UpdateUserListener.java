package net.anotheria.anosite.cms.listener;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asuserdata.data.UserDef;
import net.anotheria.anosite.gen.asuserdata.service.ASUserDataServiceException;
import net.anotheria.anosite.gen.asuserdata.service.IASUserDataService;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.listener.IServiceListener;
import net.anotheria.util.crypt.CryptTool;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Listener for checking user's updates
 *
 * @author h3llka
 */

public class UpdateUserListener implements IServiceListener {

	private static final String AUTH_KEY = "97531f6c04afcbd529028f3f45221cce";
	private static CryptTool crypt = new CryptTool(AUTH_KEY);
    private static Logger log;

    static {
        log = Logger.getLogger(UpdateUserListener.class);
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
	public void persistenceChanged() {
		updateUser(null);
	}


    private void updateUser(DataObject obj) {
        UserDef userDef = (UserDef) obj;

        if(isLoginAlreadyExists(userDef.getLogin(), userDef.getId())) {
                userDef.setLogin(userDef.getLogin() + userDef.getId());
        }

        if(!userDef.getPassword().contains("//encrypted")) {
           userDef.setPassword(crypt.encryptToHex(userDef.getPassword()) + "//encrypted");
        }
    }


    private boolean isLoginAlreadyExists(String login, String id) {
        try {
            IASUserDataService userDataService = MetaFactory.get(IASUserDataService.class);
            List<UserDef> existedUserDefs = userDataService.getUserDefs();

            boolean isExists = false;
            for(UserDef existedUserDef : existedUserDefs)
                if(login.equals(existedUserDef.getLogin()) && !id.equals(existedUserDef.getId())) {
                    isExists = true;
                }

            return isExists;
        }
        catch(MetaFactoryException e) {
            log.error("MetaFactory failed", e);
            throw new RuntimeException("MetaFactory failed", e);
        }
        catch(ASUserDataServiceException e) {
            log.error("ASUserDataService failed", e);
            throw new RuntimeException("ASUserDataService failed", e);
        }
    }

}
