package com.wcs.tih.interaction.controller.vo;

import java.io.Serializable;
import java.util.List;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.WfStepmstr;

public class ApplyQuestionVO  extends IdModel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String fileName;
	private String fileId;
	private WfStepmstr wfs;
	private String opionion;
	private List<ApplyQuestionVO> fileList;
	
	public ApplyQuestionVO(){
		
	}
	
	//普通附件list使用.
	public ApplyQuestionVO(Long id,String fileId,String fileName){
		setId(id);
		this.fileName=fileName;
		this.fileId=fileId;
	}
	
	//显示整个流程使用的VO,id,步骤信息,详细信息,dataTable使用的附件list
	public ApplyQuestionVO(Long id,WfStepmstr wfs,String opionion,List<ApplyQuestionVO> fileList){
	    setId(id);
	    this.wfs=wfs;
	    this.opionion=opionion;
	    this.fileList=fileList;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

    public WfStepmstr getWfs() {
        return wfs;
    }

    public void setWfs(WfStepmstr wfs) {
        this.wfs = wfs;
    }

    public String getOpionion() {
        return opionion;
    }

    public void setOpionion(String opionion) {
        this.opionion = opionion;
    }

    public List<ApplyQuestionVO> getFileList() {
        return fileList;
    }

    public void setFileList(List<ApplyQuestionVO> fileList) {
        this.fileList = fileList;
    }

	
}
