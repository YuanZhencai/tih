package com.wcs.tih.filenet.helper.pe;

import java.util.ResourceBundle;

public class PEConfigOptions {
    private static final ResourceBundle CONFIG_BUNDLE = ResourceBundle.getBundle("filenet");

    public static String getProcessEngineUrl() {
        return getString("pe.uri");
    }

    public static String getConnectionPointName() {
        return getString("pe.connectionPointName");
    }

    private static String getString(String key) {
        return CONFIG_BUNDLE.getString(key);
    }
}