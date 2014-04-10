package com.wcs.tih.transaction.controller.vo;


import java.util.List;

import com.wcs.tih.model.InvsVerifyTransType;

public class TransTypeVo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private InvsVerifyTransType verifyTransType = new InvsVerifyTransType();
    
    private Long id;
    
    private String adjustSpecialReason;
    
    private List<String> adjustSpecialReasons;

    private double afterAdjustRatio;

    private double beforeAdjustRatio;

    private double compareCompanyMedian;

    private String defunctInd;
    
    private String transType;

    private String validationMethod;
    
    private long random;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvsVerifyTransType getVerifyTransType() {
        return verifyTransType;
    }

    public void setVerifyTransType(InvsVerifyTransType verifyTransType) {
        this.verifyTransType = verifyTransType;
    }

    public long getRandom() {
        return random;
    }

    public void setRandom(long random) {
        this.random = random;
    }

    public String getAdjustSpecialReason() {
        return adjustSpecialReason;
    }

    public void setAdjustSpecialReason(String adjustSpecialReason) {
        this.adjustSpecialReason = adjustSpecialReason;
    }

    public double getAfterAdjustRatio() {
        return afterAdjustRatio;
    }

    public void setAfterAdjustRatio(double afterAdjustRatio) {
        this.afterAdjustRatio = afterAdjustRatio;
    }

    public double getBeforeAdjustRatio() {
        return beforeAdjustRatio;
    }

    public void setBeforeAdjustRatio(double beforeAdjustRatio) {
        this.beforeAdjustRatio = beforeAdjustRatio;
    }

    public double getCompareCompanyMedian() {
        return compareCompanyMedian;
    }

    public void setCompareCompanyMedian(double compareCompanyMedian) {
        this.compareCompanyMedian = compareCompanyMedian;
    }

    public String getDefunctInd() {
        return defunctInd;
    }

    public void setDefunctInd(String defunctInd) {
        this.defunctInd = defunctInd;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getValidationMethod() {
        return validationMethod;
    }

    public void setValidationMethod(String validationMethod) {
        this.validationMethod = validationMethod;
    }

	public List<String> getAdjustSpecialReasons() {
		return adjustSpecialReasons;
	}

	public void setAdjustSpecialReasons(List<String> adjustSpecialReasons) {
		this.adjustSpecialReasons = adjustSpecialReasons;
	}
    
}
