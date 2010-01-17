package net.anotheria.anosite.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark fields and parameters then require presence validation.
 * <p/>
 *
 * @author vitaliy
 * @version 1.0
 *          Date: Jan 16, 2010
 *          Time: 4:17:58 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER })
public @interface ValidateNotEmpty {

	/**
	 * Message custom key.
	 *
	 * @return message key.
	 */
	String key();

	/**
	 * Optional custom error message used if message by key isn't available.
	 *
	 * @return message string
	 */
	String message() default "";
}
