package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWStepElement;
import filenet.vw.api.VWWorkObject;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.wcs.tih.filenet.helper.pe.util.ToStringUtil;

public class StepElementHelper {
    public static String vwString(VWStepElement st) throws VWException, BPMException {
        ToStringBuilder b = ToStringUtil.createBuilder(st);
        b.append("step name", st.getStepName());
        String[] paramNames = st.getParameterNames();
        b.append("=== Step Element Parameters ===");
        paramNames = (paramNames == null) ? new String[0] : paramNames;
        for (int i = 0; i < paramNames.length; ++i) {
            Object value = st.getParameterValue(paramNames[i]);
            b.append(paramNames[i], value);
        }
        ToStringUtil.appendSectionEndLine(b);
        WorkItemHelper.append(st, b);
        b.append("comment", st.getComment());
        return b.toString();
    }

    public static VWWorkObject lockWorkObject(VWStepElement st) throws VWException {
        return st.fetchWorkObject(true, true);
    }

    public static VWWorkObject fetchWorkObject(VWStepElement st) throws VWException {
        return st.fetchWorkObject(false, false);
    }

    public static void setParameterValue(VWStepElement st, String n, Object v) throws VWException {
        st.setParameterValue(n, v, true);
    }
}