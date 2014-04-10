package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import java.util.List;

public class MyInboxWorklistService {
	private static final String inboxName = "Inbox(0)";

	public static List<VWStepElement> getStepElements(VWSession session, int userid, String stepName) throws VWException {
		String filter = "F_BoundUser = :Userid AND F_StepName = :stepName";
		Object[] substitutionVars = { new Integer(userid), stepName };
		int queryFlags = 3;
		return QueueHelper.getStepElementsByfilter(session, inboxName, filter, substitutionVars, queryFlags);
	}

	public static List<VWStepElement> getStepElements(VWSession session, int userid) throws VWException {
		String filter = "F_BoundUser = :Userid";
		Object[] substitutionVars = { new Integer(userid) };
		int queryFlags = 3;
		return QueueHelper.getStepElementsByfilter(session, inboxName, filter, substitutionVars, queryFlags);
	}
}