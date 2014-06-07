package com.wcs.tih.filenet.helper.ce.util;

import java.util.ResourceBundle;

public class CeConfigOptions {
	public static final String DEFAULT_JAAS_STANZA = "FileNetP8WSI";
	private static final ResourceBundle CONFIG_BUNDLE = ResourceBundle.getBundle("filenet");

	public static String getJaasConfigFile() {
		return getString("java.security.auth.login.config");
	}

	public static String getWaspLocation() {
		return getString("wasp.location");
	}

	public static String getContentEngineUrl() {
		return getString("ce.uri");
	}

	public static String getObjectStoreName() {
		return getString("ce.os");
	}

	public static String getAdminName() {
		return getString("admin.id");
	}

	public static String getAdminPassword() {
		return getString("admin.password");
	}

	public static String getUserPassword() {
		return getString("user.password");
	}

	public static String getJaasStanza() {
		String stanza = DEFAULT_JAAS_STANZA;
		String cs = getString("ce.stanza");
		if (cs != null && !"".equals(cs)) {
			stanza = cs;
		}
		return stanza;
	}

	public static String getString(String key) {
		return CONFIG_BUNDLE.getString(key);
	}
}