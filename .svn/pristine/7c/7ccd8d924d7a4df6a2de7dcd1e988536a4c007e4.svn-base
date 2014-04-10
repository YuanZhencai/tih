package com.wcs.tih.feedback.controller.vo;

import java.io.Serializable;

import org.primefaces.model.LazyDataModel;

public class StepVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String desc;
    private String title;
    private LazyDataModel<AttachmentVO> lazyData;
    private Boolean flag;
    private AttachmentVO[] stepAttachments;

    public StepVO() {
    }

    public StepVO(String desc, String title, LazyDataModel<AttachmentVO> lazyData) {
        this.desc = desc;
        this.title = title;
        this.lazyData = lazyData;
    }

    public StepVO(String desc, String title, LazyDataModel<AttachmentVO> lazyData, Boolean flag) {
		this.desc = desc;
		this.title = title;
		this.lazyData = lazyData;
		this.flag = flag;
	}

	public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public LazyDataModel<AttachmentVO> getLazyData() {
        return lazyData;
    }

    public void setLazyData(LazyDataModel<AttachmentVO> lazyData) {
        this.lazyData = lazyData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public AttachmentVO[] getStepAttachments() {
		return stepAttachments;
	}

	public void setStepAttachments(AttachmentVO[] stepAttachments) {
		this.stepAttachments = stepAttachments;
	}

}
