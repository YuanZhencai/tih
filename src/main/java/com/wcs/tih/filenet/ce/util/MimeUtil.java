package com.wcs.tih.filenet.ce.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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
		private static String configFileName="mimetypes";
		static {
			
			hs = new HashMap<String,String>();
			ResourceBundle mimeType = ResourceBundle.getBundle(configFileName);
			Enumeration<String> enumeration = mimeType.getKeys();
	        while (enumeration.hasMoreElements()) {
	            String key = enumeration.nextElement();
	            String value = mimeType.getString(key);
	            hs.put(key, value);
	        }
		}
	}

	public static String getMimeType(String type)  {
		String mimetype=PropertyKeys.hs.get(type);
		if(mimetype==null){
			return "application/octet-stream";
		}else{
			return mimetype ;
		}
	}
	

	public static String removeStrValue(String str){
		if(str.lastIndexOf('.')==-1){
			return str;
		}
		return str.substring(0, str.lastIndexOf('.'));
	}
	public static void main(String[] args) {
	}
}
