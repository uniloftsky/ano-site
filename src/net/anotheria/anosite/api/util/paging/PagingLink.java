/* ------------------------------------------------------------------------- *
$Source$
$Author$
$Date$
$Revision$


Copyright 2004-2005 by FriendScout24 GmbH, Munich, Germany.
All rights reserved.

This software is the confidential and proprietary information
of FriendScout24 GmbH. ("Confidential Information").  You
shall not disclose such Confidential Information and shall use
it only in accordance with the terms of the license agreement
you entered into with FriendScout24 GmbH.
See www.friendscout24.de for details.
** ------------------------------------------------------------------------- */
package net.anotheria.anosite.api.util.paging;

public class PagingLink extends PagingElement{
	
	private String pagingParameter;
	private String caption;
	
	public PagingLink(String aCaption, int pageNumber){
		caption = aCaption;
		pagingParameter = ""+pageNumber;
	}

	@Override
	public String getPagingParameter() {
		return pagingParameter;
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public boolean isLinked() {
		return true;
	}

	@Override
	public boolean isSeparator() {
		return false;
	}
	
	@Override
	public String getCaption(){
		return caption;
	}
	
	@Override
	public String toString(){
		return "caption:"+caption+", pagingParameter:"+pagingParameter;
	}
}
