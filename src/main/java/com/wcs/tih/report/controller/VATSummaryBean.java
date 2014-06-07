package com.wcs.tih.report.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.tih.filenet.ce.util.MimeException;
import com.wcs.tih.model.ReportSummaryHistory;
import com.wcs.tih.model.ReportVatIptDeductionDetail;
import com.wcs.tih.report.controller.vo.VATSummaryVO;
import com.wcs.tih.report.service.VATSummaryService;

@ManagedBean
@ViewScoped
public class VATSummaryBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@EJB 
	private VATSummaryService vatSummaryService;
	
	private Date statisticalTime;
	private String statisticalWay="month";
	private String companyName;
	private List<VATSummaryVO> vatShowData=new ArrayList<VATSummaryVO>();
	private LazyDataModel<VATSummaryVO> vatShowLazyModel;
	private VATSummaryVO[] vatSelectData;
	private List<ReportSummaryHistory> vatHistoryList=new ArrayList<ReportSummaryHistory>();
	private List<VATSummaryVO> showAttDlgList=new ArrayList<VATSummaryVO>();
	private List<Long> companys = new ArrayList<Long>();
    private List<CompanyManagerModel> companyItems = new ArrayList<CompanyManagerModel>();
    private int activeIndex;
	
    
    
    @PostConstruct
    public void init(){
        queryVATStatisticalInfo();
    }
	/**
	 * 主页上第一个datatable的查询,查询其公司名 ,公司ID
	 */
	public void queryData(){
		if(null==statisticalTime){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "统计日期:", "不允许为空!"));
			return;
		}
		vatShowData=vatSummaryService.queryData(statisticalTime,statisticalWay,companys);
	}
	
	public void resetQueryData(){
		this.statisticalTime=null;
		this.statisticalWay="month";
		companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
	}
	
	/**
	 * 汇总按钮
	 */
	public void queryExcelSummaryData(){
		Map<String,List<ReportVatIptDeductionDetail>> map=new HashMap<String,List<ReportVatIptDeductionDetail>>();
		if(vatSelectData.length<1){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "请选择所要汇总的公司信息!", ""));
			return;
		}
		
		map = vatSummaryService.prepareExcelData(vatSelectData,statisticalWay,statisticalTime);
		
		try {
			vatSummaryService.downVATExcel(map,statisticalTime,statisticalWay);
			activeIndex = 1;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			logger.error(e.getMessage(), e);
		}
		this.queryVATStatisticalInfo();
	}
	
	public void queryVATStatisticalInfo(){
		vatHistoryList=this.vatSummaryService.queryVATStatisticalInfo();
	}
	
	//dialog显示的数据.根据公司ID查询出所有的数据,这里要根据时间,累计/当月,来计算显示那些.
	//queryFileIdByVatId是根据vatID,所以这里要用公司ID先计算出符合上面条件的vatId.
	public void showAllData(long companyId) throws Exception{
		//先清空showAttDlgList
		this.showAttDlgList.clear();
		logger.info("companyId:"+companyId);
		Long[] vatIdArray=this.vatSummaryService.queryVatIdByCompanyId(companyId, statisticalWay, statisticalTime);
		//根据vatIdArray里的ID查询出所有的附件.
		for(Long vatId:vatIdArray){
			String fileId=this.vatSummaryService.queryFileIdByVatId(vatId);
			String fileName=this.vatSummaryService.queryFileNameByVatId(vatId);
			this.showAttDlgList.add(new VATSummaryVO(fileId, fileName));
		}
		logger.info("showAttDlgList:"+showAttDlgList.size());
	}
	
	/**
	 * @param vatId
	 * @return
	 */
	public StreamedContent downloadFileWithVATId(long vatId){
		String fileId="";
		try {
			fileId=this.vatSummaryService.queryFileIdByVatId(vatId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), ""));
			return null;
		}
		return this.downloadFile(fileId);
	}
	
	
	public StreamedContent downloadFile(String fileId){
		try {
			return vatSummaryService.downloadFile(fileId);
		} catch (MimeException e) {
			logger.error(e.getMessage(), e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "文档类型不正确。", ""));
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "下载出现异常，请联系系统管理员。", ""));
			return null;
		}
	}

    public void selectCompanys(CompanyManagerModel[] com) {
        companys = new ArrayList<Long>();
        companyItems = new ArrayList<CompanyManagerModel>();
        for (CompanyManagerModel vo : com) {
            companys.add(vo.getId());
            companyItems.add(vo);
        }
    }
	
	public VATSummaryBean(){
	}
	
	public Date getStatisticalTime() {
		return statisticalTime;
	}
	public void setStatisticalTime(Date statisticalTime) {
		this.statisticalTime = statisticalTime;
	}
	public String getStatisticalWay() {
		return statisticalWay;
	}
	public void setStatisticalWay(String statisticalWay) {
		this.statisticalWay = statisticalWay;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public LazyDataModel<VATSummaryVO> getVatShowLazyModel() {
		return vatShowLazyModel;
	}
	public void setVatShowLazyModel(LazyDataModel<VATSummaryVO> vatShowLazyModel) {
		this.vatShowLazyModel = vatShowLazyModel;
	}

	public List<ReportSummaryHistory> getVatHistoryList() {
		return vatHistoryList;
	}

	public void setVatHistoryList(List<ReportSummaryHistory> vatHistoryList) {
		this.vatHistoryList = vatHistoryList;
	}

	public VATSummaryVO[] getVatSelectData() {
		return vatSelectData;
	}

	public void setVatSelectData(VATSummaryVO[] vatSelectData) {
		this.vatSelectData = vatSelectData;
	}
	
	public List<VATSummaryVO> getShowAttDlgList() {
		return showAttDlgList;
	}

    public List<Long> getCompanys() {
        return companys;
    }

    public void setCompanys(List<Long> companys) {
        this.companys = companys;
    }

    public List<CompanyManagerModel> getCompanyItems() {
        return companyItems;
    }

    public void setCompanyItems(List<CompanyManagerModel> companyItems) {
        this.companyItems = companyItems;
    }

    public List<VATSummaryVO> getVatShowData() {
        return vatShowData;
    }

    public void setVatShowData(List<VATSummaryVO> vatShowData) {
        this.vatShowData = vatShowData;
    }

    public int getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(int activeIndex) {
        this.activeIndex = activeIndex;
    }
    
}
