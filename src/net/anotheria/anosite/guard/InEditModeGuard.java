package net.anotheria.anosite.guard;

import net.anotheria.anosite.util.AnositeConstants;

public class InEditModeGuard extends SessionFlagPresentGuard{

	@Override
	protected String getFlagName() {
		return AnositeConstants.SA_EDIT_MODE_FLAG;
	}
	
}
