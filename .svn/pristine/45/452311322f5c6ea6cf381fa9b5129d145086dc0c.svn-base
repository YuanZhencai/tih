package com.wcs.tih.feedback.controller.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.wcs.tih.model.InvsAntiAvoidance;
import com.wcs.tih.model.InvsAntiResult;

public class FeedbackAntiVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private InvsAntiAvoidance antiAvoidance;
	private List<InvsAntiResult> antiResults;

	public InvsAntiAvoidance getAntiAvoidance() {
		if(antiAvoidance == null){
			antiAvoidance = new InvsAntiAvoidance();
		}
		return antiAvoidance;
	}

	public void setAntiAvoidance(InvsAntiAvoidance antiAvoidance) {
		this.antiAvoidance = antiAvoidance;
	}

	public List<InvsAntiResult> getAntiResults() {
		if(antiResults == null){
			antiResults = new ArrayList<InvsAntiResult>();
		}
		return antiResults;
	}

	public void setAntiResults(List<InvsAntiResult> antiResults) {
		this.antiResults = antiResults;
	}

}
