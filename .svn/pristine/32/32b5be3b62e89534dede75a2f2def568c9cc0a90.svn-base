package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWQueueElement;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.wcs.tih.filenet.helper.pe.util.ToStringUtil;

public class QueueElementHelper {
    public static String vwString(VWQueueElement qElem) throws VWException {
        ToStringBuilder b = ToStringUtil.createBuilder(qElem);

        b.append("=== System Defined Fields ===");
        String[] sysFieldNames = qElem.getSystemDefinedFieldNames();
        WorkItemHelper.appendFields(qElem, sysFieldNames, b);
        ToStringUtil.appendSectionEndLine(b);

        b.append("=== User Defined Fields ===");
        String[] userFieldNames = qElem.getUserDefinedFieldNames();
        WorkItemHelper.appendFields(qElem, userFieldNames, b);
        ToStringUtil.appendSectionEndLine(b);

        WorkItemHelper.append(qElem, b);
        b.append("CurrentQueueName", qElem.getQueueName());

        int ivalue = qElem.getLockedStatus();
        String svalue = null;
        switch (ivalue) {
            case 1:
                svalue = "Locked by user";
                break;
            case 2:
                svalue = "Locked by System";
                break;
            case 0:
                svalue = "Locked by No one";
                break;
            default:
                throw new P8BpmException();
        }
        b.append("CurrentLockStatus", svalue);
        b.append("CurrentInstructionSheetName", qElem.getLockedMachine());
        return b.toString();
    }
}