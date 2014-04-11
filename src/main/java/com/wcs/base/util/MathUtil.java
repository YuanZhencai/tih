package com.wcs.base.util;

import java.math.BigDecimal;

public class MathUtil {

	
	/**
	 * Double四舍五入
	 * @param d
	 * @param scale
	 * @return
	 */
	public static Double roundHalfUp(Double d,int scale){
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(scale, BigDecimal.ROUND_HALF_UP);
		d = bd.doubleValue();
		return d;
	}
	
	/**
	 * 
	 * <p>Description: 验证字符串是否为空或者为 null</p>
	 * @param string 字符串
	 * @return true Or false
	 */
	public static Boolean isEmptyOrNull(String string){
		if(string == null || string.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * <p>Description: 验证数字是否小于等于零或者为 null</p>
	 * @param number 数字
	 * @return true Or false
	 */
	public static Boolean isZeroOrNull(Double number){
		if(number == null || number <= 0d){
			return true;
		}else{
			return false;
		}
	}
	
    /**
     * 提供精确的小数位四舍五入处理。 
     * @param value 需要四舍五入的数字    
     * @param scale 小数点后保留几位    
     * @return 四舍五入后的结果    
     */      
    public static String round(Double value,int scale)
    {
    	String content = null;
        if (value == null) {
             content = "";
         } else {
             if(value instanceof Double){
                 BigDecimal	bd = new  BigDecimal(Double.toString(value));
                 BigDecimal one = new BigDecimal("1");
                 BigDecimal	bd1 = bd.divide(one,scale,BigDecimal.ROUND_HALF_UP);
                 content = bd1.toString();
             }else{
                 content = String.valueOf(value);
             }
         }
        return content;
    }    
}
