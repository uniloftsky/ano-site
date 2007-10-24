package net.anotheria.anosite.content.servlet;

import net.anotheria.anosite.content.bean.PageBean;
import net.anotheria.anosite.shared.InternalResponseCode;

public class InternalPageBeanResponse extends InternalResponse {
	private PageBean pageBean;
	
	public InternalPageBeanResponse(PageBean aPageBean){
		this(InternalResponseCode.CONTINUE, aPageBean);
	}
	
	public InternalPageBeanResponse(InternalResponseCode code, PageBean aPageBean){
		super(code);
		pageBean = aPageBean;
	}
	
	public PageBean getPageBean(){
		return pageBean;
	}
}
