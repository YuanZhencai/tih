package com.wcs.tih.system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.model.Dict;
import com.wcs.tih.system.controller.vo.CommonTempVo;
import com.wcs.tih.system.controller.vo.MaterialVo;
import com.wcs.tih.system.service.MaterialService;
import com.wcs.tih.util.ValidateUtil;

@ManagedBean(name = "materialBean")
@ViewScoped
public class MaterialBean {
    private static final String ISSUCC = "issucc";
    @EJB
    private MaterialService materialService;
    private MaterialVo materialVo;
    private LazyDataModel<MaterialVo> lazyMaterialVoModel;
    private LazyDataModel<CommonTempVo> lazyMainMaterialModel;
    private LazyDataModel<CommonTempVo> lazyMainProductModel;
    private LazyDataModel<CommonTempVo> lazyProcessingModel;
    private static List<CommonTempVo> mainMaterialList;
    private static List<CommonTempVo> mainProductList;
    private static List<CommonTempVo> processingList;
    private CommonTempVo ctvMM;
    private CommonTempVo ctvMP;
    private CommonTempVo ctvPR;
    private String mainMaterial;
    private String mainProduct;
    private String existMainMaterial;
    private String existMainProduct;
    private String effective;
    private String excuteMethod;
    private long companymstrId;
    private List<MaterialVo> mvs;


    /**
     * <p>
     * Description: 初始化
     * </p>
     */
    @PostConstruct
    public void init() {
        materialVo = new MaterialVo();
        mainMaterialList = new ArrayList<CommonTempVo>();
        Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        List<Dict> dicts = materialService.getDictByCat(DictConsts.TIH_TAX_VARIETY, browserLang.toString());
        if (dicts != null && dicts.size() != 0) {
            for (Dict d : dicts) {
                mainMaterialList.add(new CommonTempVo(d.getId(), d.getCodeVal()));
            }
        }
        addMainProductList(DictConsts.TIH_TAX_PRODUCT, browserLang);
        addProcessingList(DictConsts.TIH_TAX_PROCESSING, browserLang);
        lazyMainMaterialModel = new PageModel<CommonTempVo>(mainMaterialList, false);
        lazyMainProductModel = new PageModel<CommonTempVo>(mainProductList, false);
        lazyProcessingModel = new PageModel<CommonTempVo>(processingList, false);
    }
    
    public void addMainProductList(String mainProduct, Locale browserLang){
        mainProductList = new ArrayList<CommonTempVo>();
        List<Dict> dicts = materialService.getDictByCat(mainProduct, browserLang.toString());
        if (dicts != null && dicts.size() != 0) {
            for (Dict d : dicts) {
                mainProductList.add(new CommonTempVo(d.getId(), d.getCodeVal()));
            }
        }
    }
    
    public void addProcessingList(String processing, Locale browserLang){
        processingList = new ArrayList<CommonTempVo>();
        List<Dict> dicts = materialService.getDictByCat(processing, browserLang.toString());
        if (dicts != null && dicts.size() != 0) {
            for (Dict d : dicts) {
                processingList.add(new CommonTempVo(d.getId(), d.getCodeVal()));
            }
        }
    }

    /**
     * <p>
     * Description: 初始化
     * </p>
     */
    public void initMaterial() {
        lazyMaterialVoModel = null;
        mainMaterial = "";
        mainProduct = "";
        effective = "N";
        this.queryMaterial();
    }

    /**
     * <p>
     * Description: 查询原料及工艺信息
     * </p>
     */
    public void queryMaterial() {
        mvs = materialService.queryMaterial(this.companymstrId, mainMaterial, mainProduct, effective);
        if (null != mvs && mvs.size() != 0) {
            lazyMaterialVoModel = new PageModel<MaterialVo>(mvs, false);
        } else {
            lazyMaterialVoModel = null;
        }
    }

    /**
     * <p>
     * Description: 增加原料及工艺信息
     * </p>
     */
    public void addMaterial() {
        materialVo = new MaterialVo();
        materialVo.setEffective("N");
        materialVo.setUnit("吨/天");
    }

