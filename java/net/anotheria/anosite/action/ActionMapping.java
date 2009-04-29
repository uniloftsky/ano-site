package net.anotheria.anosite.action;

import net.anotheria.anosite.gen.ascustomaction.data.ActionMappingDef;

public class ActionMapping {


	private ActionMappingDef def;
	private ActionCommand predefinedCommand;
	
	public ActionMapping(ActionMappingDef aDef){
		def = aDef;
		predefinedCommand = new ActionCommand(def);
	}
	
	
	public ActionCommand getPredefinedCommand(){
		return predefinedCommand;
	}
}
