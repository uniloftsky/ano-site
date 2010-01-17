package net.anotheria.anosite.util;

import net.anotheria.anosite.api.common.APICallContext;
import net.anotheria.anosite.api.validation.ValidationError;
import net.anotheria.maf.util.FormObjectMapper;
import net.anotheria.maf.validation.Validator;
import net.anotheria.maf.validation.annotations.ValidateCustom;
import net.anotheria.maf.validation.annotations.ValidateNotEmpty;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * Guitar Model Object.
 * <p/>
 * <P>Various attributes of guitars, and related behaviour.
 * <p/>
 *
 * @author vitaliy
 * @version 1.0
 *          Date: Jan 16, 2010
 *          Time: 3:17:22 PM
 */
public final class ModelObjectMapper {

	/**
	 * Default constructor.
	 */
	private ModelObjectMapper() {
	}

	/**
	 * Mapper log.
	 */
	private static final Logger LOGGER = Logger.getLogger(FormObjectMapper.class);

	/**
	 * @param req  http request
	 * @param bean backing bean
	 */
	public static void map(final HttpServletRequest req, final Object bean) {
		FormObjectMapper.map(req, bean);
	}

	/**
	 * Validate mapped bean according defined preconditions.
	 *
	 * @param req  http request
	 * @param bean backing bean
	 */
	public static void validate(final HttpServletRequest req, final Object bean) {
		final Class beanClass = bean.getClass();
		final Field[] fields = beanClass.getDeclaredFields();
		for (Field field : fields) {
			try {
				final ValidateNotEmpty validateNotEmpty = field.getAnnotation(ValidateNotEmpty.class);
				if (validateNotEmpty != null) {
					field.setAccessible(true);
					Object value = field.get(bean);
					if (value == null
							|| String.valueOf(value).isEmpty()) {
						APICallContext.getCallContext().addValidationError(new ValidationError(field.getName(), validateNotEmpty.key(), validateNotEmpty.message()));
					}
				}
				final ValidateCustom validateCustom = field.getAnnotation(ValidateCustom.class);
				if (validateCustom != null) {
					field.setAccessible(true);
					Object value = field.get(bean);
					

					Validator validator = validateCustom.validator().newInstance();
					//noinspection unchecked
					if (!validator.validate(value)) {
						APICallContext.getCallContext().addValidationError(new ValidationError(field.getName(), validateCustom.key(), validateCustom.message()));
					}
				}
			} catch (IllegalAccessException e) {
				LOGGER.error(e);
			} catch (InstantiationException e) {
				LOGGER.error(e);
			}
		}
	}
}
