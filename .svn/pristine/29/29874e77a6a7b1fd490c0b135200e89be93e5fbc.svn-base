package com.wcs.common.upload.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title:  </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2012/1/18</p>
 * <p>Company: willmar </p>
 * @author:yidongjun
 * @version 1.0
 */
public final class MimeUtil {
    private MimeUtil() {
    }
	protected static final class PropertyKeys {
		private static Map<String,String> hs;
		static {
			hs = new HashMap<String,String>();
			hs.put("doc", "application/msword");
			hs.put("dot", "application/msword");
			hs.put("pdf", "application/pdf");
			hs.put("jpg", "image/jpg");
		}
	}

	public static String getMimeType(String type) {
		String mimetype=PropertyKeys.hs.get(type);
		if(mimetype==null){
			throw new RuntimeException("type not found : "+type);
		}else{
			return mimetype ;
		}
	}
	
	public static String getMimeTypeWidhValue(String value){
		String keyValue=null;
		for(String key:PropertyKeys.hs.keySet()){
			   if(PropertyKeys.hs.get(key).equals(value)){
				   keyValue= key;
			   }
		}
		if(keyValue==null){
			throw new RuntimeException("type not found : "+value);
		}
			  return keyValue;
	}
}
