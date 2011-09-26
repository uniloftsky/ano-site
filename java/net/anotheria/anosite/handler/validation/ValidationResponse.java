package net.anotheria.anosite.handler.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Validation response container used in {@link AbstractValidationBoxHandler}.
 * 
 * @author Alexandr Bolbat
 */
public class ValidationResponse implements Serializable {

	/**
	 * Basic serialVersionUID variable.
	 */
	private static final long serialVersionUID = -4130382436892907495L;

	/**
	 * Empty validation response.
	 */
	public static final ValidationResponse EMPTY_RESPONSE = new ValidationResponse();

	/**
	 * Fields data.
	 */
	private final Map<String, FieldData> fieldsData = new HashMap<String, FieldData>();

	/**
	 * Create new instance of {@link ValidationResponse}.
	 * 
	 * @return {@link ValidationResponse}
	 */
	public static ValidationResponse create() {
		return new ValidationResponse();
	}

	/**
	 * Add field with error key to response.
	 * 
	 * @param fieldName
	 *            - field name
	 * @param errorKey
	 *            - error key
	 */
	public void addError(final String fieldName, final String errorKey) {
		FieldData fData = fieldsData.get(fieldName);
		if (fData == null) {
			fData = new FieldData();
			fieldsData.put(fieldName, fData);
		}

		fData.addErrorKey(errorKey);
	}

	/**
	 * Is validation response has errors.
	 * 
	 * @return <code>true</code> or <code>false</code>
	 */
	public boolean hasErrors() {
		return !fieldsData.isEmpty();
	}

	/**
	 * {@link ValidationResponse} supported formats.
	 * 
	 * @author Alexandr Bolbat
	 */
	public enum Format {

		/**
		 * Pure JSON output.
		 */
		JSON("json"),

		/**
		 * JSON in HTML JavaScript tag.
		 */
		HTML_JS_JSON("htmlJsJson");

		/**
		 * Default format.
		 */
		public static final Format DEFAULT = JSON;

		/**
		 * Format name.
		 */
		private String name;

		/**
		 * Constructor.
		 * 
		 * @param aName
		 *            - format name
		 */
		Format(String aName) {
			this.name = aName;
		}

		/**
		 * Get format by name. If format with given name not found - DEFAULT format will be returned.
		 * 
		 * @param aName
		 *            - format name
		 * @return {@link Format}
		 */
		public static final Format getFormat(final String aName) {
			for (Format format : Format.values())
				if (format.name.equalsIgnoreCase(aName))
					return format;

			return DEFAULT;
		}
	}

	@Override
	public final String toString() {
		return toString(Format.DEFAULT);
	}

	/**
	 * Get response as {@link String} in some supported format.
	 * 
	 * @param formatName
	 *            - format name
	 * @return {@link String}
	 */
	public final String toString(String formatName) {
		return toString(Format.getFormat(formatName));
	}

	/**
	 * Get response as {@link String} in some supported format.
	 * 
	 * @param format
	 *            - format
	 * @return {@link String}
	 */
	public final String toString(Format format) {
		try {
			switch (format) {
			case JSON:
				return asJSON();
			case HTML_JS_JSON:
				return asHTML_JS_JSON();
			default:
				return toString(Format.DEFAULT);
			}
		} catch (JSONException e) {
			throw new RuntimeException("Preparing JSON fail.", e);
		}
	}

	/**
	 * Get response as JSON {@link String}.
	 * 
	 * @return {@link String}
	 * @throws JSONException
	 */
	private String asJSON() throws JSONException {
		JSONObject result = new JSONObject();

		// prepare status
		result.put("status", (hasErrors() ? "ERROR" : "OK"));

		// prepare errors
		if (hasErrors()) {
			JSONObject errorsScope = new JSONObject();

			// prepare errors
			for (String fieldName : fieldsData.keySet())
				errorsScope.put(fieldName, new JSONArray(fieldsData.get(fieldName).getErrorsKeys()));

			result.put("errors", errorsScope);
		}

		return result.toString();
	}

	/**
	 * Get response as HTML_JS_JSON {@link String}.
	 * 
	 * @return {@link String}
	 * @throws JSONException
	 */
	private String asHTML_JS_JSON() throws JSONException {
		StringBuilder sb = new StringBuilder();
		sb.append("<script type=\"text/javascript\">");
		sb.append("var validationResponse = ");
		sb.append(asJSON());
		sb.append("</script>");
		return sb.toString();
	}

	/**
	 * Class containing information about not valid field value.
	 * 
	 * @author Alexandr Bolbat
	 */
	private final class FieldData {

		/**
		 * Error keys.
		 */
		private final List<String> errorsKeys;

		/**
		 * Constructor.
		 */
		public FieldData() {
			this.errorsKeys = new ArrayList<String>();
		}

		public List<String> getErrorsKeys() {
			return errorsKeys;
		}

		/**
		 * Add error key.
		 * 
		 * @param errorKey
		 *            - error key
		 */
		public void addErrorKey(String errorKey) {
			errorsKeys.add(errorKey);
		}

	}
	
	public static void main(String... args) {
		ValidationResponse vr = ValidationResponse.create();
		System.out.println(vr.toString());
		System.out.println(vr.toString(Format.HTML_JS_JSON));
		vr.addError("testField1", "error-key");
		vr.addError("testField2", "error-key-1");
		vr.addError("testField2", "error-key-2");
		System.out.println(vr.toString());
		System.out.println(vr.toString(Format.HTML_JS_JSON));
	}

}
