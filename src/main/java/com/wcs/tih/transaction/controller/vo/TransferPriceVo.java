package com.wcs.tih.transaction.controller.vo;

import java.util.List;

import com.wcs.tih.model.InvsTransferPrice;
import com.wcs.tih.model.InvsVerifyTransType;

public class TransferPriceVo extends InvsTransferPrice {

    private static final long serialVersionUID = 1L;
    
    private boolean same;
    private boolean parent = false;
    private boolean selected = false;
    private InvsTransferPrice transferPrice = new InvsTransferPrice();
    private InvsVerifyTransType transType = new InvsVerifyTransType();
    private TransTypeVo transTypeVo = new TransTypeVo();
    private List<Long> companys;
    private String companyCode;
    private String lowerCode;

    public InvsTransferPrice getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(InvsTransferPrice transferPrice) {
        this.transferPrice = transferPrice;
    }

    public InvsVerifyTransType getTransType() {
        return transType;
    }

    public void setTransType(InvsVerifyTransType transType) {
        this.transType = transType;
    }

    public boolean isParent() {
        return parent;
    }

    public void setParent(boolean parent) {
        this.parent = parent;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSame() {
        return same;
    }

    public void setSame(boolean same) {
        this.same = same;
    }

    public List<Long> getCompanys() {
        return companys;
    }

    public void setCompanys(List<Long> companys) {
        this.companys = companys;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getLowerCode() {
        return lowerCode;
    }

    public void setLowerCode(String lowerCode) {
        this.lowerCode = lowerCode;
    }

    public TransTypeVo getTransTypeVo() {
        return transTypeVo;
    }

    public void setTransTypeVo(TransTypeVo transTypeVo) {
        this.transTypeVo = transTypeVo;
    }
    
}
