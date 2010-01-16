package net.anotheria.anosite.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Guitar Model Object.
 * <p/>
 * <P>Various attributes of guitars, and related behaviour.
 * <p/>
 * <P>Note that {@link BigDecimal} is used to model the price - not double or float.
 * See {@link #Guitar(String, BigDecimal, Integer)} for more information.
 *
 * @author vitaliy
 * @version 1.0
 *          Date: Jan 16, 2010
 *          Time: 4:17:58 PM
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface ValidateNotEmpty { 
	String key();
	String message() default "";	
}
