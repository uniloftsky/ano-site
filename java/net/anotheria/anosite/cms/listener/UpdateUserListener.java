package net.anotheria.anosite.cms.listener;

import net.anotheria.anoprise.metafactory.MetaFactory;
import net.anotheria.anoprise.metafactory.MetaFactoryException;
import net.anotheria.anosite.gen.asuserdata.service.IASUserDataService;
import net.anotheria.anosite.gen.asuserdata.data.UserDef;
import net.anotheria.anosite.gen.asuserdata.service.IASUserDataService;
import net.anotheria.anosite.gen.asuserdata.service.ASUserDataServiceException;
import net.anotheria.anosite.guard.SystemEnvironmentAbstractGuard;
import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.data.ObjectInfo;
import net.anotheria.asg.util.listener.IServiceListener;
import net.anotheria.util.NumberUtils;
import net.anotheria.util.crypt.CryptTool;

import java.util.List;

/**
 * Listener for checking user's updates
 *
 * @author h3llka
 */

public class UpdateUserListener implements IServiceListener {

	private static final String AUTH_KEY = "97531f6c04afcbd529028f3f45221cce";
	private static CryptTool crypt = new CryptTool(AUTH_KEY);

	@Override
    public void documentUpdated(DataObject dataObject, DataObject dataObject1) {
        updateUser(dataObject, dataObject1);
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

	/**
     * Just creates string with doc.id && clazz name
     *
     * @param dataObjects objects array
     * @return created string
     */
    private void updateUser(DataObject... dataObjects) {
       try {
            IASUserDataService userDataService = MetaFactory.get(IASUserDataService.class);
            List<UserDef> userDefs = userDataService.getUserDefs();
            for (DataObject obj : dataObjects) {
		        UserDef user = (UserDef) obj;
                //if(checkUsers(userDefs, user))
                //    user.setLogin(user.getLogin() + user.getId());
		        if(user.getPassword().indexOf("//encrypted") == -1)
			        user.setPassword(crypt.encryptToHex(user.getPassword()) + "//encrypted");
            }
        }
        catch(MetaFactoryException e) {
            System.out.println("UpdateUserListener corrupted: " + e);
        }
        catch(ASUserDataServiceException e) {
            System.out.println("UpdateUserListener corrupted: " + e);
        }
    }


    private boolean checkUsers(List<UserDef> userDefs, UserDef user) {
        boolean flag = false;
        for(int i = 0; i < userDefs.size(); i++)
            if(user.getLogin().equals(userDefs.get(i).getLogin()))
                flag = true;
        return flag;
    }
}
