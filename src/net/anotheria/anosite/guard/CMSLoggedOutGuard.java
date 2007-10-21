package net.anotheria.anosite.guard;

public class CMSLoggedOutGuard extends CMSLogedInGuard{

	@Override
	protected boolean getDesiredResult() {
		return false;
	}
	
}
