package com.wcs.tihm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RestfulUtil {
    private static Logger logger = LoggerFactory.getLogger(RestfulUtil.class);
    
    private static final String EMPTY="[]";
    
    private RestfulUtil() {
    }
    
	static public <T>T getRS(String uri,TypeReference<T> typeReference) {
		String resource = null;
		StringBuffer resources = new StringBuffer();
		URL u;
		try {
			u = new URL(uri);
			URLConnection urlc;
			urlc = u.openConnection();
			urlc.setDoOutput(false);
			urlc.setAllowUserInteraction(false);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					urlc.getInputStream(), "UTF8"));
			while ((resource = br.readLine()) != null) {
				resources.append(resource);
			}
			br.close();
		} catch (MalformedURLException me) {
			logger.error(me.getMessage(), me);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return jsonStrGenericObject(resources.toString(), typeReference);
	}
	
    @SuppressWarnings("unchecked")
    static private <T> T jsonStrGenericObject(String jsonString, TypeReference<T> typeReference) {
        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
            	ObjectMapper objectMapper = new ObjectMapper();
                return ((T) objectMapper.readValue(jsonString, typeReference));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }
    
    static public String objectToJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonGenerationException e) {
            logger.error(e.getMessage(), e);
            return EMPTY;
        } catch (JsonMappingException e) {
            logger.error(e.getMessage(), e);
            return EMPTY;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return EMPTY;
        }
    }
}
