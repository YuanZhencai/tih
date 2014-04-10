package com.wcs.common.controller.vo;

import java.util.ArrayList;
import java.util.List;

public class SubjectVo {
    private String subject;
    private String urgent;
    private String important;
    private String link;
    private boolean display = true;//html是否显示subjectVo
    private List<NotificationVo> notices = new ArrayList<NotificationVo>();


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUrgent() {
        return urgent;
    }

    public void setUrgent(String urgent) {
        this.urgent = urgent;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public List<NotificationVo> getNotices() {
        return notices;
    }

    public void setNotices(List<NotificationVo> notices) {
        this.notices = notices;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

}
