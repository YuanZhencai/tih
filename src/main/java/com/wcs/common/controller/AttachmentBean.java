package com.wcs.common.controller;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.AttachmentVo;
import com.wcs.common.controller.vo.FilemstrVo;
import com.wcs.common.service.AttachmentService;
import com.wcs.tih.model.Filemstr;

@ManagedBean(name = "attachmentBean")
@ViewScoped
public class AttachmentBean extends AttachmentFolderBean{

	private static final long serialVersionUID = 1L;

	@EJB
	private AttachmentService attachmentService;
	
	private LazyDataModel<FilemstrVo> attachments;
	private FilemstrVo[] selectedAttachments;   // 当前操作的附件
	private String fileId;
	private String confirmType;//控制删除提示dialog的弹出：单个文档的删除/多个文档的删除
	private AttachmentVo attachmentVo = new AttachmentVo();//前台传递过来的参数
	
    /**
     * 
     * <p>Description: 上传附件</p>  
     * @param event
     */
    public void attachFileUpload(FileUploadEvent event) {
    	UploadedFile upFile = event.getFile();
        if(upFile == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件上传：", "附件不能为空"));
            return;
        }
        String fileName = upFile.getFileName();
        if(fileName.length() > 225){
        	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件名称不能超过225个字符!", ""));
        	return;
        }
        
        Filemstr filemstr = new Filemstr();
        filemstr.setTableName(attachmentVo.getTableName());
        filemstr.setEntityId(attachmentVo.getEntityId());
        
        filemstr.setName(fileName);
        try {
        	String fullFoldername = getSubFolder(attachmentVo.getTableName());
        	String fileid = attachmentService.uploadAttachments(upFile.getInputstream(), fileName, fullFoldername);
        	filemstr.setFnId(fileid);
        	attachmentService.saveFilemstr(filemstr);
		} catch (Exception e) {
			// 上传失败
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "附件上传：", e.getMessage()));
            return;
		}
        searchFilemstr();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("文档上传成功!", ""));
    }
    
    /**
     * 
     * <p>Description: 下载</p>  
     * @param fileId
     * @return
     */
    public StreamedContent getFileById(String fileId) {
        try {
            return attachmentService.downloadAttachment(fileId);
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载附件：", "下载附件失败！"));
            return null;
        }
    }
    
    /**
     * 
     * <p>Description: 删除附件</p>  
     * @param fileId
     */
    public void deleteDocument(){
    	try {
			attachmentService.deleteDocumentAndFilemstr(fileId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除附件：", "删除附件失败！"));
		}
    	searchFilemstr();
    }
    
    /**
     * 
     * <p>Description: 删除多个附件</p>
     */
    public void deleteDocuments(){
    	for(FilemstrVo f : selectedAttachments){
    		try {
				attachmentService.deleteDocumentAndFilemstr(f.getFnId());
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
	                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "删除附件：", "删除附件失败！"));
			}
    	}
    	searchFilemstr();
    }
    
    /**
     * 
     * <p>Description: 查询附件</p>
     */
    public void searchFilemstr(){
    	clear();
    	attachments = new PageModel<FilemstrVo>(attachmentService.searchFilemstr(attachmentVo.getTableName(), attachmentVo.getEntityId()), false);
    }

	private void clear() {
		selectedAttachments = null;
    	attachments = null;
	}

    //================= getter & setter ===================
	public LazyDataModel<FilemstrVo> getAttachments() {
		return attachments;
	}

	public void setAttachments(LazyDataModel<FilemstrVo> attachments) {
		this.attachments = attachments;
	}

	public FilemstrVo[] getSelectedAttachments() {
		return selectedAttachments;
	}

	public void setSelectedAttachments(FilemstrVo[] selectedAttachments) {
		this.selectedAttachments = selectedAttachments;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getConfirmType() {
		return confirmType;
	}

	public void setConfirmType(String confirmType) {
		this.confirmType = confirmType;
	}

	public AttachmentVo getAttachmentVo() {
		return attachmentVo;
	}

	public void setAttachmentVo(AttachmentVo attachmentVo) {
		this.attachmentVo = attachmentVo;
	}

}
