package com.wcs.tih.feedback.controller.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wcs.tih.model.InvsInspectation;
import com.wcs.tih.model.InvsInspectationResult;

public class FeedbackInspectionVO implements Serializable {

	private static final long serialVersionUID = 1L;

	private InvsInspectation inspectation;
	private List<InvsInspectationResult> result;

	public InvsInspectation getInspectation() {
		if(inspectation == null){
			inspectation = new InvsInspectation();
		}
		return inspectation;
	}

	public void setInspectation(InvsInspectation inspectation) {
		this.inspectation = inspectation;
	}

	public List<InvsInspectationResult> getResult() {
		if(result == null){
			result = new ArrayList<InvsInspectationResult>();
		}
		return result;
	}

	public void setResult(List<InvsInspectationResult> result) {
		this.result = result;
	}

}
