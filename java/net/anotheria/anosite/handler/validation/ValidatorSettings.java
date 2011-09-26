package net.anotheria.anosite.handler.validation;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * Validator settings generic interface.
 * 
 * @author Alexandr Bolbat
 */
public interface ValidatorSettings extends Serializable {

	/**
	 * Get validator settings name.
	 * 
	 * @return {@link String}
	 */
	String getName();

	/**
	 * Get validator settings as {@link JSONObject}.
	 * 
	 * @return
	 */
	JSONObject toJSON();

}
