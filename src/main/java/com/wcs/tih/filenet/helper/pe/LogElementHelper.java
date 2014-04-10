package com.wcs.tih.filenet.helper.pe;

import org.apache.commons.lang.builder.ToStringBuilder;




import com.wcs.tih.filenet.helper.pe.util.ToStringUtil;
import com.wcs.tih.filenet.helper.vo.HistoryVo;

import filenet.vw.api.VWException;
import filenet.vw.api.VWLogElement;

public class LogElementHelper {
	public static String vwString(VWLogElement logElement) throws VWException {
		ToStringBuilder b = ToStringUtil.createBuilder(logElement);
		WorkItemHelper.appendFields(logElement, logElement.getFieldNames(), b);
		return b.toString();
	}

	public static HistoryVo getHistoryVo(VWLogElement logElement, String[] fieldNames) throws VWException {
		HistoryVo historyVo = new HistoryVo();
		historyVo.setStepName(logElement.getStepName());
		historyVo.setF_TimeStamp(logElement.getTimeStamp());
		historyVo.setQueueName(logElement.getQueueName());
		historyVo.setUserName(logElement.getUserName());
		historyVo.setWorkflowName(logElement.getWorkClassName());
		historyVo.setSubject(logElement.getSubject());
		historyVo.setF_WorkFlowNumber(logElement.getWorkFlowNumber());
		if (fieldNames != null) {
			for (String name : fieldNames) {
				historyVo.setFieldValue(name, logElement.getFieldValue(name));
			}
		}

		return historyVo;
	}
}