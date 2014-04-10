package com.wcs.tih.filenet.helper.ce.util;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import javax.security.auth.Subject;

public class AuthenticatedObjectStore {
	private ObjectStore os;
	private Subject subject;

	static {
		CeConfigUtils.config();
	}

	public AuthenticatedObjectStore(String userName, String password, boolean fetch) {
		Connection conn = ConnectionUtils.getConnection();
		this.subject = UserContextUtils.pushObject(conn, userName, password);
		Domain domain = Factory.Domain.getInstance(conn, null);
		if (fetch) {
			this.os = (ObjectStore) Factory.ObjectStore.fetchInstance(domain, CeConfigOptions.getObjectStoreName(), null);
		} else {
			this.os = (ObjectStore) Factory.ObjectStore.getInstance(domain, CeConfigOptions.getObjectStoreName());
		}
	}

	public static AuthenticatedObjectStore createDefault() {
		return new AuthenticatedObjectStore(CeConfigOptions.getAdminName(), CeConfigOptions.getAdminPassword(), true);
	}

	public Subject getSubject() {
		return this.subject;
	}

	public ObjectStore getObjectStore() {
		return this.os;
	}
}