package com.wcs.tih.filenet.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Project: tih
 * Description: 文档Model，对应Filenet上的文档类
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
public class FnDocument implements Serializable {
    private static final long serialVersionUID = 1L;
    // 基本属性
    private String  createdBy;      // 创建人员
    private Date    createdDate;    // 创建时间
    private String  updatedBy;      // 修改人员
    private Date    updatedDate;    // 修改时间
    private String  id;             // 文档惟一标识，系统自动生成
    private Boolean isFrozen;       // 是否被冻结(检出否)
    private Boolean isCurrent;      // 是否为当前版本
    private Integer majorVersion;   // 主版本号
    private Integer minorVersion;   // 次版本号
    private String  currentState;   // 版本状态(Released,...)
    private Double  size;           // 文档大小
    private String  mimeType;       // 文档类型
    private String  documentTitle;  // 文档标题，非业务属性
    private String  foldersFiledIn; // 文档所属文件夹
    
    // 业务属性
    private final static String CATEGORY     = "category";        // 文档类别
    private final static String TAXTYPE      = "taxType";         // 税种
    private final static String DOCTYPE      = "docType";         // 文档分类
    private final static String PUBLISHORG   = "publishOrg";      // 发文机关
    private final static String PUBLISHNO    = "publishNo";       // 发文字号
    private final static String PUBLISHYEAR  = "publishYear";     // 发文年度
    private final static String PUBLISHSEQNO = "publishSeqNo";    // 发文序号
    private final static String PUBLISHTIME  = "publishTime";     // 发文日期
    private final static String BELONGTOCOMPANY = "belongtoCompany"; // 所属公司
    private final static String EFFECTSTATUS = "effectStatus";    // 有效性
    private final static String DESC         = "desc";            // 简单描述
    private final static String REGION       = "region";          // 地域
    private final static String SUBMITCOMPANY= "submitCompany";   // 提交资料公司
    private final static String SUBMITYEAR   = "submitYear";      // 提交资料年度
    private final static String SUBMITSTATUS = "submitStatus";    // 资料提交状态
    private final static String AUDITSTATUS  = "auditStatus";     // 审核状态
    private final static String INDUSTRY     = "industry";        // 行业

    private Map<String, Object> map = new HashMap<String, Object>();
    
    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    // 业务属性 Getter & Setter
    public String getIndustry() {
        if(map.get(INDUSTRY) == null) {
            map.put(INDUSTRY, null);
        }
        return (String) map.get(INDUSTRY);
    }

    public void setIndustry(String industry) {
        map.put(INDUSTRY, industry);
    }
    
    public String getCategory() {
        if(map.get(CATEGORY) == null) {
            map.put(CATEGORY, null);
        }
        return (String) map.get(CATEGORY);
    }

    public void setCategory(String category) {
        map.put(CATEGORY, category);
    }

    public String getTaxType() {
        if(map.get(TAXTYPE) == null) {
            map.put(TAXTYPE, null);
        }
        return (String) map.get(TAXTYPE);
    }

    public void setTaxType(String taxType) {
        map.put(TAXTYPE, taxType);
    }

    public String getPublishOrg() {
        if(map.get(PUBLISHORG) == null) {
            map.put(PUBLISHORG, null);
        }
        return (String) map.get(PUBLISHORG);
    }

    public void setPublishOrg(String publishOrg) {
        map.put(PUBLISHORG, publishOrg);
    }

    public String getPublishNo() {
        if(map.get(PUBLISHNO) == null) {
            map.put(PUBLISHNO, null);
        }
        return (String) map.get(PUBLISHNO);
    }

    public void setPublishNo(String publishNo) {
        map.put(PUBLISHNO, publishNo);
    }

    public String getPublishYear() {
        if(map.get(PUBLISHYEAR) == null) {
            map.put(PUBLISHYEAR, null);
        }
        return (String) map.get(PUBLISHYEAR);
    }

    public void setPublishYear(String publishYear) {
        map.put(PUBLISHYEAR, publishYear);
    }

    public String getPublishSeqNo() {
        if(map.get(PUBLISHSEQNO) == null) {
            map.put(PUBLISHSEQNO, null);
        }
        return (String) map.get(PUBLISHSEQNO);
    }

    public void setPublishSeqNo(String publishSeqNo) {
        map.put(PUBLISHSEQNO, publishSeqNo);
    }

