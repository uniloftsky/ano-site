package net.anotheria.anosite.api.session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import net.anotheria.anoprise.sessiondistributor.SessionAttribute;
import net.anotheria.anoprise.sessiondistributor.SessionDistributorService;
import net.anotheria.anoprise.sessiondistributor.SessionDistributorServiceException;
import net.anotheria.net.util.ByteArraySerializer;

public final class APISessionDistributionHelper {
	private static SessionDistributorService distributorService;
	private static Logger log = Logger.getLogger(APISessionDistributionHelper.class);
	
	public static void setSessionDistributorService(SessionDistributorService aSessionDistributorService){
		distributorService = aSessionDistributorService;
	}
	
	/**
	 * Distributes the session. Returns the name of the distributed session.
	 * @param session
	 * @return
	 */
	public static String distributeSession(APISession session) throws APISessionDistributionException{
		if (distributorService==null)
			throw new IllegalStateException("No SessionDistributorService configured. Please set a SessionDistributorService.");
		if (!(session instanceof APISessionImpl))
			throw new AssertionError("Unknown APISession implementation");
		List<SessionAttribute> attributes = createAttributeList(session);
		try{
			return distributorService.createDistributedSession(attributes);
		}catch(SessionDistributorServiceException e){
			throw new APISessionDistributionException(e);
		}
	}
	
	public static void restoreSession(String distributedSessionName, APISession session) throws APISessionDistributionException{
		if (distributorService==null)
			throw new IllegalStateException("No SessionDistributorService configured. Please set a SessionDistributorService.");
		if (!(session instanceof APISessionImpl))
			throw new AssertionError("Unknown APISession implementation");
		List<SessionAttribute> attributes = null;
		try{
			attributes = distributorService.getAndDeleteDistributedSession(distributedSessionName);
		}catch(SessionDistributorServiceException e){
			throw new APISessionDistributionException(e);
		}
		
		APISessionImpl sessionImpl = (APISessionImpl) session;
		for (SessionAttribute attribute : attributes){
			try{
				AttributeWrapper wrapper = (AttributeWrapper)ByteArraySerializer.deserializeObject(attribute.getData());
				sessionImpl.setAttributeWrapper(wrapper);
			}catch(IOException e){
				log.error("exception occured attempting to deserialize this attribute: "+attribute, e);
			}
		}
		
	}
	
	private static List<SessionAttribute> createAttributeList(APISession session){
		Collection<AttributeWrapper> attributes = ((APISessionImpl)session).getAttributes();
		ArrayList<SessionAttribute> ret = new ArrayList<SessionAttribute>();
		for (AttributeWrapper wrapper : attributes){
			if (PolicyHelper.isDistributed(wrapper.getPolicy())){
				if (wrapper.isSerializable()){
					try{
						byte[] data = ByteArraySerializer.serializeObject(wrapper);
						ret.add(new SessionAttribute(wrapper.getKey(), data));
					}catch(IOException e){
						log.error("exception occured attempting to serialize this attribute: "+wrapper, e);
					}
				}else{
					log.warn("Attribute "+wrapper.getKey()+" is marked as distributed but is not serializeable, skipped.");
				}
			}
		}
		return ret;
	}
	
	private APISessionDistributionHelper(){}
}