    public void selectMainMaterial() {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
        if (null == ctvMM || null == ctvMM.getName() || "".equals(ctvMM.getName())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "请先选择一个原料"));
            return;
        }
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
        materialVo.setMainMaterial(ctvMM.getName());
    }

    public void selectMainProduct() {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
        if (null == ctvMP || null == ctvMP.getName() || "".equals(ctvMP.getName())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "请先选择一个产品"));
            return;
        }
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
        materialVo.setMainProduct(ctvMP.getName());
    }

    public void selectProcessing() {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
        if (null == ctvPR || null == ctvPR.getName() || "".equals(ctvPR.getName())) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "请先选择一个加工项目"));
            return;
        }
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
        materialVo.setProcessing(ctvPR.getName());
    }

    public void selectMM(SelectEvent se) {
        ctvMM = (CommonTempVo) se.getObject();
    }

    public void selectMP(SelectEvent se) {
        ctvMP = (CommonTempVo) se.getObject();
    }

    public void selectPR(SelectEvent se) {
        ctvPR = (CommonTempVo) se.getObject();
    }

    /**
     * <p>
     * Description: 保存原料及工艺信息
     * </p>
     */
    public void saveMaterial() {
        FacesContext context = FacesContext.getCurrentInstance();
        RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "no");
        boolean b = true;
        for(MaterialVo vo : mvs){ //编辑时，验证主要原料和加工项目是否修改
        	if(vo.getProcessing().equals(materialVo.getProcessing()) && vo.getMainMaterial().equals(materialVo.getMainMaterial()) && vo.getId().equals(materialVo.getId())){
        		b = true;
        	}
        }
        if (!(ValidateUtil.validateRequiredAndMax(context, materialVo.getMainMaterial(), "主要原料：", 100) & ValidateUtil.validateRequiredAndMax(context, materialVo.getMainProduct(), "主要产品：", 100) & ValidateUtil.validateRequiredAndMax(context, materialVo.getProcessing(), "加工项目：", 100) & ValidateUtil.validateRequired(context, materialVo.getAbility(), "处理能力："))){
        	b = false;
        }
        if (null != materialVo.getAbility() && materialVo.getAbility().intValue() == 0) {
            FacesMessage error = new FacesMessage(FacesMessage.SEVERITY_ERROR, "处理能力：", "");
            error.setDetail("不允许为空。");
            context.addMessage(null, error);
            b = false;
        }
        if (b) {
        	if (materialService.isExistMaterial(companymstrId, materialVo,excuteMethod)){
        		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "加工项目: ", "该原料已有此加工项目，不能重复添加"));
        		this.queryMaterial();
        		return;
        	}
        }
        if (!b){
        	return;
        }
        if ("add".equals(excuteMethod)) {
            try {
                materialService.saveMaterial(companymstrId, materialVo);
                RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "保存产品原料：" + materialVo.getMainMaterial() + "，主要产品：" + materialVo.getMainProduct() + "的原料及工艺信息成功,请查看并确认"));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
            }
        } else {
            try {
                materialService.updateMaterial(materialVo);
                RequestContext.getCurrentInstance().addCallbackParam(ISSUCC, "yes");
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "编辑产品原料：" + materialVo.getMainMaterial() + "，主要产品：" + materialVo.getMainProduct() + "的原料及工艺信息成功,请查看并确认"));
            } catch (Exception e) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "失败", e.getMessage()));
            }
        }
        queryMaterial();
    }

    public MaterialVo getMaterialVo() {
        return materialVo;
    }

    public void setMaterialVo(MaterialVo materialVo) {
        this.materialVo = materialVo;
    }

    public LazyDataModel<MaterialVo> getLazyMaterialVoModel() {
        return lazyMaterialVoModel;
    }

    public void setLazyMaterialVoModel(LazyDataModel<MaterialVo> lazyMaterialVoModel) {
        this.lazyMaterialVoModel = lazyMaterialVoModel;
    }

    public String getMainMaterial() {
        return mainMaterial;
    }

    public void setMainMaterial(String mainMaterial) {
        this.mainMaterial = mainMaterial;
    }

    public String getMainProduct() {
        return mainProduct;
    }

    public void setMainProduct(String mainProduct) {
        this.mainProduct = mainProduct;
    }

    public String getExistMainMaterial() {
        return existMainMaterial;
    }

    public void setExistMainMaterial(String existMainMaterial) {
        this.existMainMaterial = existMainMaterial;
    }

    public String getExistMainProduct() {
        return existMainProduct;
    }

    public void setExistMainProduct(String existMainProduct) {
        this.existMainProduct = existMainProduct;
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

    public LazyDataModel<CommonTempVo> getLazyMainMaterialModel() {
        return lazyMainMaterialModel;
    }

    public void setLazyMainMaterialModel(LazyDataModel<CommonTempVo> lazyMainMaterialModel) {
        this.lazyMainMaterialModel = lazyMainMaterialModel;
    }

    public LazyDataModel<CommonTempVo> getLazyMainProductModel() {
        return lazyMainProductModel;
    }

    public void setLazyMainProductModel(LazyDataModel<CommonTempVo> lazyMainProductModel) {
        this.lazyMainProductModel = lazyMainProductModel;
    }

    public LazyDataModel<CommonTempVo> getLazyProcessingModel() {
        return lazyProcessingModel;
    }

    public void setLazyProcessingModel(LazyDataModel<CommonTempVo> lazyProcessingModel) {
        this.lazyProcessingModel = lazyProcessingModel;
    }

}
