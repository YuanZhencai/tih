package com.wcs.tih.interaction.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.openjpa.lib.conf.StringValue;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.exception.EngineRuntimeException;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.util.PageModel;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.ce.util.DownloadIdNotFoundException;
import com.wcs.tih.interaction.service.WizardAnswerService;
import com.wcs.tih.model.SmartAttachmentmstr;
import com.wcs.tih.model.Smartmstr;
/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
 */
@ManagedBean
@ViewScoped
public class WizardAnswerBean implements Serializable {
    private Logger logger = LoggerFactory.getLogger(getClass());
	@EJB
	private WizardAnswerService w;
	private Smartmstr st = new Smartmstr();
	private LazyDataModel<SmartAttachmentmstr> smartAttachmentmstrByQuestion;
	
	private LazyDataModel<SmartAttachmentmstr> smartAttachmentmstrByAnswer;
	
	private List smartAttachmentmstrByAnswerList;
	
	public List getSmartAttachmentmstrByAnswerList() {
        return smartAttachmentmstrByAnswerList;
    }


    public void setSmartAttachmentmstrByAnswerList(List smartAttachmentmstrByAnswerList) {
        this.smartAttachmentmstrByAnswerList = smartAttachmentmstrByAnswerList;
    }


    public List getSmartAttachmentmstrByQuestionList() {
        return smartAttachmentmstrByQuestionList;
    }


    public void setSmartAttachmentmstrByQuestionList(List smartAttachmentmstrByQuestionList) {
        this.smartAttachmentmstrByQuestionList = smartAttachmentmstrByQuestionList;
    }



    private List smartAttachmentmstrByQuestionList;
	
	public LazyDataModel<SmartAttachmentmstr> getSmartAttachmentmstrByQuestion() {
        return smartAttachmentmstrByQuestion;
    }


    public void setSmartAttachmentmstrByQuestion(LazyDataModel<SmartAttachmentmstr> smartAttachmentmstrByQuestion) {
        this.smartAttachmentmstrByQuestion = smartAttachmentmstrByQuestion;
    }


    public LazyDataModel<SmartAttachmentmstr> getSmartAttachmentmstrByAnswer() {
        return smartAttachmentmstrByAnswer;
    }


    public void setSmartAttachmentmstrByAnswer(LazyDataModel<SmartAttachmentmstr> smartAttachmentmstrByAnswer) {
        this.smartAttachmentmstrByAnswer = smartAttachmentmstrByAnswer;
    }



    @EJB
	private FileNetUploadDownload file;
	
	private LazyDataModel<Smartmstr> question;


	
	
	
	


	public LazyDataModel<Smartmstr> getQuestion() {
        return question;
    }


    public void setQuestion(LazyDataModel<Smartmstr> question) {
        this.question = question;
    }


 
	private Date beginTime;
	private Date endTime;
	
	private long id;

	public long getId() {
		return id;
	}
	
	
	public void setId(long id) {
		if(id!=0){
			this.w.saveOrUpdateCount(id);
		}
		this.id = id;
		smartAttachmentmstrByAnswerList= w.getAttachement(id,DictConsts.TIH_TAX_ATTACH_TYPE_5);
		smartAttachmentmstrByQuestionList=w.getAttachement(id,DictConsts.TIH_TAX_ATTACH_TYPE_4);
		this.smartAttachmentmstrByQuestion = new PageModel<SmartAttachmentmstr>(w.getAttachement(id,DictConsts.TIH_TAX_ATTACH_TYPE_4), false) ;
		this.smartAttachmentmstrByAnswer = new PageModel<SmartAttachmentmstr>(w.getAttachement(id,DictConsts.TIH_TAX_ATTACH_TYPE_5), false) ; 
	}

	public Smartmstr getSt() {
		return st;
	}

	public void setSt(Smartmstr st) {
		this.st = st;
	}
	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void search(){
	    if((this.getBeginTime()!=null &&!this.getBeginTime().equals("")&&(this.getEndTime()!=null&&!this.getEndTime().equals("")))){
	    if(this.getBeginTime().getTime()>this.getEndTime().getTime()){
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "开始时间不能大于结束时间！", ""));
	        return;
	    }
	    }
		this.question= new PageModel<Smartmstr>(w.getQuestion(st, beginTime, endTime), false);
	}
	
	
	public List<Smartmstr> getTop5(){
		return w.getQuestionsTops();
	}
	public Smartmstr getAnswer() {
		return this.w.getAnswer(id);
	}
	public StreamedContent getFile(String id) {  
        try {
			return this.file.downloadDocumentEncoding(id,"utf-8","iso8859-1");
		} catch (EngineRuntimeException e) {
			logger.error(e.getMessage(), e);
		} catch (DownloadIdNotFoundException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        return null;
    }    
	
}
