package com.wcs.tih.homepage.contronller.vo;

import java.io.Serializable;
import java.util.List;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.tih.model.Newschannelmstr;
import com.wcs.tih.model.Rss;

public class NewschannelmstrVo extends IdModel implements Serializable {

	private static final long serialVersionUID = -5313588416247600235L;

	private Newschannelmstr newschannelmstr;

	private List<Rss> rssList;

	public Newschannelmstr getNewschannelmstr() {
		return newschannelmstr;
	}

	public void setNewschannelmstr(Newschannelmstr newschannelmstr) {
		this.newschannelmstr = newschannelmstr;
	}

	public List<Rss> getRssList() {
		return rssList;
	}

	public void setRssList(List<Rss> rssList) {
		this.rssList = rssList;
	}
}
