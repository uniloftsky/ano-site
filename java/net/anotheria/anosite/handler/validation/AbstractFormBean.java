package net.anotheria.anosite.handler.validation;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Form definition bean used in {@link AbstractValidationBoxHandler}.
 * 
 * @author Alexandr Bolbat
 */
public abstract class AbstractFormBean implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = 3724770407071535482L;

	/**
	 * Utility constant with empty {@link String} value.
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * Utility constant with empty {@link Number} value.
	 */
	public static final Number EMPTY_NUMBER = -1;

	/**
	 * Utility constant with empty {@link Boolean} value.
	 */
	public static final boolean EMPTY_BOOLEAN = false;

	/**
	 * Utility constant with empty {@link List} value.
	 */
	public static final List<?> EMPTY_LIST = Collections.emptyList();

	/**
	 * Utility constant with empty {@link Set} value.
	 */
	public static final Set<?> EMPTY_SET = Collections.emptySet();

	/**
	 * Internal storage for form data.
	 */
	private final Map<String, Object> formData = new HashMap<String, Object>();

	/**
	 * Save field value to form data storage.
	 * 
	 * @param name
	 *            - field name
	 * @param value
	 *            - field value
	 */
	protected final void saveField(final String name, final Object value) {
		formData.put(name, value);
	}

	/**
	 * Read field value from form data storage. Return value or <code>null</code>.
	 * 
	 * @param name
	 *            - field name
	 * @return {@link Object}
	 */
	protected final Object readField(final String name) {
		return formData.get(name);
	}

	/**
	 * Get array of all form fields names.
	 * 
	 * @return array of {@link String}
	 */
	protected abstract String[] getFieldsNames();

	/**
	 * Get form field value by form field name.
	 * 
	 * @param fieldName
	 *            - form field name
	 * @return {@link Object}
	 */
	protected abstract Object getFieldValue(final String fieldName);

	/**
	 * Any bean extended from this abstract class must have possibility to initialize by self from request.
	 * 
	 * @param req
	 *            - request
	 */
	protected abstract void prepare(final HttpServletRequest req);

}
