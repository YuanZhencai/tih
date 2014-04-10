package com.wcs.tih.util;

import java.text.DecimalFormat;

/**
 * <p>Project: tih</p>
 * <p>Description: 数字处理</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public final class DataUtil {

    private DataUtil() {
    }
    /**
     * <p>Description: 数字千分位格式化</p>
     * @param number 数字
     * @param decimal 小数位数
     * @return
     */
    public static String dataFormat(double number, int decimal) {
        String pattern = "#,###,###,##0.";
        if (decimal != 0) {
            for (int i = 0; i < decimal; i++) {
                pattern += "0";
            }
        } else {
            pattern = "###,##0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(number);
    }
}
