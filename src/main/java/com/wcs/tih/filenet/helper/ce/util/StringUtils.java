package com.wcs.tih.filenet.helper.ce.util;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {
	public static String UTF8ToGBK(String s) {
		try {
			return new String(s.getBytes("UTF-8"), "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String charCn(String s) {
		if (s != null)
			try {
				return new String(s.trim().getBytes("ISO-8859-1"), "GBK");
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			}
		return "";
	}

	public static String charCn(Object o) {
		if (o != null)
			try {
				return new String(o.toString().trim().getBytes("GBK"), "ISO-8859-1");
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			}
		return "";
	}

	public static String charEn(String s) {
		if (s != null)
			try {
				return new String(s.trim().getBytes("GBK"), "ISO-8859-1");
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			}
		return "";
	}

	public static String charEn(Object o) {
		if (o != null)
			try {
				return new String(o.toString().trim().getBytes("GBK"), "ISO-8859-1");
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			}
		return "";
	}

	public static int stoi(String s) {
		int i = -1;
		try {
			i = Integer.parseInt(s);
		} catch (Exception e) {
			i = -1;
		}
		return i;
	}

	public static int stoi(Object o) {
		int i = -1;
		try {
			i = Integer.parseInt(o.toString());
		} catch (Exception e) {
			i = -1;
		}
		return i;
	}

	public static String trim(int i) {
		return String.valueOf(i);
	}

	public static String trim(String s) {
		if (s != null) {
			return s.trim();
		}
		return "";
	}

	public static String trim(Object o) {
		if (o != null) {
			return o.toString().trim();
		}
		return "";
	}

	public static String getDate(String datetime) {
		if (datetime != null) {
			if (datetime.length() > 10) {
				return datetime.substring(0, 10);
			}
			return datetime;
		}

		return "";
	}

	public static String getDate(Object datetime) {
		if (datetime != null) {
			if (datetime.toString().length() > 10) {
				return datetime.toString().substring(0, 10);
			}
			return datetime.toString();
		}

		return "";
	}

	public static String getTime(Object datetime) {
		if (datetime != null) {
			if (datetime.toString().length() > 19) {
				return datetime.toString().substring(0, 19);
			}
			return datetime.toString();
		}

		return "";
	}

	public static String getTime(String datetime) {
		if (datetime != null) {
			if (datetime.length() > 19) {
				return datetime.substring(0, 19);
			}
			return datetime;
		}

		return "";
	}

	public static String today() {
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	}

	public static String tonow() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
	}

	public static String charWn(String s) {
		if (s != null)
			try {
				return new String(s.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			}
		return "";
	}

	public static boolean isNullOrEmpty(Object object) {
		return ((object == null) || (object.toString().length() < 1));
	}

	public static String nullToString(Object object) {
		if (object == null) {
			return "";
		}
		return object.toString();
	}
}