    public Date getPublishTime() {
        if(map.get(PUBLISHTIME) == null) {
            map.put(PUBLISHTIME, null);
        }
        return (Date) map.get(PUBLISHTIME);
    }

    public void setPublishTime(Date publishTime) {
        map.put(PUBLISHTIME, publishTime);
    }

    public String getBelongtoCompany() {
        if(map.get(BELONGTOCOMPANY) == null) {
            map.put(BELONGTOCOMPANY, null);
        }
        return (String) map.get(BELONGTOCOMPANY);
    }

    public void setBelongtoCompany(String belongtoCompany) {
        map.put(BELONGTOCOMPANY, belongtoCompany);
    }

    public String getEffectStatus() {
        if(map.get(EFFECTSTATUS) == null) {
            map.put(EFFECTSTATUS, null);
        }
        return (String) map.get(EFFECTSTATUS);
    }

    public void setEffectStatus(String effectStatus) {
        map.put(EFFECTSTATUS, effectStatus);
    }

    public String getDesc() {
        if(map.get(DESC) == null) {
            map.put(DESC, null);
        }
        return (String) map.get(DESC);
    }

    public void setDesc(String desc) {
        map.put(DESC, desc);
    }

    public String getRegion() {
        if(map.get(REGION) == null) {
            map.put(REGION, null);
        }
        return (String) map.get(REGION);
    }

    public void setRegion(String region) {
        map.put(REGION, region);
    }

    public String getSubmitCompany() {
        if(map.get(SUBMITCOMPANY) == null) {
            map.put(SUBMITCOMPANY, null);
        }
        return (String) map.get(SUBMITCOMPANY);
    }

    public void setSubmitCompany(String submitCompany) {
        map.put(SUBMITCOMPANY, submitCompany);
    }

    public Date getSubmitYear() {
        if(map.get(SUBMITYEAR) == null) {
            map.put(SUBMITYEAR, null);
        }
        return (Date) map.get(SUBMITYEAR);
    }

    public void setSubmitYear(Date submitYear) {
        map.put(SUBMITYEAR, submitYear);
    }

    public String getSubmitStatus() {
        if(map.get(SUBMITSTATUS) == null) {
            map.put(SUBMITSTATUS, null);
        }
        return (String) map.get(SUBMITSTATUS);
    }

    public void setSubmitStatus(String submitStatus) {
        map.put(SUBMITSTATUS, submitStatus);
    }

    public String getAuditStatus() {
        if(map.get(AUDITSTATUS) == null) {
            map.put(AUDITSTATUS, null);
        }
        return (String) map.get(AUDITSTATUS);
    }

    public void setAuditStatus(String auditStatus) {
        map.put(AUDITSTATUS, auditStatus);
    }
    
    public String getDocType() {
    	if(map.get(DOCTYPE) == null) {
            map.put(DOCTYPE, null);
        }
        return (String) map.get(DOCTYPE);
	}
    
    public void setDocType(String docType) {
        map.put(DOCTYPE, docType);
    }

	// 基本属性 Getter && Setter
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Date getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public String getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Date getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Boolean getIsFrozen() {
        return isFrozen;
    }
    
    public void setIsFrozen(Boolean isFrozen) {
        this.isFrozen = isFrozen;
    }
    
    public Boolean getIsCurrent() {
        return isCurrent;
    }
    
    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
    
    public Integer getMajorVersion() {
        return majorVersion;
    }
    
    public void setMajorVersion(Integer majorVersion) {
        this.majorVersion = majorVersion;
    }
    
    public Integer getMinorVersion() {
        return minorVersion;
    }
    
    public void setMinorVersion(Integer minorVersion) {
        this.minorVersion = minorVersion;
    }
    
    public String getCurrentState() {
        return currentState;
    }
    
    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }
    
    public Double getSize() {
        return size;
    }
    
    public void setSize(Double size) {
        this.size = size;
    }
    
    public String getMimeType() {
        return mimeType;
    }
    
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    
    public String getDocumentTitle() {
        return documentTitle;
    }
    
    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getFoldersFiledIn() {
        return foldersFiledIn;
    }

    public void setFoldersFiledIn(String foldersFiledIn) {
        this.foldersFiledIn = foldersFiledIn;
    }

}
