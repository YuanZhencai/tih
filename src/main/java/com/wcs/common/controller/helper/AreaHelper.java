package com.wcs.common.controller.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import com.wcs.common.consts.AreaConsts;


/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
public final class AreaHelper {

	private static Map<String,String> map = null;
	
	static{
		map = new HashMap<String,String>();
		//地域
		map.put(AreaConsts.TIH_AREA_CODE_1, AreaConsts.TIH_AREA_VALUE_1);
		map.put(AreaConsts.TIH_AREA_CODE_2, AreaConsts.TIH_AREA_VALUE_2);
		map.put(AreaConsts.TIH_AREA_CODE_3, AreaConsts.TIH_AREA_VALUE_3);
		map.put(AreaConsts.TIH_AREA_CODE_4, AreaConsts.TIH_AREA_VALUE_4);
		map.put(AreaConsts.TIH_AREA_CODE_5, AreaConsts.TIH_AREA_VALUE_5);
		map.put(AreaConsts.TIH_AREA_CODE_6, AreaConsts.TIH_AREA_VALUE_6);
		map.put(AreaConsts.TIH_AREA_CODE_7, AreaConsts.TIH_AREA_VALUE_7);
		map.put(AreaConsts.TIH_AREA_CODE_261, AreaConsts.TIH_AREA_VALUE_261);
		//华北
		map.put(AreaConsts.TIH_AREA_CODE_8, AreaConsts.TIH_AREA_VALUE_8);
		map.put(AreaConsts.TIH_AREA_CODE_9, AreaConsts.TIH_AREA_VALUE_9);
		map.put(AreaConsts.TIH_AREA_CODE_10, AreaConsts.TIH_AREA_VALUE_10);
		map.put(AreaConsts.TIH_AREA_CODE_11, AreaConsts.TIH_AREA_VALUE_11);
		map.put(AreaConsts.TIH_AREA_CODE_12, AreaConsts.TIH_AREA_VALUE_12);
		//东北
		map.put(AreaConsts.TIH_AREA_CODE_13, AreaConsts.TIH_AREA_VALUE_13);
		map.put(AreaConsts.TIH_AREA_CODE_14, AreaConsts.TIH_AREA_VALUE_14);
		map.put(AreaConsts.TIH_AREA_CODE_15, AreaConsts.TIH_AREA_VALUE_15);
		//华东
		map.put(AreaConsts.TIH_AREA_CODE_16, AreaConsts.TIH_AREA_VALUE_16);
		map.put(AreaConsts.TIH_AREA_CODE_17, AreaConsts.TIH_AREA_VALUE_17);
		map.put(AreaConsts.TIH_AREA_CODE_18, AreaConsts.TIH_AREA_VALUE_18);
		map.put(AreaConsts.TIH_AREA_CODE_19, AreaConsts.TIH_AREA_VALUE_19);
		map.put(AreaConsts.TIH_AREA_CODE_20, AreaConsts.TIH_AREA_VALUE_20);
		map.put(AreaConsts.TIH_AREA_CODE_21, AreaConsts.TIH_AREA_VALUE_21);
		map.put(AreaConsts.TIH_AREA_CODE_22, AreaConsts.TIH_AREA_VALUE_22);
		//中南
		map.put(AreaConsts.TIH_AREA_CODE_23, AreaConsts.TIH_AREA_VALUE_23);
		map.put(AreaConsts.TIH_AREA_CODE_24, AreaConsts.TIH_AREA_VALUE_24);
		map.put(AreaConsts.TIH_AREA_CODE_25, AreaConsts.TIH_AREA_VALUE_25);
		map.put(AreaConsts.TIH_AREA_CODE_26, AreaConsts.TIH_AREA_VALUE_26);
		map.put(AreaConsts.TIH_AREA_CODE_27, AreaConsts.TIH_AREA_VALUE_27);
		map.put(AreaConsts.TIH_AREA_CODE_28, AreaConsts.TIH_AREA_VALUE_28);
		//西南
		map.put(AreaConsts.TIH_AREA_CODE_29, AreaConsts.TIH_AREA_VALUE_29);
		map.put(AreaConsts.TIH_AREA_CODE_30, AreaConsts.TIH_AREA_VALUE_30);
		map.put(AreaConsts.TIH_AREA_CODE_31, AreaConsts.TIH_AREA_VALUE_31);
		map.put(AreaConsts.TIH_AREA_CODE_32, AreaConsts.TIH_AREA_VALUE_32);
		map.put(AreaConsts.TIH_AREA_CODE_33, AreaConsts.TIH_AREA_VALUE_33);
		//西北
		map.put(AreaConsts.TIH_AREA_CODE_34, AreaConsts.TIH_AREA_VALUE_34);
		map.put(AreaConsts.TIH_AREA_CODE_35, AreaConsts.TIH_AREA_VALUE_35);
		map.put(AreaConsts.TIH_AREA_CODE_36, AreaConsts.TIH_AREA_VALUE_36);
		map.put(AreaConsts.TIH_AREA_CODE_37, AreaConsts.TIH_AREA_VALUE_37);
		map.put(AreaConsts.TIH_AREA_CODE_38, AreaConsts.TIH_AREA_VALUE_38);
		//港澳台
		map.put(AreaConsts.TIH_AREA_CODE_39, AreaConsts.TIH_AREA_VALUE_39);
		map.put(AreaConsts.TIH_AREA_CODE_40, AreaConsts.TIH_AREA_VALUE_40);
		map.put(AreaConsts.TIH_AREA_CODE_41, AreaConsts.TIH_AREA_VALUE_41);
	}
	
	private AreaHelper(){

	}
	
    public static String getValueByKey(String key){
        return map.get(key);
    }
    
    public static List<SelectItem> getItemsByCatKey(String catKey){
        List<SelectItem> items = new ArrayList<SelectItem>();
        if(catKey!=null&&!"".equals(catKey)){
            for (String key : map.keySet()) {
                if(catKey.equals(key.substring(0,key.lastIndexOf("-")))){
                    items.add(new SelectItem(key, map.get(key)));
                }
            }
        }
        return items;
    }
    
}
