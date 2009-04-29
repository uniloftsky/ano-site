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
