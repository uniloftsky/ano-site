package net.anotheria.anosite.content.servlet;

import net.anotheria.anosite.content.bean.PageBean;
import net.anotheria.anosite.shared.InternalResponseCode;

/**
 * Internal response with a resulting page bean. Used for successful processing of a page.
 * @author lrosenberg
 *
 */
public class InternalPageBeanResponse extends InternalResponse {
	/**
	 * The resulting pageBean.
	 */
	private PageBean pageBean;
	/**
	 * Creates a new InternalPageBeanResponse with a pagebean and InternalResponseCode.CONTINUE.
	 * @param aPageBean
	 */
	public InternalPageBeanResponse(PageBean aPageBean){
		this(InternalResponseCode.CONTINUE, aPageBean);
	}
	/**
	 * Creates a new InternalPageBeanResponse with the given response code and pageBean.
	 * @param code
	 * @param aPageBean
	 */
	public InternalPageBeanResponse(InternalResponseCode code, PageBean aPageBean){
		super(code);
		pageBean = aPageBean;
	}
	
	/**
	 * Returns the page bean.
	 * @return
	 */
	public PageBean getPageBean(){
		return pageBean;
	}
}
