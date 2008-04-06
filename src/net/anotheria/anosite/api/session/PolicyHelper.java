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
package net.anotheria.anosite.api.session;

public class PolicyHelper {
	public static final boolean isAutoExpiring(int policy){
		return isPolicySet(policy, APISession.POLICY_AUTOEXPIRE);
	}
	
	public static final boolean isPolicySet(int policyToCheck, int expectedPolicy ){
		return !((policyToCheck & expectedPolicy) == 0);
	}
	
	public static final boolean isDistributed(int policy){
		return isPolicySet(policy, APISession.POLICY_DISTRIBUTED);
	}
}

/* ------------------------------------------------------------------------- *
 * $Log$
 * Revision 1.3  2006/11/17 15:27:20  lrosenberg
 * *** empty log message ***
 *
 * Revision 1.2  2006/10/04 14:12:50  lrosenberg
 * *** empty log message ***
 *
 * Revision 1.1  2006/10/04 12:39:54  lrosenberg
 * *** empty log message ***
 *
 */