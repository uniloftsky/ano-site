package net.anotheria.anosite.handler;

public class ResponseRedirectAfterProcessing extends AbstractRedirectResponse{
	
	public ResponseRedirectAfterProcessing(String redirectUrl){
		super(redirectUrl);
	}
	
	public BoxHandlerResponseCode getResponseCode(){
		return BoxHandlerResponseCode.CONTINUE_AND_REDIRECT;
	}
}
