package net.anotheria.anosite.cms.listener;

import net.anotheria.asg.util.listener.IServiceListener;
import net.anotheria.asg.data.DataObject;
import net.anotheria.anosite.api.common.APICallContext;
import org.apache.log4j.Logger;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * Simplest Listener - for Logging CRUD operations.
 *
 * @author h3llka
 */

public class CRUDLogListener implements IServiceListener {
	/**
	 * Logger.
	 */
    private final Logger log = Logger.getLogger("cms-crud-log");
	/**
	 * Separator.
	 */
    private static final String SEPARATOR = " , ";

    @Override
    public void documentUpdated(DataObject dataObject, DataObject dataObject1) {
        logData("UPDATE", createObjString(dataObject, dataObject1));
    }

    @Override
    public void documentDeleted(DataObject dataObject) {
        logData("DELETE", createObjString(dataObject));
    }

    @Override
    public void documentCreated(DataObject dataObject) {
        logData("CREATE", createObjString(dataObject));
    }

    @Override
    public void documentImported(DataObject dataObject) {
        logData("IMPORT", createObjString(dataObject));
    }

    /**
     * Just creates string with doc.id && clazz name
     *
     * @param dataObjects objects array
     * @return created string
     */
    private String createObjString(DataObject... dataObjects) {
        String result = "";
        int count = 0;
        for (DataObject obj : dataObjects) {
            result += count == 0 ? " DOCUMENT [ id : " + obj.getId() + " ] " :
                    " DOCUMENT" + count + " [ id : " + obj.getId() + " ] ";
            count++;
        }
        result += " --> class(zz) : " + dataObjects[0].getClass().toString().substring(dataObjects[0].getClass().toString().lastIndexOf(".") + 1);
        return result;
    }

    /**
     * Writing data to log
     *
     * @param operation - CREATE - UPDATE - DELETE etc.
     * @param objData   object info
     */
    private void logData(String operation, String objData) {
        String user = APICallContext.getCallContext().getCurrentUserId();
        String editor = APICallContext.getCallContext().getCurrentEditorId();
        String time = new SimpleDateFormat().format(new Date());
        log.info(time + SEPARATOR + operation + SEPARATOR + " uid : " + user + SEPARATOR + "eid : " + editor + SEPARATOR + objData);
    }
}
