package com.wcs.tih.interaction.controller.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.Smartmstr;
import com.wcs.tih.model.WfInstancemstr;

public class SmartImportVO extends IdModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	//W表中的ID,wp表中的标题和内容
	private Long firstId;
	private String firstHead;
	private String firstMore;
	private String workflowNumber;
	private Date createTime;
	private String isImport;		// 'Y','N'
	
	public SmartImportVO(Long id,Long firstId,String firstHead,String firstMore){
		setId(id);
		this.firstId=firstId;
		this.firstHead=firstHead;
		this.firstMore=firstMore;
	}
	
	public SmartImportVO(Long id,String firstHead,String firstMore,String workflowNumber,Date createTime, String isImport){
		setId(id);
		this.firstId=id;
		this.firstHead=firstHead;
		this.firstMore=firstMore;
		this.workflowNumber=workflowNumber;
		this.createTime=createTime;
		this.isImport = isImport;
	}
	
	//index页面dataTable所使用的参数以及构造方法
	private Smartmstr s;
	private WfInstancemstr w;
	
	public SmartImportVO(Smartmstr s,WfInstancemstr w){
		setId(s.getId());
		this.s=s;
		this.w=w;
	}
	public SmartImportVO(Smartmstr s){
		setId(s.getId());
		this.s=s;
		this.w=null;
	}
	
	//精灵自动导入对话框的dialog右侧第一个accordionPanl中的DataTable所使用的List的集合类型和参数
	//RF就是right first.右边第一个.同时,更新,添加页面也使用这个VO.
	private String fileIdRF;
	private String fileNameRF;
	
	public SmartImportVO(Long id,String fileIdRF,String fileNameRF){
		setId(id);
		this.fileIdRF=fileIdRF;
		this.fileNameRF=fileNameRF;
	}
	
	//queryFlow使用的VO
	private String tetleName;
	private String txtInput;
	private List<SmartImportVO> fileList;
	private Date completeDate;
	private String completeUser;
	private String completeMethod;
	public SmartImportVO(Long id,String tetleName,String txtInput,List<SmartImportVO> fileList,Date completeDate,String completeUser,String completeMethod){
		setId(id);
		this.tetleName=tetleName;
		this.txtInput=txtInput;
		this.fileList=fileList;
		this.completeDate=completeDate;
		this.completeUser=completeUser;
		this.completeMethod=completeMethod;
		
	}
	
	
	
	
	public SmartImportVO(){
		//默认构造方法
	}
	
	public Long getFirstId() {
		return firstId;
	}
	public void setFirstId(Long firstId) {
		this.firstId = firstId;
	}
	public String getFirstHead() {
		return firstHead;
	}
	public void setFirstHead(String firstHead) {
		this.firstHead = firstHead;
	}
	public String getFirstMore() {
		return firstMore;
	}
	public void setFirstMore(String firstMore) {
		this.firstMore = firstMore;
	}
	public String getFileIdRF() {
		return fileIdRF;
	}
	public void setFileIdRF(String fileIdRF) {
		this.fileIdRF = fileIdRF;
	}
	public String getFileNameRF() {
		return fileNameRF;
	}
	public void setFileNameRF(String fileNameRF) {
		this.fileNameRF = fileNameRF;
	}

	public String getTetleName() {
		return tetleName;
	}

	public void setTetleName(String tetleName) {
		this.tetleName = tetleName;
	}

	public String getTxtInput() {
		return txtInput;
	}

	public void setTxtInput(String txtInput) {
		this.txtInput = txtInput;
	}

	public List<SmartImportVO> getFileList() {
		return fileList;
	}

	public void setFileList(List<SmartImportVO> fileList) {
		this.fileList = fileList;
	}

	public String getWorkflowNumber() {
		return workflowNumber;
	}

	public void setWorkflowNumber(String workflowNumber) {
		this.workflowNumber = workflowNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getIsImport() {
		return isImport;
	}

	public void setIsImport(String isImport) {
		this.isImport = isImport;
	}

	public Smartmstr getS() {
		return s;
	}

	public void setS(Smartmstr s) {
		this.s = s;
	}

	public WfInstancemstr getW() {
		return w;
	}

	public void setW(WfInstancemstr w) {
		this.w = w;
	}

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public String getCompleteUser() {
        return completeUser;
    }

    public void setCompleteUser(String completeUser) {
        this.completeUser = completeUser;
    }

    public String getCompleteMethod() {
        return completeMethod;
    }

    public void setCompleteMethod(String completeMethod) {
        this.completeMethod = completeMethod;
    }
	
}
