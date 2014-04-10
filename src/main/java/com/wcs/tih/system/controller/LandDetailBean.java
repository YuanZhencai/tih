package com.wcs.tih.system.controller;

import java.math.BigDecimal;
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
import com.wcs.tih.system.controller.vo.LandDetailVo;
import com.wcs.tih.system.service.LandDetailService;
import com.wcs.tih.util.ValidateUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: 土地明细Bean</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "landDetailBean")
@ViewScoped
public class LandDetailBean {
    @EJB
    private LandDetailService landDetailService;
    private LandDetailVo landDetailVo;
    private LazyDataModel<LandDetailVo> lazyLandDetailVoModel;
    private String landCertificateNo;
    private String landName;
    private String existLandName;
    private String effective;
    private String excuteMethod;
    private long companymstrId;

    /**
     * <p>Description: 初始化</p>
     */
    @PostConstruct
    public void init() {
        landDetailVo = new LandDetailVo();
    }

    /**
     * <p>Description: 初始化</p>
     */
    public void initLandDetail() {
        lazyLandDetailVoModel = null;
        landCertificateNo = "";
        existLandName = "";
        effective = "N";
        this.queryLandDetail();
    }

    /**
     * <p>Description: 查询土地明细信息</p>
     */
    public void queryLandDetail() {
        List<LandDetailVo> ldvs = landDetailService.queryLandDetail(this.companymstrId,landCertificateNo, landName, effective);
        if (ldvs.size() != 0) {
            lazyLandDetailVoModel = new PageModel<LandDetailVo>(ldvs, false);
        } else {
            lazyLandDetailVoModel = null;
        }
    }

    /**
     * <p>Description: 增加土地明细信息</p>
     */
    public void addLandDetail() {
        landDetailVo = new LandDetailVo();
        landDetailVo.setEffective("N");
    }

    /**
     * <p>Description: 更新土地明细的单位土地成本</p>
     */
    public void updateUnitLandCost() {
        BigDecimal landCost = landDetailVo.getLandCost();
        Double landArea = landDetailVo.getLandArea();
        Double unitLandCost;
        if (landCost != null && !"".equals(landCost) && landCost.doubleValue() != 0 && landArea != null && landArea != 0) {
            unitLandCost = landCost.doubleValue() * 10000 / landArea;
        } else {
            unitLandCost = 0.00;
        }
        landDetailVo.setUnitLandCost(new BigDecimal(unitLandCost));
    }

