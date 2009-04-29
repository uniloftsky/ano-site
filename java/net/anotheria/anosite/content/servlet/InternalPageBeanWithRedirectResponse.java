package net.anotheria.anosite.content.servlet;

import net.anotheria.anosite.content.bean.PageBean;
import net.anotheria.anosite.shared.InternalResponseCode;

public class InternalPageBeanWithRedirectResponse extends InternalPageBeanResponse{
	
	private String redirectUrl;
	
	public InternalPageBeanWithRedirectResponse(PageBean bean, String aRedirectUrl){
		super(InternalResponseCode.CONTINUE_AND_REDIRECT, bean);
		redirectUrl = aRedirectUrl;
	}
	
	public String getRedirectUrl(){
		return redirectUrl;
	}
}
