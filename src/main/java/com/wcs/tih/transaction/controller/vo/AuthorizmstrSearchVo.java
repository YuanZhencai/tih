package com.wcs.tih.transaction.controller.vo;

import java.io.Serializable;
import java.util.Date;

import com.wcs.common.controller.helper.IdModel;

public class AuthorizmstrSearchVo extends IdModel implements Serializable {

    private static final long serialVersionUID = 1838625992185241979L;
    private String authorizedBy;// 授权人
    private String authorizedTo;// 被授权人
    private Date startDatetime;
    private Date endDatetime;

    public String getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(String authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public String getAuthorizedTo() {
        return authorizedTo;
    }

    public void setAuthorizedTo(String authorizedTo) {
        this.authorizedTo = authorizedTo;
    }

    public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }
}