    /**
     * <p>Description: 保存土地明细信息</p>
     */
    public void saveLandDetail() {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext.getCurrentInstance().addCallbackParam("issucc", "no");
        boolean b = true;
        if (!(ValidateUtil.validateRequiredAndMax(context, landDetailVo.getLandCertificateNo(), "土地证编号：", 50) 
        		& ValidateUtil.validateRequiredAndMax(context, landDetailVo.getLandName(), "宗地名称：", 50) 
        		& ValidateUtil.validateRequired(context, landDetailVo.getLandArea(), "宗地面积：") 
        		& ValidateUtil.validateRequired(context, landDetailVo.getLandGetDatetime(), "土地取得日期：") 
        		& ValidateUtil.validateRequired(context, landDetailVo.getLandOverDatetime(), "土地终止日期：") 
        		& ValidateUtil.validateRequired(context, landDetailVo.getLandCost(), "土地账面原值：") 
        		& ValidateUtil.validateRequired(context, landDetailVo.getTaxAccroding(), "土地使用税征收标准：") 
        		& ValidateUtil.validateRequired(context, landDetailVo.getAnnualPay(), "年缴纳土地使用税额：") 
        		& ValidateUtil.validateRequiredAndMax(context, landDetailVo.getLandAddress(), "宗地详细地址：", 200))){
        	b = false;
        }
        if (null != landDetailVo.getLandArea() && landDetailVo.getLandArea().doubleValue() == 0) {
            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "宗地面积：", "");
            error.setDetail("不允许为空或0。");
            context.addMessage(null, error);
            b = false;
        }
        if (landDetailVo.getLandVolumeRate() != null && !ValidateUtil.validateRequiredAndRegex(context, landDetailVo.getLandVolumeRate(), "容积率：", "^-?[0-9]+([.][0-9]{1,2})?$", "填写格式只能为整数或保留2位小数")) {
        	b=false;
		}
        if(!ValidateUtil.validateRequiredAndRegex(context, landDetailVo.getLandCost(), "土地账面原值：", "^-?[0-9]+([.][0-9]{1,2})?$", "填写格式只能为整数或保留2位小数")){
        	b=false;
        }
        if (null == landDetailVo.getTaxAccroding() || "".equals(landDetailVo.getTaxAccroding().toString().trim())) {
            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "土地使用税征收标准：", "");
            error.setDetail("不允许为空。");
            context.addMessage(null, error);
            b = false;
        }else if (!ValidateUtil.validateRequiredAndRegex(context, landDetailVo.getLandCost(), "土地使用税征收标准：", "^-?[0-9]+(.[0-9]{0,2})?$", "填写格式只能为整数或保留2位小数")) {
        	b = false;
		}
        if (null == landDetailVo.getAnnualPay() || "".equals(landDetailVo.getAnnualPay().toString().trim())) {
            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "年缴纳土地使用税额金额：", "");
            error.setDetail("不允许为空。");
            context.addMessage(null, error);
            b = false;
        }else if (!ValidateUtil.validateRequiredAndRegex(context, landDetailVo.getAnnualPay(), "年缴纳土地使用税额金额：", "^-?[0-9]+(.[0-9]{0,2})?$", "填写格式只能为整数或保留2位小数")) {
        	b = false;
		}
        if (!b) { 
        	return; 
        }
        if (landDetailVo.getLandGetDatetime().getTime() > landDetailVo.getLandOverDatetime().getTime()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "土地取得日期不能大于土地终止日期", ""));
            b = false;
        }
        if (landDetailService.isExistLandDetail(companymstrId, landDetailVo)) {
        	context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "宗地名称重复", ""));
        	b = false;
        }
        if (!b) { 
        	return; 
        }
        if ("add".equals(excuteMethod)) {
            try {
                landDetailService.saveLandDetail(companymstrId, landDetailVo);
                RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "保存土地证编号：" + landDetailVo.getLandCertificateNo() + ",宗地名称：" + landDetailVo.getLandName() + "的土地明细信息成功,请查看并确认"));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
            }
        } else {
            try {
                landDetailService.updateLandDetail(landDetailVo);
                RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "编辑土地证编号：" + landDetailVo.getLandCertificateNo() + ",宗地名称：" + landDetailVo.getLandName() + "的土地明细信息成功,请查看并确认"));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
            }
        }
        queryLandDetail();
    }

    public LandDetailVo getLandDetailVo() {
        return landDetailVo;
    }

    public void setLandDetailVo(LandDetailVo landDetailVo) {
        this.landDetailVo = landDetailVo;
    }

    public LazyDataModel<LandDetailVo> getLazyLandDetailVoModel() {
        return lazyLandDetailVoModel;
    }

    public void setLazyLandDetailVoModel(LazyDataModel<LandDetailVo> lazyLandDetailVoModel) {
        this.lazyLandDetailVoModel = lazyLandDetailVoModel;
    }

    public String getLandCertificateNo() {
        return landCertificateNo;
    }

    public void setLandCertificateNo(String landCertificateNo) {
        this.landCertificateNo = landCertificateNo;
    }

    public String getLandName() {
        return landName;
    }

    public void setLandName(String landName) {
        this.landName = landName;
    }

    public String getExistLandName() {
        return existLandName;
    }

    public void setExistLandName(String existLandName) {
        this.existLandName = existLandName;
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

    public long getCompanymstrId() {
        return companymstrId;
    }

    public void setCompanymstrId(long companymstrId) {
        this.companymstrId = companymstrId;
    }

}
