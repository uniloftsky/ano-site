package net.anotheria.anosite.wizard.handler;


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
	public static  WizardCommand getCommandByValue(Map<String, String[]> parameters) {
		for (String param : parameters.keySet())
			for (WizardCommand button : values())
				if (param.equals(button.getCommandTitle()))
					return button;

		//Default
		return DEFAULT_COMMAND;
	}

}

