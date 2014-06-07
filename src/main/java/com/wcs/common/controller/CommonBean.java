package com.wcs.common.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.AnnualVo;
import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.scheduler.util.DateUtils;

@ManagedBean(name = "commonBean")
@ApplicationScoped
public class CommonBean {
    private Logger logger = LoggerFactory.getLogger(getClass());

    // 紧急程度
    private static final String TIH_TAX_WORKFLOWURGENCY = DictConsts.TIH_TAX_WORKFLOWURGENCY;
    private static final String TIH_TAX_WORKFLOWURGENCY_1 = DictConsts.TIH_TAX_WORKFLOWURGENCY_1;
    private static final String TIH_TAX_WORKFLOWURGENCY_2 = DictConsts.TIH_TAX_WORKFLOWURGENCY_2;
    private static final String TIH_TAX_WORKFLOWURGENCY_3 = DictConsts.TIH_TAX_WORKFLOWURGENCY_3;
    // 重要程度
    private static final String TIH_TAX_WORKFLOWIMPORTANCE = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE;
    private static final String TIH_TAX_WORKFLOWIMPORTANCE_1 = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_1;
    private static final String TIH_TAX_WORKFLOWIMPORTANCE_2 = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_2;
    private static final String TIH_TAX_WORKFLOWIMPORTANCE_3 = DictConsts.TIH_TAX_WORKFLOWIMPORTANCE_3;

    // 地域
    private static final String TIH_TAX_REGION = DictConsts.TIH_TAX_REGION;
    // 文档分类
    private static final String TIH_TAX_DOCTYPE = DictConsts.TIH_TAX_DOCTYPE;
    // 税种
    private static final String TIH_TAX_TYPE = DictConsts.TIH_TAX_TYPE;
    // 有效性
    private static final String TIH_TAX_DOCSTATUS = DictConsts.TIH_TAX_DOCSTATUS;
    // 发文字号
    private static final String TIH_TAX_NO = DictConsts.TIH_TAX_NO;
    // 发文机关
    private static final String TIH_TAX_ORG = DictConsts.TIH_TAX_ORG;
    // 检出
    private static final String TIH_DOC_STATUS_4 = DictConsts.TIH_DOC_STATUS_4;
    // 行业
    private static final String TIH_TAX_INDUSTRY = DictConsts.TIH_TAX_INDUSTRY;

    // 提交状态
    private static final String TIH_DOC_SUBMITSTATUS = DictConsts.TIH_DOC_SUBMITSTATUS;

    private static final String TIH_TAX_OPERATETYPE_TYPE = DictConsts.TIH_TAX_OPERATETYPE_TYPE;
    private static final String TIH_TAX_OPERATETYPE_TYPE_4 = DictConsts.TIH_TAX_OPERATETYPE_TYPE_4;
    private static final String TIH_TAX_ANTIAVOIDANCE_TYPE = DictConsts.TIH_TAX_ANTIAVOIDANCE_TYPE;
    // 流程发起类型
    private static final String TIH_TAX_REQUESTBY = DictConsts.TIH_TAX_REQUESTBY;
    // 集团
    private static final String TIH_TAX_REQUESTBY_1 = DictConsts.TIH_TAX_REQUESTBY_1;
    // 工厂
    private static final String TIH_TAX_REQUESTBY_2 = DictConsts.TIH_TAX_REQUESTBY_2;
    // 检查类型
    private static final String TIH_TAX_INSPECTION_TYPE = DictConsts.TIH_TAX_INSPECTION_TYPE;

    // 房产类型
    private static final String TIH_TAX_COMPANY_ESTATE_TYPE = DictConsts.TIH_TAX_COMPANY_ESTATE_TYPE;
    // 计税类型
    private static final String TIH_TAX_COMPANY_ESTATE_TAXTYPE = DictConsts.TIH_TAX_COMPANY_ESTATE_TAXTYPE;
    
