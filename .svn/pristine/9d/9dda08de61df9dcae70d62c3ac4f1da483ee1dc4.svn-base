package com.wcs.tih.transaction.controller.helper;

import javax.faces.context.FacesContext;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public final class TaskRefreshHelper {

    private TaskRefreshHelper() {
    }
    /**
     * <p>Description: 刷新任务菜单和主页面数据</p>
     * @param context JSF对象,通过这个对象去找Bean实例
     * @throws Exception 刷新失败抛出异常
     */
    public static void refreshTask(FacesContext context) throws Exception {
        try {
            Object o = context.getApplication().getELResolver().getValue(context.getELContext(), null, "taskManagementBean");
            o.getClass().getDeclaredMethod("initTask").invoke(o);
        } catch (Exception e) {
            throw new Exception("刷新任务菜单和主页内容失败");
        }
    }
}
