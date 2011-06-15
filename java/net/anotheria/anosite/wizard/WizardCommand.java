package net.anotheria.anosite.wizard;


import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Wizard step navigation button constants.
 *
 * @author h3ll
 */
public enum WizardCommand {
	/**
	 * Cancel.
	 */
	CANCEL("cancel"),
	/**
	 * Finish.
	 */
	FINISH("finish"),
	/**
	 * Previous.
	 */
	PREVIOUS("previous"),
	/**
	 * Next.
	 */
	NEXT("next"),

	/**
	 * NAVIGATE_TO  step.
	 */
	NAVIGATE_TO("navigate_to");

	/**
	 * WizardCommand default.
	 */
	public static WizardCommand DEFAULT_COMMAND = WizardCommand.NEXT;

	/**
	 * ButtonTitle.
	 */
	private String commandTitle;

	/**
	 * Constructor.
	 *
	 * @param aCommandTitle string title
	 */
	WizardCommand(String aCommandTitle) {
		this.commandTitle = aCommandTitle;
	}

	public String getCommandTitle() {
		return commandTitle;
	}

	@Override
	public String toString() {
		return commandTitle;
	}

	/**
	 * Returns {@link WizardCommand} .
	 *
	 * @param parameters {@link Map<String,String>} actually request parameter map
	 * @return {@link WizardCommand}, if not found in map NEXT will be returned
	 */
	public static WizardCommand getCommandByValue(Map<String, String[]> parameters) {
		for (String param : parameters.keySet())
			for (WizardCommand button : values())
				if (param.equals(button.getCommandTitle()))
					return button;

		Logger.getLogger(WizardCommand.class).debug("Command not found! Relying on defaults!");
		return DEFAULT_COMMAND;
	}

	/**
	 * Return {@link WizardCommand} with selected value. (Comparing using ignore-case).
	 *
	 * @param command incoming string
	 * @return {@link WizardCommand} with selected command value, or DEFAULT_COMMAND if nothing found.
	 */
	public static WizardCommand getCommandByValue(String command) {
		for (WizardCommand wizCommand : values()) {
			if (wizCommand.getCommandTitle().equalsIgnoreCase(command))
				return wizCommand;
		}
		Logger.getLogger(WizardCommand.class).debug("Command not found! Relying on defaults! command[" + command + "]");
		return DEFAULT_COMMAND;
	}

}

