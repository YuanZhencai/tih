package com.wcs.common.controller.vo;

import java.util.Date;

import com.wcs.base.model.IdEntity;

public class CompanyManagerModel extends IdEntity {
	private String stext;
	private String hanYuStext;
	private String address;
	private String hanYuAddress;
	private String zipcode;
	private String telphone;
	private String jgName;
	private String jgCode;
	private String type;
	private String desc;
	private String oid;
	private String region = " ";
	private String province = " ";
	private String code;
	private String lowererCode;
	
	private Date startDatetime;
	public Date getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(Date startDatetime) {
        this.startDatetime = startDatetime;
    }

    public Date getStepDatetime() {
        return stepDatetime;
    }

    public void setStepDatetime(Date stepDatetime) {
        this.stepDatetime = stepDatetime;
    }

    private Date stepDatetime;
	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJgCode() {
		return jgCode;
	}

	public void setJgCode(String jgCode) {
		this.jgCode = jgCode;
	}

	private String defuctInt;

	public String getStext() {
		return stext;
	}

	public void setStext(String stext) {
		this.stext = stext;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getJgName() {
		return jgName;
	}

	public void setJgName(String jgName) {
		this.jgName = jgName;
	}

	public String getDefuctInt() {
		return defuctInt;
	}

	public void setDefuctInt(String defuctInt) {
		this.defuctInt = defuctInt;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLowererCode() {
        return lowererCode;
    }

    public void setLowererCode(String lowererCode) {
        this.lowererCode = lowererCode;
    }

    public String getHanYuStext() {
        return hanYuStext;
    }

    public void setHanYuStext(String hanYuStext) {
        this.hanYuStext = hanYuStext;
    }

    public String getHanYuAddress() {
        return hanYuAddress;
    }

    public void setHanYuAddress(String hanYuAddress) {
        this.hanYuAddress = hanYuAddress;
    }
	
}
