package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWTransferResult;
import filenet.vw.api.VWValidationError;
import filenet.vw.api.VWWorkflowDefinition;
import java.io.InputStream;

public class WorkflowDefinitionHelper {
	public static void validate(VWSession vwSession, VWWorkflowDefinition wfDef) {
		VWValidationError[] validationErrors = (VWValidationError[]) null;
		try {
			validationErrors = wfDef.validate(vwSession, false);
		} catch (VWException vwe) {
			throw new P8BpmException(vwe);
		}
		if (validationErrors != null)
			throw new P8BpmValidationException("Validation errors:", validationErrors);
	}

	public static void transferWorkflow(VWSession vwSession, VWWorkflowDefinition wfDef) {
		VWTransferResult transferResult = null;
		try {
			transferResult = vwSession.transfer(wfDef, null, false, true);
		} catch (VWException vwe) {
			throw new P8BpmException(vwe);
		}
		String[] errorArray = transferResult.getErrors();
		if (errorArray != null)
			throw new P8BpmTransferException("Transfer operation errors:", errorArray);
	}

	public static void validateAndTransfer(VWSession vwSession, String pathname) {
		VWWorkflowDefinition wfDef = null;
		try {
			wfDef = VWWorkflowDefinition.readFromFile(pathname);
		} catch (VWException vwe) {
			throw new P8BpmException(vwe);
		}
		validateAndTransfer(vwSession, wfDef);
	}

	public static void validateAndTransfer(VWSession vwSession, InputStream is) {
		VWWorkflowDefinition wfDef = null;
		try {
			wfDef = VWWorkflowDefinition.read(is);
		} catch (VWException vwe) {
			throw new P8BpmException(vwe);
		}
		validateAndTransfer(vwSession, wfDef);
	}

	private static void validateAndTransfer(VWSession session, VWWorkflowDefinition wfDef) {
		validate(session, wfDef);
		transferWorkflow(session, wfDef);
	}
}