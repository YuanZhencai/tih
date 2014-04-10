package com.wcs.tih.util;

import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.primefaces.component.selectonemenu.SelectOneMenu;

import com.wcs.tih.feedback.controller.vo.DictPictureVO;

@FacesConverter(forClass = DictPictureVO.class, value = "playerConverter")
public class DictPictureConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {
		DictPictureVO player = null;
        if(component instanceof SelectOneMenu){
            SelectOneMenu om = (SelectOneMenu)component;
            List<UIComponent> children = om.getChildren();
            for(UIComponent child : children){
                if(child instanceof UISelectItems){
                    UISelectItems items = (UISelectItems)child;
                    Object value = items.getValue();
                    List<DictPictureVO> players = (List<DictPictureVO>)value;    
                    for(DictPictureVO p:players){
                        if((p.getCode()).equals(submittedValue) ){
                            player = p;
                            break;
                        }
                    }
                }
            }
        }
        return player;
    }

	@Override
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
        String str = null;
        if(value != null){
            if(value instanceof DictPictureVO){
            	DictPictureVO p = (DictPictureVO)value;
                if(p.getCode() != null){
                    str = p.getCode().toString();
                }
            }
        }
        return str;
	}

}