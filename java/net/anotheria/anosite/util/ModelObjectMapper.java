package net.anotheria.anosite.util;

import net.anotheria.anosite.handler.BoxHandler;
import net.anotheria.maf.util.FormObjectMapper;

import javax.servlet.http.HttpServletRequest;

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
 *          Time: 3:17:22 PM
 */
public class ModelObjectMapper {

	/**
	 * 
	 * @param req
	 * @param bean
	 */
	public static void map(HttpServletRequest req, Object bean) {
		FormObjectMapper.map(req, bean);
	}
}
