package com.wcs.tih.system.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.tih.system.controller.vo.BranchVo;
import com.wcs.tih.system.service.BranchService;
import com.wcs.tih.util.ValidateUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: 分支机构Bean</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "branchBean")
@ViewScoped
public class BranchBean {
    @EJB
    private BranchService branchService;
    private BranchVo branchVo;
    private LazyDataModel<BranchVo> lazyBranchVoModel;
    private String organizationName;
    private String exsitsOrganizationName;
    private String effective;
    private String excuteMethod;
    private long companymstrId;

    /**
     * <p>Description: 初始化</p>
     */
    @PostConstruct
    public void init() {
        branchVo = new BranchVo();
    }

    /**
     * <p>Description: 初始化</p>
     */
    public void initBranch() {
        lazyBranchVoModel = null;
        organizationName = "";
        effective = "N";
        this.queryBranch();
    }

    /**
     * <p>Description: 查询分支机构</p>
     */
    public void queryBranch() {
        List<BranchVo> branchVos = branchService.queryBranch(this.companymstrId, organizationName, effective);
        if (null != branchVos && branchVos.size() != 0) {
            lazyBranchVoModel = new PageModel<BranchVo>(branchVos, false);
        } else {
            lazyBranchVoModel = null;
        }
    }

    /**
     * <p>Description: 增加分支机构</p>
     */
    public void addBranch() {
        branchVo = new BranchVo();
        branchVo.setEffective("N");
    }

    /**
     * <p>Description: 保存分支机构</p>
     */
    public void saveBranch() {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext.getCurrentInstance().addCallbackParam("issucc", "no");
        if (!(ValidateUtil.validateRequiredAndMax(context, branchVo.getOrganizationName(), "机构名称：", 100) & ValidateUtil.validateRequired(context, branchVo.getSetUpDatetime(), "成立时间：") & ValidateUtil.validateRequiredAndMax(context, branchVo.getLocation(), "经营地点：", 500) & ValidateUtil.validateRequiredAndMax(context, branchVo.getBusinessScope(), "经营范围：", 500) & ValidateUtil.validateMaxlength(context, branchVo.getRemakrs(), "备注：", 500))){
        	return;
        }
        if (branchService.isExistBranch(companymstrId, branchVo)) {
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "机构名称重复", ""));
        	return;
        }
        if ("add".equals(excuteMethod)) {
            try {
                branchService.saveBranch(companymstrId, branchVo);
                RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "保存机构名称：" + branchVo.getOrganizationName() + "的分支机构信息成功,请查看并确认"));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
            }
        } else {
            try {
                branchService.updateBranch(branchVo);
                RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "编辑机构名称：" + branchVo.getOrganizationName() + "的分支机构信息成功,请查看并确认"));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
            }
        }
        queryBranch();
    }

    public BranchVo getBranchVo() {
        return branchVo;
    }

    public void setBranchVo(BranchVo branchVo) {
        this.branchVo = branchVo;
    }

    public LazyDataModel<BranchVo> getLazyBranchVoModel() {
        return lazyBranchVoModel;
    }

    public void setLazyBranchVoModel(LazyDataModel<BranchVo> lazyBranchVoModel) {
        this.lazyBranchVoModel = lazyBranchVoModel;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public String getExcuteMethod() {
        return excuteMethod;
    }

    public void setExcuteMethod(String excuteMethod) {
        this.excuteMethod = excuteMethod;
    }

    public String getExsitsOrganizationName() {
        return exsitsOrganizationName;
    }

    public void setExsitsOrganizationName(String exsitsOrganizationName) {
        this.exsitsOrganizationName = exsitsOrganizationName;
    }

    public long getCompanymstrId() {
        return companymstrId;
    }

    public void setCompanymstrId(long companymstrId) {
        this.companymstrId = companymstrId;
    }

}
