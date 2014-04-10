package com.wcs.common.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AttachmentFolderBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// 资源
    private static ResourceBundle rb = ResourceBundle.getBundle("filenet");
	
    private static String attachmentTransferPriceFolder  = "ce.folder.attachmentTransferPrice";//转让定价
    private static String attachmentTaxAuthority  = "ce.folder.attachmentTaxAuthority";//税务机关
    private static String attachmentBranch  = "ce.folder.attachmentBranch";//税务机关
    private static String attachmentTaxIncentives  = "ce.folder.attachmentTaxIncentives";//税收优惠
    private static String attachmentFinancialReturn  = "ce.folder.attachmentFinancialReturn";//财政返还
    private static String attachmentMaterial  = "ce.folder.attachmentMaterial";//财政返还
    private static String attachmentLandDetails  = "ce.folder.attachmentLandDetails";//土地明细
    private static String attachmentEstate  = "ce.folder.attachmentEstate";//房产明细
    private static String attachmentTaxTypeRatio  = "ce.folder.attachmentTaxTypeRatio";//税种税率
    private static String attachmentMainAssets  = "ce.folder.attachmentMainAssets";//主要资产
    private static String attachmentInvestment  = "ce.folder.attachmentInvestment";//投资情况
    private static String attachmentStock  = "ce.folder.attachmentStock";//股权机构
    
    private static Map<String, String> subFolder;
    
    static{
    	subFolder = new HashMap<String, String>();
    	subFolder.put("INVS_TRANSFER_PRICE", rb.getString(attachmentTransferPriceFolder));
    	subFolder.put("TAXAUTHORITY_COMPANYMSTR", rb.getString(attachmentTaxAuthority));
    	subFolder.put("COMPANY_BRANCH", rb.getString(attachmentBranch));
    	subFolder.put("COMPANY_TAX_INCENTIVES", rb.getString(attachmentTaxIncentives));
    	subFolder.put("COMPANY_FINANCIAL_RETURN", rb.getString(attachmentFinancialReturn));
    	subFolder.put("COMPANY_MATERIAL", rb.getString(attachmentMaterial));
    	subFolder.put("COMPANY_LAND_DETAILS", rb.getString(attachmentLandDetails));
    	subFolder.put("COMPANY_ESTATE", rb.getString(attachmentEstate));
    	subFolder.put("COMPANY_TAX_TYPE_RATIO", rb.getString(attachmentTaxTypeRatio));
    	subFolder.put("COMPANY_MAIN_ASSETS", rb.getString(attachmentMainAssets));
    	subFolder.put("COMPANY_INVESTMENT", rb.getString(attachmentInvestment));
    	subFolder.put("COMPANY_STOCK_STRUCTURE", rb.getString(attachmentStock));
    }
    
    public String getSubFolder(String tableName){
    	return subFolder.get(tableName);
    }
    
    //================= getter & setter ===================
	public static Map<String, String> getSubFolder() {
		return subFolder;
	}

	public static void setSubFolder(Map<String, String> subFolder) {
		AttachmentFolderBean.subFolder = subFolder;
	}

}
