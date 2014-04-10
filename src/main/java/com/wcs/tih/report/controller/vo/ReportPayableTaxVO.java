package com.wcs.tih.report.controller.vo;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;
import com.wcs.tih.model.ReportPayableTax;

public class ReportPayableTaxVO extends IdModel {

    private static final long serialVersionUID = 1L;
    
    private ReportPayableTax payableTax = new ReportPayableTax();
    private O o = new O();
    private Companymstr c = new Companymstr();
    private String lowerCode;
    
    public ReportPayableTaxVO() {}
    
    public ReportPayableTaxVO(ReportPayableTax payableTax, O o, Companymstr c) {
        setId(payableTax.getId());
        this.payableTax = payableTax;
        this.o = o;
        this.c = c;
    }

    public ReportPayableTax getPayableTax() {
        return payableTax;
    }

    public void setPayableTax(ReportPayableTax payableTax) {
        this.payableTax = payableTax;
    }

    public Companymstr getC() {
        return c;
    }

    public void setC(Companymstr c) {
        this.c = c;
    }

    public O getO() {
        return o;
    }

    public void setO(O o) {
        this.o = o;
    }

    public String getLowerCode() {
        return lowerCode;
    }

    public void setLowerCode(String lowerCode) {
        this.lowerCode = lowerCode;
    }
    
}
