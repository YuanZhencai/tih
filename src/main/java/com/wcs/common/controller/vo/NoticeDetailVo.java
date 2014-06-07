package com.wcs.common.controller.vo;

public class NoticeDetailVo {

    private String detailType;// 消息详细的当时操作的类型
    private String value;// 消息详细的内容
    private boolean display = true;// 消息详细的内容

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

}
