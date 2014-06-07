package com.wcs.tih.filenet.helper.pe;

public class P8BpmException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public P8BpmException() {
	}

	public P8BpmException(String msg) {
		super(msg);
	}

	public P8BpmException(String firstMsg, Object[] otherMsgs) {
		this(firstMsg, toStrings(otherMsgs));
	}

	public P8BpmException(String firstMsg, String[] otherMsgs) {
		super(constructMsg(firstMsg, otherMsgs));
	}

	public P8BpmException(Throwable cause) {
		super(cause);
	}

	public P8BpmException(String msg, Throwable cause) {
		super(msg, cause);
	}

	private static String constructMsg(String firstMsg, String[] otherMsgs) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(firstMsg + "\n");
		for (int i = 0; i < otherMsgs.length; ++i) {
			strBuf.append(otherMsgs[i] + "\n");
		}
		return strBuf.toString();
	}

	private static String[] toStrings(Object[] objs) {
		int length = objs.length;
		String[] strs = new String[length];
		for (int i = 0; i < length; ++i) {
			strs[i] = objs[i].toString();
		}
		return strs;
	}
}