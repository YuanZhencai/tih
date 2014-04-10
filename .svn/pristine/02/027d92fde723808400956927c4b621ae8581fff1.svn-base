package com.wcs.tih.transaction.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.common.controller.vo.DictVo;
import com.wcs.tih.model.WfInstancemstr;

public class WfInstancemstrVo extends IdModel implements Serializable {

    private static final long serialVersionUID = -9058794004947408594L;
    private DictVo typeDictVo = new DictVo();
    private WfInstancemstr wfInstancemstr;
    private String frontHandleDatetime;// 前一个处理时间
    private String noabb;
    private String importance;
    private String urgency;
    private String type;
    private String status;

    public WfInstancemstr getWfInstancemstr() {
        return wfInstancemstr;
    }

    public void setWfInstancemstr(WfInstancemstr wfInstancemstr) {
        this.wfInstancemstr = wfInstancemstr;
    }

    public String getFrontHandleDatetime() {
        return frontHandleDatetime;
    }

    public void setFrontHandleDatetime(String frontHandleDatetime) {
        this.frontHandleDatetime = frontHandleDatetime;
    }

	public void setNoabb(String noabb) {
		this.noabb = noabb;
	}
    
    public String getNoabb() {
		String no = this.wfInstancemstr.getNo();
		if(!no.equals("未启动")){
			String no1 = no.substring(0, 4);
			String no2 = no.substring(no.length()-4);
			noabb = no1+".........."+no2;
		}else{
			noabb = no;
		}
		return noabb;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public String getUrgency() {
		return urgency;
	}

	public void setUrgency(String urgency) {
		this.urgency = urgency;
	}

	public String getType() {
		return this.wfInstancemstr.getType();
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return this.wfInstancemstr.getStatus();
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public DictVo getTypeDictVo() {
        return typeDictVo;
    }

    public void setTypeDictVo(DictVo typeDictVo) {
        this.typeDictVo = typeDictVo;
    }

}
