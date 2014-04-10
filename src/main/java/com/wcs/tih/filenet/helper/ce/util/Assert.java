package com.wcs.tih.filenet.helper.ce.util;

import java.util.Collection;

public final class Assert {
	public static void notNull(Object o, String message) {
		if (o == null)
			throw new NullPointerException(message);
	}

	public static void notEmpty(String s, String message) {
		if ((s == null) || ("".equals(s.trim())))
			throw new IllegalArgumentException(message);
	}

	public static void notEmpty(Collection<?> c, String message) {
		if ((c == null) || (c.isEmpty()))
			throw new IllegalArgumentException(message);
	}

	public static void isTrue(boolean condition, String message) {
		if (!(condition))
			throw new IllegalArgumentException(message);
	}

	public static void validState(boolean condition, String message) {
		if (!(condition))
			throw new IllegalStateException(message);
	}
}