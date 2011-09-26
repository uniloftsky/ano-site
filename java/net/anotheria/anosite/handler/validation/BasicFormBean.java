package net.anotheria.anosite.handler.validation;

import javax.servlet.http.HttpServletRequest;

/**
 * Basic form bean. Use it as example for real form beans.
 * 
 * @author Alexandr Bolbat
 */
public class BasicFormBean extends AbstractFormBean {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 5952785433197663177L;

	/**
	 * First name.
	 */
	public static final String FIRST_NAME = "firstName";

	/**
	 * First name type class.
	 */
	private static final Class<String> firstNameTypeClass = String.class;

	/**
	 * Last name.
	 */
	public static final String LAST_NAME = "lastName";

	/**
	 * Last name type class.
	 */
	private static final Class<String> lastNameTypeClass = String.class;

	@Override
	protected String[] getFieldsNames() {
		return new String[] { FIRST_NAME, LAST_NAME };
	}

	@Override
	protected Object getFieldValue(String fieldName) {
		return readField(fieldName);
	}

	@Override
	protected void prepare(HttpServletRequest req) {
		String firstName = firstNameTypeClass.cast(req.getAttribute(FIRST_NAME));
		saveField(FIRST_NAME, firstName);

		String lastName = lastNameTypeClass.cast(req.getAttribute(LAST_NAME));
		saveField(LAST_NAME, lastName);
	}

	/**
	 * Get first name in right type.
	 * 
	 * @return {@link String}
	 */
	public String getFirstName() {
		return readField(FIRST_NAME, String.class, EMPTY_STRING);
	}

	/**
	 * Get last name in right type.
	 * 
	 * @return {@link String}
	 */
	public String getLastName() {
		return readField(LAST_NAME, String.class, EMPTY_STRING);
	}

}
