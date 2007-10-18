package net.anotheria.anosite.guard;

public class CMSLogedInGuard extends SessionFlagPresentGuard{
	protected String getFlagName(){
		return "currentUserId";
	}
}
