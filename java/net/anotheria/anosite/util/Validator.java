package net.anotheria.anosite.util;

import net.anotheria.maf.bean.FormBean;

/**
 * Custom validator used to customize and define own checks.
 * <p/>
 *
 * @author vitaliy
 * @version 1.0
 *          Date: Jan 16, 2010
 *          Time: 9:23:55 PM
 */
public interface Validator<T> {

	/**
	 * Check field value.
	 *
	 * @param field value to check
	 * @return true if valid
	 */
	public boolean validate(T field);
}
