package com.wcs.tih.filenet.helper.ce.util;

import com.filenet.api.collection.EngineCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.apache.commons.collections.CollectionUtils;

public class EngineCollectionUtils {
	public static <T> Collection<T> c(EngineCollection ec, Class<T> cls) {
		Iterator<?> it = ec.iterator();
		Collection<T> c = new ArrayList<T>();
		CollectionUtils.addAll(c, it);
		return c;
	}
}