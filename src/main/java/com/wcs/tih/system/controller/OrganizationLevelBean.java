package com.wcs.tih.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.UserCommonBean;
import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.tih.system.controller.vo.WfSupervisorVo;
import com.wcs.tih.system.service.OrganizationLevelInterface;
import com.wcs.tih.util.ValidateUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: 用户上级主管设置</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "organizationLevelBean")
@ViewScoped
public class OrganizationLevelBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @EJB
    private OrganizationLevelInterface organizationLevelService;
    @ManagedProperty(value = "#{userCommonBean}")
    private UserCommonBean userCommonBean;
    private String selectedAdAccount;
    private String supervisor;
    private String showSupervisor;
    private String requestFormType;
    private List<WfSupervisorVo> wfSupervisorVoList;
    private LazyDataModel<WfSupervisorVo> lazyWfSupervisorVoModel;
    private WfSupervisorVo wfSupervisorVo;
    // 公司查询
    private CompanyManagerModel company;
    private String companyName;

    /**
     * <p>Description: 初始化</p>
     */
    @PostConstruct
    public void init() {
        wfSupervisorVoList = new ArrayList<WfSupervisorVo>();
    }

    /**
     * <p>Description: 取得选择的上级主管</p>
     */
    public void getNeedSupervisor() {
        try {
            supervisor = userCommonBean.getSelectedUsermstrVo().getUsermstr().getAdAccount();
            showSupervisor = userCommonBean.getSelectedUsermstrVo().getP().getNachn();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            supervisor = "";
            showSupervisor = "";
        }
    }
    
    /**
     * <p>Description: 选择公司</p>
     */
    public void selectCompany(CompanyManagerModel company) {
        if(company == null) {
            companyName = "";
        } else {
            this.setCompany(company);
            companyName = company.getStext();
        }
    }

    /**
     * <p>Description: 加载用户的上级主管</p>
     * @param selectedAdAccount 选中的用户帐号
     */
    public void loadUserArganizationLevel(String selectedAdAccount) {
        this.selectedAdAccount = selectedAdAccount;
        requestFormType = "";
        supervisor = "";
        showSupervisor = "";
        companyName = "";
        wfSupervisorVoList = organizationLevelService.getAllWfSupervisorVo(selectedAdAccount);
        lazyWfSupervisorVoModel = new PageModel<WfSupervisorVo>(wfSupervisorVoList, false);
    }

    /**
     * <p>Description: 增加用户的上级主管</p>
     */
    public void addOrganizationLevel() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!(ValidateUtil.validateRequiredAndMax(context, showSupervisor, "上级主管：", 50) & ValidateUtil.validateRequired(context, requestFormType, "申请单类型：") & ValidateUtil.validateRequired(context, companyName, "所属公司："))){
        	return;
        }
        // 验证申请单类型只能有一个上级主管
        if (wfSupervisorVoList.size() != 0) {
            for (WfSupervisorVo wfsv : wfSupervisorVoList) {
                if (wfsv.getSupervisor().equals(supervisor) && wfsv.getRequestFormType().equals(requestFormType) && companyName.equals(wfsv.getCompanyName())) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败：", "不能重复添加！"));
                    return;
                }
                if (wfsv.getRequestFormType().equals(requestFormType) && companyName.equals(wfsv.getCompanyName())) {
                    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败：", "一家公司的一个业务流程只能分配一个上级主管！"));
                    return;
                }
            }
        }
        if (wfSupervisorVoList != null) {
            WfSupervisorVo wfsv = new WfSupervisorVo();
            wfsv.setId((long) (wfSupervisorVoList.size() + 1));
            wfsv.setAdAccount(this.selectedAdAccount);
            wfsv.setSupervisor(this.supervisor);
            wfsv.setSupervisorName(this.showSupervisor);
            wfsv.setRequestFormType(this.requestFormType);
            wfsv.setCompanymstrId(company.getId());
            wfsv.setCompanyName(company.getStext());
            wfSupervisorVoList.add(wfsv);
        }
        lazyWfSupervisorVoModel = new PageModel<WfSupervisorVo>(wfSupervisorVoList, false);
    }
    
    /**
     * <p>Description: 保存用户的上级主管</p>
     */
    public void saveOrganizationLevel() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean b = organizationLevelService.saveOrganizationLevel(this.selectedAdAccount,this.wfSupervisorVoList);
        if(b){
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "用户帐号：" + this.selectedAdAccount + "，分配组织层级成功，请查询并确认！"));
        }else{
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", "用户帐号：" + this.selectedAdAccount + "，分配组织层级失败！"));
        }
    }

    /**
     * <p>Description: 删除用户上级主管</p>
     */
    public void deleteOrganizationLevel() {
        lazyWfSupervisorVoModel = null;
        List<WfSupervisorVo> wfSupervisors = new ArrayList<WfSupervisorVo>();
        boolean b = false;
        for (WfSupervisorVo wfsv : wfSupervisorVoList) {
            b = false;
            if(wfsv.getCompanyName() == null && wfSupervisorVo.getCompanyName() ==null){
                if (wfsv.getSupervisor().equals(wfSupervisorVo.getSupervisor()) && wfsv.getRequestFormType().equals(wfSupervisorVo.getRequestFormType())) {
                    b = true;
                }
            }else if (wfsv.getCompanyName() != null && wfSupervisorVo.getCompanyName() != null && wfsv.getCompanyName().equals(wfSupervisorVo.getCompanyName())) {
                if (wfsv.getSupervisor().equals(wfSupervisorVo.getSupervisor()) && wfsv.getRequestFormType().equals(wfSupervisorVo.getRequestFormType())) {
                    b = true;
                }
            }
            if (!b) {
                wfSupervisors.add(wfsv);
            }
        }
        wfSupervisorVoList = wfSupervisors;
        lazyWfSupervisorVoModel = new PageModel<WfSupervisorVo>(wfSupervisors, false);
    }

    public String getSelectedAdAccount() {
        return selectedAdAccount;
    }

    public void setSelectedAdAccount(String selectedAdAccount) {
        this.selectedAdAccount = selectedAdAccount;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getRequestFormType() {
        return requestFormType;
    }

    public void setRequestFormType(String requestFormType) {
        this.requestFormType = requestFormType;
    }

    public List<WfSupervisorVo> getWfSupervisorVoList() {
        return wfSupervisorVoList;
    }

    public void setWfSupervisorVoList(List<WfSupervisorVo> wfSupervisorVoList) {
        this.wfSupervisorVoList = wfSupervisorVoList;
    }

    public LazyDataModel<WfSupervisorVo> getLazyWfSupervisorVoModel() {
        return lazyWfSupervisorVoModel;
    }

    public void setLazyWfSupervisorVoModel(LazyDataModel<WfSupervisorVo> lazyWfSupervisorVoModel) {
        this.lazyWfSupervisorVoModel = lazyWfSupervisorVoModel;
    }

    public WfSupervisorVo getWfSupervisorVo() {
        return wfSupervisorVo;
    }

    public void setWfSupervisorVo(WfSupervisorVo wfSupervisorVo) {
        this.wfSupervisorVo = wfSupervisorVo;
    }

    public String getShowSupervisor() {
        return showSupervisor;
    }

    public void setShowSupervisor(String showSupervisor) {
        this.showSupervisor = showSupervisor;
    }

    public UserCommonBean getUserCommonBean() {
        return userCommonBean;
    }

    public void setUserCommonBean(UserCommonBean userCommonBean) {
        this.userCommonBean = userCommonBean;
    }

    public CompanyManagerModel getCompany() {
        return company;
    }

    public void setCompany(CompanyManagerModel company) {
        this.company = company;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
