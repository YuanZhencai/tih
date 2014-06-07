package com.wcs.tih.feedback.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;

public class AttachmentVO extends IdModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fileId;
    private String fileName;

    public AttachmentVO() {
        super();
    }

    public AttachmentVO(Long id, String fileId, String fileName) {
        this.setId(id);
        this.fileId = fileId;
        this.fileName = fileName;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
