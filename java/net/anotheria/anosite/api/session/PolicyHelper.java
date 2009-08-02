package net.anotheria.anosite.api.session;

/**
 * Helper class for operations on APISession policies.
 * @author lrosenberg
 *
 */
public class PolicyHelper {
	/**
	 * Returns true if the policy autoexpire is set in this integer value.
	 * @param policy
	 * @return
	 */
	public static final boolean isAutoExpiring(int policy){
		return isPolicySet(policy, APISession.POLICY_AUTOEXPIRE);
	}
	/**
	 * Returns true if the specified policy is set.
	 * @param policyToCheck
	 * @param expectedPolicy
	 * @return
	 */
	public static final boolean isPolicySet(int policyToCheck, int expectedPolicy ){
		return !((policyToCheck & expectedPolicy) == 0);
	}
	/**
	 * Returns true if the distributed attribute policy is set.
	 * @param policy
	 * @return
	 */
	public static final boolean isDistributed(int policy){
		return isPolicySet(policy, APISession.POLICY_DISTRIBUTED);
	}
}
