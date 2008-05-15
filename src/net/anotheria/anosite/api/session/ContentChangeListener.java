package net.anotheria.anosite.api.session;

import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.listener.IServiceListener;

public class ContentChangeListener implements IServiceListener{
	
	public void documentCreated(DataObject doc) {
		sendEvent(ContentChangeType.CREATE, doc);
	}

	public void documentDeleted(DataObject doc) {
		sendEvent(ContentChangeType.DELETE, doc);
		
	}

	public void documentUpdated(DataObject oldVersion, DataObject newVersion) {
		sendEvent(ContentChangeType.UPDATE, oldVersion);
		
	}
	
	private void sendEvent(ContentChangeType type, DataObject object){
		ContentChangeEvent event = new ContentChangeEvent(type, object.getDefinedName(), object.getId());
		APISessionManager.getInstance().propagateContentChangeEvent(event);
	}

}
