package com.wcs.common.controller.vo;

import java.io.Serializable;

import com.wcs.common.controller.helper.IdModel;
import com.wcs.common.model.Synclog;

public class SynclogVo extends IdModel implements Serializable {

	private static final long serialVersionUID = -2579287573991290825L;
	private Synclog synclog;

	public Synclog getSynclog() {
		return synclog;
	}

	public void setSynclog(Synclog synclog) {
		this.synclog = synclog;
	}

}
