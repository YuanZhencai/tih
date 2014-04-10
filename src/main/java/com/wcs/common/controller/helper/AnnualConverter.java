/** * AnnualConverter.java 
* Created on 2014年3月19日 上午10:41:28 
*/

package com.wcs.common.controller.helper;

import java.util.Date;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;



import com.wcs.common.controller.vo.AnnualVo;

/** 
 * <p>Project: tih</p> 
 * <p>Title: AnnualConverter.java</p> 
 * <p>Description: </p> 
 * <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 * 
 */
@FacesConverter(forClass = AnnualVo.class, value = "annualConverter")
public class AnnualConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if(value == null){
			return null;
		}
        if(component instanceof HtmlSelectOneMenu){
        	HtmlSelectOneMenu om = (HtmlSelectOneMenu)component;
            List<UIComponent> children = om.getChildren();
            for(UIComponent child : children){
                if(child instanceof UISelectItems){
                    UISelectItems items = (UISelectItems)child;
                    List<AnnualVo> annualVos = (List<AnnualVo>)items.getValue();
                    for (AnnualVo annualVo : annualVos) {
                    	if(annualVo.getItemLabel().equals(value)){
                    		return annualVo.getItemValue();
                    	}
					}
                }
            }
        }
        return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if(value == null){
			return null;
		}
		 if(value instanceof Date){
			 if(component instanceof HtmlSelectOneMenu){
				 HtmlSelectOneMenu om = (HtmlSelectOneMenu)component;
		            List<UIComponent> children = om.getChildren();
		            for(UIComponent child : children){
		                if(child instanceof UISelectItems){
		                    UISelectItems items = (UISelectItems)child;
		                    List<AnnualVo> annualVos = (List<AnnualVo>)items.getValue();
		                    for (AnnualVo annualVo : annualVos) {
		                    	if(annualVo.getItemValue().getYear() == ((Date)value).getYear()){
		                    		return annualVo.getItemLabel();
		                    	}
							}
		                }
		            }
		        }
         }
		return null;
	}

}
