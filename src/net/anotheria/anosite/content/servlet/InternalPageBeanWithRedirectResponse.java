package net.anotheria.anosite.content.servlet;

import net.anotheria.anosite.content.bean.PageBean;

public class InternalPageBeanWithRedirectResponse extends InternalPageBeanResponse{
	
	private String redirectUrl;
	
	public InternalPageBeanWithRedirectResponse(PageBean bean, String aRedirectUrl){
		super(bean);
		redirectUrl = aRedirectUrl;
	}
	
	public String getRedirectUrl(){
		return redirectUrl;
	}
}
