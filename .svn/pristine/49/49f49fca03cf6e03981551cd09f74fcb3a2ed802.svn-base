package com.wcs.tih.filenet.helper.ce.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	private static final String[] hex = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11",
			"12", "13", "14", "15", "16", "17", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", "20", "21", "22", "23", "24", "25", "26", "27", "28",
			"29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F",
			"40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56",
			"57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D",
			"6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84",
			"85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B",
			"9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", "AC", "AD", "AE", "AF", "B0", "B1", "B2",
			"B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9",
			"CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0",
			"E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7",
			"F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF" };

	private static final byte[] val = { 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
			63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 63, 63, 63, 63, 63, 63,
			63, 10, 11, 12, 13, 14, 15, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 10,
			11, 12, 13, 14, 15, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
			63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
			63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
			63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
			63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63 };

	public static String escape(String sourceStr) {
		StringBuffer sbuf = new StringBuffer();
		int len = sourceStr.length();
		for (int i = 0; i < len; ++i) {
			int ch = sourceStr.charAt(i);
			if ((65 <= ch) && (ch <= 90)) {
				sbuf.append((char) ch);
			} else if ((97 <= ch) && (ch <= 122)) {
				sbuf.append((char) ch);
			} else if ((48 <= ch) && (ch <= 57)) {
				sbuf.append((char) ch);
			} else if ((ch == 45) || (ch == 95) || (ch == 46) || (ch == 33) || (ch == 126) || (ch == 42) || (ch == 39) || (ch == 40) || (ch == 41)) {
				sbuf.append((char) ch);
			} else if (ch <= 127) {
				sbuf.append('%');
				sbuf.append(hex[ch]);
			} else if (ch == 32) {
				sbuf.append("%20");
			} else {
				sbuf.append('%');
				sbuf.append('u');
				sbuf.append(hex[(ch >>> 8)]);
				sbuf.append(hex[(0xFF & ch)]);
			}
		}
		return sbuf.toString();
	}

	public static String unescape(String sourceStr) {
		StringBuffer sbuf = new StringBuffer();
		int i = 0;
		int len = sourceStr.length();
		while (i < len) {
			int ch = sourceStr.charAt(i);
			if ((65 <= ch) && (ch <= 90)) {
				sbuf.append((char) ch);
			} else if ((97 <= ch) && (ch <= 122)) {
				sbuf.append((char) ch);
			} else if ((48 <= ch) && (ch <= 57)) {
				sbuf.append((char) ch);
			} else if ((ch == 45) || (ch == 95) || (ch == 46) || (ch == 33) || (ch == 126) || (ch == 42) || (ch == 39) || (ch == 40) || (ch == 41)) {
				sbuf.append((char) ch);
			} else if (ch == 37) {
				int cint = 0;
				if ('u' != sourceStr.charAt(i + 1)) {
					cint = cint << 4 | val[sourceStr.charAt(i + 1)];
					cint = cint << 4 | val[sourceStr.charAt(i + 2)];
					i += 2;
				} else {
					cint = cint << 4 | val[sourceStr.charAt(i + 2)];
					cint = cint << 4 | val[sourceStr.charAt(i + 3)];
					cint = cint << 4 | val[sourceStr.charAt(i + 4)];
					cint = cint << 4 | val[sourceStr.charAt(i + 5)];
					i += 5;
				}
				sbuf.append((char) cint);
			} else {
				sbuf.append((char) ch);
			}
			++i;
		}
		return sbuf.toString();
	}

	public static String XMLEscape(String src) {
		if (src == null)
			return null;
		String rtnVal = src.replaceAll("&", "&amp;");
		rtnVal = rtnVal.replaceAll("\"", "&quot;");
		rtnVal = rtnVal.replaceAll("<", "&lt;");
		rtnVal = rtnVal.replaceAll(">", "&gt;");
		return rtnVal;
	}

	public static String getParameter(String query, String param) {
		Pattern p = Pattern.compile("&" + param + "=([^&]*)");
		Matcher m = p.matcher("&" + query);
		if (m.find())
			return m.group(1);
		return null;
	}

	public static Map<String, String[]> getParameterMap(String query, String splitStr) {
		Map<String, String[]> rtnVal = new HashMap<String, String[]>();
		if (isNull(query))
			return rtnVal;
		String[] parameters = query.split("\\s*" + splitStr + "\\s*");
		for (int i = 0; i < parameters.length; ++i) {
			int j = parameters[i].indexOf(61);
			if (j > -1)
				rtnVal.put(parameters[i].substring(0, j), new String[] { parameters[i].substring(j + 1) });
		}
		return rtnVal;
	}

	public static String setQueryParameter(String query, String param, String value) {
		String rtnVal = null;
		try {
			String m_query = "&" + query;
			String m_param = "&" + param + "=";
			String m_value = URLEncoder.encode(value, "UTF-8");
			Pattern p = Pattern.compile(m_param + "[^&]*");
			Matcher m = p.matcher(m_query);
			if (m.find())
				rtnVal = m.replaceFirst(m_param + m_value);
			else
				rtnVal = m_query + m_param + m_value;
			rtnVal = rtnVal.substring(1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return rtnVal;
	}

	public static String replace(String srcText, String fromStr, String toStr) {
		if (srcText == null)
			return null;
		StringBuffer rtnVal = new StringBuffer();
		String rightText = srcText;
		for (int i = rightText.indexOf(fromStr); i > -1; i = rightText.indexOf(fromStr)) {
			rtnVal.append(rightText.substring(0, i));
			rtnVal.append(toStr);
			rightText = rightText.substring(i + fromStr.length());
		}
		rtnVal.append(rightText);
		return rtnVal.toString();
	}

	public static String linkString(String leftStr, String linkStr, String rightStr) {
		if (isNull(leftStr))
			return rightStr;
		if (isNull(rightStr))
			return leftStr;
		return leftStr + linkStr + rightStr;
	}

	public static boolean isNull(String str) {
		return ((str == null) || (str.trim().length() == 0));
	}

	public static boolean isNotNull(String str) {
		return (!(isNull(str)));
	}

	public static String getString(String s) {
		return ((s.equals("null")) ? "" : (s == null) ? "" : s);
	}

	public static List<String> convertToList(String source, String regex) {
		List<String> dest = new ArrayList<String>();
		if (isNull(source)) {
			return dest;
		}
		String[] result = source.split(regex);
		for (int i = 0; i < result.length; ++i) {
			dest.add(result[i]);
		}
		return dest;
	}

	public static List<String> convertToList(String source) {
		return convertToList(source, ";");
	}

	public static String convertToString(List<String> source, String separator) {
		if (source == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < source.size(); ++i) {
			if (isNotNull(sb.toString())) {
				sb.append(separator);
			}
			sb.append(source.get(i));
		}
		return sb.toString();
	}

	public static String convertToString(List<String> source) {
		return convertToString(source, ";");
	}
}