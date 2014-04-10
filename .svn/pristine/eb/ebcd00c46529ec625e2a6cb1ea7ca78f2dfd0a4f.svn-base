package com.wcs.base.ui;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.datatable.DataTableRenderer;
/**
 * 
 * <p>Project: tih</p>
 * <p>Description:重写datatalbe 渲染类 </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yidongjun@wcs-global.com">易东骏</a>
 * @see org.primefaces.component.datatable.DataTableRenderer
 */
public class BaseDataTableRenderer extends DataTableRenderer {

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        String reset =(String)component.getAttributes().get("reset");
        if(reset==null){
        	reset="true";
        }
        DataTable table = (DataTable)component;
        if(table.isPaginator()&&table.isLazy()&&reset.equals("true")){
            table.reset();
        }
        super.encodeEnd(context, table);
    }
    
}
