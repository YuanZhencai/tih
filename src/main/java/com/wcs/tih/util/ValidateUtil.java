package com.wcs.tih.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ValidateUtil {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	private static Logger logger = LoggerFactory.getLogger(ValidateUtil.class);

	private ValidateUtil() {
	}

	/**
	 * <p>
	 * Description: 验证非空
	 * </p>
	 * 
	 * @param obj
	 * @param label
	 * @return
	 */
	public static boolean validateRequired(FacesContext context, Object obj, String label) {
		FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, label, "");
		if (obj == null) {
			error.setDetail("不允许为空。");
			context.addMessage(null, error);
			return false;
		}
		String value = String.valueOf(obj).trim();
		if ("".equals(value)) {
			error.setDetail("不允许为空。");
			context.addMessage(null, error);
			return false;
		}
		return true;
	}

	public static boolean validateRequiredINVS(Object obj) {
		if (obj == null) {
			return true;
		}
		String value = String.valueOf(obj).trim();
		if ("".equals(value)) {
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Description: 验证最小长度
	 * </p>
	 * 
	 * @param obj
	 * @param label
	 * @param length
	 * @return
	 */
	public static boolean validateMinlength(FacesContext context, Object obj, String label, int length) {
		if (obj == null) {
			return length == 0;
		}
		String value = String.valueOf(obj).trim();
		if (value.length() < length) {
			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, label, "");
			error.setDetail("不允许少于" + length + "个字符");
			context.addMessage(null, error);
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Description: 验证最大长度
	 * </p>
	 * 
	 * @param context
	 * @param obj
	 * @param label
	 * @param length
	 * @return
	 */
	public static boolean validateMaxlength(FacesContext context, Object obj, String label, int length) {
		if (obj == null) {
			return true;
		}
		String value = String.valueOf(obj).trim();
		if (value.length() > length) {
			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, label, "");
			error.setDetail("不允许大于" + length + "个字符");
			context.addMessage(null, error);
			return false;
		}
		return true;
	}

	public static boolean validateNumericMorethan(FacesContext context, Object obj, String label, double num) {
		if (obj == null) {
			return true;
		}
		boolean b = Double.valueOf(String.valueOf(obj)) > num;
		if (!b) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, label, "不允许小于" + num));
		}
		return b;
	}

	public static boolean validateNumericMorethanINVS(Object obj, double num) {
		return Double.valueOf(String.valueOf(obj)) > num;
	}

	public static boolean validateNumericLessthan(FacesContext context, Object obj, String label, double num) {
		if (obj == null) {
			return true;
		}
		boolean b = Double.valueOf(String.valueOf(obj)) < num;
		if (!b) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, label, "不允许大于" + num));
		}
		return b;
	}

	public static boolean validateRepeat(FacesContext context, Object obj, String label, double num) {
		if (obj == null) {
			return true;
		}
		boolean b = Double.valueOf(String.valueOf(obj)) < num;
		if (!b) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, label, "不允许重复"));
		}
		return b;
	}

	public static boolean validateRepeatWf(FacesContext context, Object obj, String label, double num) {
		boolean b = Double.valueOf(String.valueOf(obj)) == num;
		if (b) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, label, "不允许为空"));
		}
		return !b;
	}

	public static boolean validateStartTimeAndEndTime(FacesContext context, Date startTime, Date endTime) {
		if (endTime == null) {
			return true;
		}

		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = sdf.parse(sdf.format(startTime));
			dateEnd = sdf.parse(sdf.format(endTime));
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		if (dateStart.getTime() >= dateEnd.getTime()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "结束时间：", "结束时间必须晚于开始时间"));
			return false;
		}
		return true;
	}

	/**
	 * 
	 * <p>
	 * Description: endTime时间和startTime时间可以大于等于
	 * </p>
	 * 
	 * @param context
	 * @param startTime
	 * @param endTime
	 * @param label1
	 * @param label2
	 * @return
	 */
	public static boolean validateStartTimeAndEndTime(FacesContext context, Date startTime, Date endTime, String label1, String label2) {
		if (startTime == null) {
			return true;
		}
		if (endTime == null) {
			return true;
		}
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = sdf.parse(sdf.format(startTime));
			dateEnd = sdf.parse(sdf.format(endTime));
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		if (dateStart.getTime() > dateEnd.getTime()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, label1, label2));
			return false;
		}
		return true;
	}

	/**
	 * 
	 * <p>
	 * Description: endTime时间必须大于startTime时间
	 * </p>
	 * 
	 * @param context
	 * @param startTime
	 * @param endTime
	 * @param label1
	 * @param label2
	 * @return
	 */
	public static boolean validateStartTimeGTEndTime(FacesContext context, Date startTime, Date endTime, String label1, String label2) {
		if (startTime == null) {
			return true;
		}
		if (endTime == null) {
			return true;
		}
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = sdf.parse(sdf.format(startTime));
			dateEnd = sdf.parse(sdf.format(endTime));
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		if (dateStart.getTime() >= dateEnd.getTime()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, label1, label2));
			return false;
		}
		return true;
	}

	public static boolean validateNewTimeAndEndTime(FacesContext context, Date startTime, Date endTime, String label1, String label2) {
		if (endTime == null) {
			return true;
		}
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = sdf.parse(sdf.format(startTime));
			dateEnd = sdf.parse(sdf.format(endTime));
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		if (dateStart.getTime() > dateEnd.getTime()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, label1, label2));
			return false;
		}
		return true;
	}

	public static boolean validateNewTimeAndEndTime(FacesContext context, Date startTime, Date endTime) {
		if (endTime == null) {
			return true;
		}
		Date dateStart = null;
		Date dateEnd = null;
		try {
			dateStart = sdf.parse(sdf.format(startTime));
			dateEnd = sdf.parse(sdf.format(endTime));
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		if (dateStart.getTime() >= dateEnd.getTime()) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "结束时间：", "结束时间必须晚于当前时间"));
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Description: 验证正则表达式
	 * </p>
	 * 
	 * @param context
	 * @param obj
	 * @param label
	 * @param regex
	 * @param errMsg
	 * @return
	 */
	public static boolean validateRegex(FacesContext context, Object obj, String label, String regex, String errMsg) {
		if (obj == null) {
			return true;
		}
		String value = String.valueOf(obj).trim();
		if ("".equals(value)) {
			return true;
		}
		if (!value.matches(regex)) {
			FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, label, errMsg);
			context.addMessage(null, error);
			return false;
		}
		return true;
	}

	public static boolean validateRequiredAndMax(FacesContext context, Object obj, String label, int max) {
		if (!validateRequired(context, obj, label)) {
			return false;
		}
		return validateMaxlength(context, obj, label, max);
	}

	public static boolean validateMinAndMax(FacesContext context, Object obj, String label, int min, int max) {
		if (!validateMinlength(context, obj, label, min)) {
			return false;
		}
		return validateMaxlength(context, obj, label, max);
	}

	public static boolean validateRequiredAndRegex(FacesContext context, Object obj, String label, String regex, String errMsg) {
		if (!validateRequired(context, obj, label)) {
			return false;
		}
		return validateRegex(context, obj, label, regex, errMsg);
	}

	public static boolean validateTwoDate(FacesContext context, Object begin, Object end, String msg) {
		if (begin != null && end != null && ((Date) begin).after(((Date) end))) {
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, ""));
			return false;
		}
		return true;
	}

}