    private static final String TIH_TAX_CURRENCY =DictConsts.TIH_TAX_CURRENCY;
    
 // 项目字典
    private static final String TIH_TAX_COMPANY_ASSETS_ITEM = DictConsts.TIH_TAX_COMPANY_ASSETS_ITEM;
    
    private static final String TIH_TAX_COMPANY_STOCK = DictConsts.TIH_TAX_COMPANY_STOCK_HOLDERTYPE;
    
 // 频率
    private static final String TIH_TAX_COMPANY_TAXRATE_RATE = DictConsts.TIH_TAX_COMPANY_TAXRATE_RATE;

    
    private static final String TIH_TAX_COMPANY_INCENTIVES_STATUS = DictConsts.TIH_TAX_COMPANY_INCENTIVES_STATUS;
    
    private static final String TIH_TAX_WORKFLOWSTATUS = DictConsts.TIH_TAX_WORKFLOWSTATUS;// 流程状态
    private static final String TIH_TAX_WORKFLOWSTATUS_1 = DictConsts.TIH_TAX_WORKFLOWSTATUS_1;// 新建
    private static final String TIH_TAX_WORKFLOWSTATUS_2 = DictConsts.TIH_TAX_WORKFLOWSTATUS_2;// 处理中
    private static final String TIH_TAX_WORKFLOWSTATUS_3 = DictConsts.TIH_TAX_WORKFLOWSTATUS_3;// 完成
    private static final String TIH_TAX_WORKFLOWSTATUS_4 = DictConsts.TIH_TAX_WORKFLOWSTATUS_4;// 终止
    // 重要程度
    // 申请单类型
    private static final String TIH_TAX_REQUESTFORM = DictConsts.TIH_TAX_REQUESTFORM;
 // 上传文档申请单
    private static final String TIH_TAX_REQUESTFORM_1 = DictConsts.TIH_TAX_REQUESTFORM_1;
 // 检入文档申请单
    private static final String TIH_TAX_REQUESTFORM_2 = DictConsts.TIH_TAX_REQUESTFORM_2;
 // 提问申请单
    private static final String TIH_TAX_REQUESTFORM_3 = DictConsts.TIH_TAX_REQUESTFORM_3;
    //报送报表
    private static final String TIH_TAX_REQUESTFORM_4 = DictConsts.TIH_TAX_REQUESTFORM_4;
    private static final  String TIH_TAX_REQUESTFORM_4_1 = DictConsts.TIH_TAX_REQUESTFORM_4_1;
    private static final  String TIH_TAX_REQUESTFORM_4_2 = DictConsts.TIH_TAX_REQUESTFORM_4_2;
  //情况反馈
    private static final String TIH_TAX_REQUESTFORM_5 = DictConsts.TIH_TAX_REQUESTFORM_5;
    
    private static final String TIH_TAX_TRADETYPE_TYPE = DictConsts.TIH_TAX_TRADETYPE_TYPE;
    private static final String TIH_TAX_VALIDATIONMETHOD_TYPE = DictConsts.TIH_TAX_VALIDATIONMETHOD_TYPE;
    
    private static final String TIH_TAX_TIMEOUTEMAIL_TYPE = DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE;
    private static final String TIH_TAX_TIMEOUTEMAIL_TYPE_1 = DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_1;
    private static final String TIH_TAX_TIMEOUTEMAIL_TYPE_2 = DictConsts.TIH_TAX_TIMEOUTEMAIL_TYPE_2;

    private static final String TIH_TAX_MSG_STATUS = DictConsts.TIH_TAX_MSG_STATUS;
    @EJB
    private CommonService commonService;

    private String lang = "zh_CN";


    // 构造方法
    // 这个托管bean主要是对字典表(DICT)的读取
    public CommonBean() {
    }

