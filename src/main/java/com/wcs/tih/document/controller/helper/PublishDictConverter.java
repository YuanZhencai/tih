package com.wcs.tih.document.controller.helper;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.model.Dict;

@FacesConverter(forClass = Dict.class, value = "publishDictConverter")
public class PublishDictConverter implements Converter {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {

        Dict dict = new Dict();
        String value = null;
        if (submittedValue != null && !"".equals(submittedValue.trim())) {
            try {
                String contentType = facesContext.getExternalContext().getRequestHeaderMap().get("Content-Type");
                if (contentType.contains("multipart/form-data")) {
                    value = new String(submittedValue.trim().getBytes("iso-8859-1"), "utf-8");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        } else {
            return null;
        }
        dict.setCodeKey(value);
        return dict;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        if (value == null || value.equals("")) {
            return "";
        } else {
            Dict d = (Dict) value;
            return d.getCodeVal() == null ? "" : (d.getCodeVal().trim() + "/" + d.getCodeCat() + "." + d.getCodeKey());
        }
    }

}
