package net.anotheria.anosite.util.annotations;

import net.anotheria.anosite.util.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Create and register custom validation implementation.
 * <p/>
 *
 * @author vitaliy
 * @version 1.0
 *          Date: Jan 16, 2010
 *          Time: 4:17:58 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER })
public @interface ValidateCustom {
	/**
	 *  Error message key.
	 *
	 * @return message key.
	 */
	String key();

	/**
	 * Optional custom message, used when message by key not available.
	 *
	 * @return message string.
	 */
	String message() default "";

	/**
	 * Custom validator class.
	 *
	 * @return class that implements {@link Validator}
	 */
	Class<? extends Validator<?>> validator();
}