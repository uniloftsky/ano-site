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
package net.anotheria.anosite.api.common;

public class NoSuchAPIException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5683012743530462076L;

	public NoSuchAPIException(String apiName){
		super("API "+apiName+" not found.");
	}
}

/* ------------------------------------------------------------------------- *
 * $Log$
 * Revision 1.1  2006/09/20 08:09:19  lrosenberg
 * *** empty log message ***
 *
 * Revision 1.1  2006/06/21 12:51:22  lrosenberg
 * *** empty log message ***
 *
 */