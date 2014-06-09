package com.wcs.common.controller.vo;

import java.io.Serializable;
import java.util.Date;

import com.wcs.common.controller.helper.IdModel;

public class UserVo extends IdModel implements Serializable {

    private static final long serialVersionUID = -441203132140100200L;

    private String userName;
    private String realName;
    private String organizationName;
    private String jobNumber;
    private String email;
    private String phone;
    private String telephone;
    private String sex;
    private String certificatesType;
    private String certificatesNumber;
    private Date workDateTime;
    private Date birthday;
    private String effective;
    private String remark;
    private String positionRemark;

    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCertificatesType() {
        return certificatesType;
    }

    public void setCertificatesType(String certificatesType) {
        this.certificatesType = certificatesType;
    }

    public String getCertificatesNumber() {
        return certificatesNumber;
    }

    public void setCertificatesNumber(String certificatesNumber) {
        this.certificatesNumber = certificatesNumber;
    }

    public Date getWorkDateTime() {
        return workDateTime;
    }

    public void setWorkDateTime(Date workDateTime) {
        this.workDateTime = workDateTime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public String getPositionRemark() {
		return positionRemark;
	}

	public void setPositionRemark(String positionRemark) {
		this.positionRemark = positionRemark;
	}

    
}
