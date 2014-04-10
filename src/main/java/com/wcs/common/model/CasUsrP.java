package com.wcs.common.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CAS_USR_P")
public class CasUsrP implements Serializable {
    private static final long serialVersionUID = -784063246286468119L;

    private String id;

    @Id
    private String pernr;

    @Column(name = "DEFUNCT_IND", nullable = false, length = 1)
    private String defunctInd;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getDefunctInd() {
        return defunctInd;
    }

    public void setDefunctInd(String defunctInd) {
        this.defunctInd = defunctInd;
    }

}
