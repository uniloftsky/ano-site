package net.anotheria.anosite.handler;

public class ResponseRedirectImmediately extends AbstractRedirectResponse{
	
	public ResponseRedirectImmediately(String redirectUrl){
		super(redirectUrl);
	}
	
	public BoxHandlerResponseCode getResponseCode(){
		return BoxHandlerResponseCode.CANCEL_AND_REDIRECT;
	}
}