    @PostConstruct
    public void init() {
        try {
            lang = FacesContext.getCurrentInstance().getViewRoot().getLocale().toString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    // 这里是以CodeCat()+"."+CodeKey()来获取相应的value值.
    // 请参照数据库的字段.通过这种方法获取Value.
    public String getValueByDictCatKey(String catPointKey) {
        Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        /**
         * 这里必须需要注意的事情. 1.传入到这里的catPointKey必须是TIH.TAX.TYPE.3这种以.的形式的value.而不是DictConsts中的key.
         */
        String lang = browserLang.toString();
        return commonService.getValueByDictCatKey(catPointKey, lang);
    }

    // 这是通过一个数据库的Code_Cat字段来获取对应的list集合,常用于下拉框.
    public List<Dict> getDictByCat(String cat) {
        Locale browserLang = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return commonService.getDictByCat(cat, browserLang.toString());
    }

    // 刷新调用的方法
    public void refreshDictData() {
        commonService.queryDict();
    }

    /**
     * <p>
     * Description: 将日期转化成24小时的格式
     * </p>
     * 
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        if (date == null) { return ""; }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }

//    /**
//     * <p>Description: 将日期转化成24小时的格式</p>
//     * @param tt
//     * @return
//     */
//    public static String formatDate(Timestamp tt) {
//        if (tt == null) { return ""; }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        return sdf.format(tt);
//    }

    public String getDictHtmlClass(String dictValue) {
        if (dictValue == null) { return ""; }
        return dictValue.replaceAll("\\.", "-");
    }

    public List<Dict> completePublishNo(String queryCodeVal) {
        return commonService.completeByCat(queryCodeVal, DictConsts.TIH_TAX_NO, lang);
    }

    public List<Dict> completePublishOrg(String queryCodeVal) {
        return commonService.completeByCat(queryCodeVal, DictConsts.TIH_TAX_ORG, lang);
    }

    
    public String getTihTaxRegion() {
        return TIH_TAX_REGION;
    }

    public String getTihTaxWorkflowurgency() {
        return TIH_TAX_WORKFLOWURGENCY;
    }

    public String getTihTaxWorkflowurgency1() {
        return TIH_TAX_WORKFLOWURGENCY_1;
    }

    public String getTihTaxWorkflowurgency2() {
        return TIH_TAX_WORKFLOWURGENCY_2;
    }

    public String getTihTaxWorkflowurgency3() {
        return TIH_TAX_WORKFLOWURGENCY_3;
    }

    public String getTihTaxWorkflowimportance() {
        return TIH_TAX_WORKFLOWIMPORTANCE;
    }

    public String getTihTaxWorkflowimportance1() {
        return TIH_TAX_WORKFLOWIMPORTANCE_1;
    }

    public String getTihTaxWorkflowimportance2() {
        return TIH_TAX_WORKFLOWIMPORTANCE_2;
    }

    public String getTihTaxWorkflowimportance3() {
        return TIH_TAX_WORKFLOWIMPORTANCE_3;
    }

    public String getTihTaxDoctype() {
        return TIH_TAX_DOCTYPE;
    }

    public String getTihTaxType() {
        return TIH_TAX_TYPE;
    }

    public String getTihTaxDocstatus() {
        return TIH_TAX_DOCSTATUS;
    }

    public String getTihTaxNo() {
        return TIH_TAX_NO;
    }

    public String getTihTaxOrg() {
        return TIH_TAX_ORG;
    }

    public String getTihDocStatus4() {
        return TIH_DOC_STATUS_4;
    }

    public String getTihTaxIndustry() {
        return TIH_TAX_INDUSTRY;
    }

    public String getTihDocSubmitstatus() {
        return TIH_DOC_SUBMITSTATUS;
    }

    public String getTihTaxOperatetypeType() {
        return TIH_TAX_OPERATETYPE_TYPE;
    }

    public String getTihTaxOperatetypeType4() {
        return TIH_TAX_OPERATETYPE_TYPE_4;
    }

    public String getTihTaxAntiavoidanceType() {
        return TIH_TAX_ANTIAVOIDANCE_TYPE;
    }

    public String getTihTaxRequestby() {
        return TIH_TAX_REQUESTBY;
    }

    public String getTihTaxRequestby1() {
        return TIH_TAX_REQUESTBY_1;
    }

    public String getTihTaxRequestby2() {
        return TIH_TAX_REQUESTBY_2;
    }

    public String getTihTaxInspectionType() {
        return TIH_TAX_INSPECTION_TYPE;
    }

    public String getTihTaxCompanyEstateType() {
        return TIH_TAX_COMPANY_ESTATE_TYPE;
    }

    public String getTihTaxCompanyEstateTaxtype() {
        return TIH_TAX_COMPANY_ESTATE_TAXTYPE;
    }

    public String getTihTaxCurrency() {
        return TIH_TAX_CURRENCY;
    }

    public String getTihTaxCompanyAssetsItem() {
        return TIH_TAX_COMPANY_ASSETS_ITEM;
    }

    public String getTihTaxCompanyStock() {
        return TIH_TAX_COMPANY_STOCK;
    }

    public String getTihTaxCompanyTaxrateRate() {
        return TIH_TAX_COMPANY_TAXRATE_RATE;
    }

    public String getTihTaxCompanyIncentivesStatus() {
        return TIH_TAX_COMPANY_INCENTIVES_STATUS;
    }

    public String getTihTaxWorkflowstatus() {
        return TIH_TAX_WORKFLOWSTATUS;
    }

    public String getTihTaxWorkflowstatus1() {
        return TIH_TAX_WORKFLOWSTATUS_1;
    }

    public String getTihTaxWorkflowstatus2() {
        return TIH_TAX_WORKFLOWSTATUS_2;
    }

    public String getTihTaxWorkflowstatus3() {
        return TIH_TAX_WORKFLOWSTATUS_3;
    }

    public String getTihTaxWorkflowstatus4() {
        return TIH_TAX_WORKFLOWSTATUS_4;
    }

    public String getTihTaxRequestform() {
        return TIH_TAX_REQUESTFORM;
    }

    public String getTihTaxRequestform1() {
        return TIH_TAX_REQUESTFORM_1;
    }

    public String getTihTaxRequestform2() {
        return TIH_TAX_REQUESTFORM_2;
    }

    public String getTihTaxRequestform3() {
        return TIH_TAX_REQUESTFORM_3;
    }

    public String getTihTaxRequestform4() {
        return TIH_TAX_REQUESTFORM_4;
    }

    public String getTihTaxRequestform41() {
        return TIH_TAX_REQUESTFORM_4_1;
    }

    public String getTihTaxRequestform42() {
        return TIH_TAX_REQUESTFORM_4_2;
    }

    public String getTihTaxRequestform5() {
        return TIH_TAX_REQUESTFORM_5;
    }

    public String getTihTaxTradetypeType() {
        return TIH_TAX_TRADETYPE_TYPE;
    }

    public String getTihTaxValidationmethodType() {
        return TIH_TAX_VALIDATIONMETHOD_TYPE;
    }

	public String getTihTaxTimeoutemailType() {
		return TIH_TAX_TIMEOUTEMAIL_TYPE;
	}

	public String getTihTaxTimeoutemailType1() {
		return TIH_TAX_TIMEOUTEMAIL_TYPE_1;
	}

	public String getTihTaxTimeoutemailType2() {
		return TIH_TAX_TIMEOUTEMAIL_TYPE_2;
	}

	public String getTihTaxMsgStatus() {
		return TIH_TAX_MSG_STATUS;
	}

	public List<AnnualVo> getAnnuals(Date selectedDate) {
		List<AnnualVo> annuals = new ArrayList<AnnualVo>();
		try {
			annuals = commonService.getAnnualsBy(selectedDate);
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return annuals;
	}

}