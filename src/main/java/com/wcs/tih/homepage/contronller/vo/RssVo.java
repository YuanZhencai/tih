package com.wcs.tih.homepage.contronller.vo;

import com.wcs.tih.model.Rss;

public class RssVo {
    private Rss rss = new Rss();
    
    private String content;

    public Rss getRss() {
        return rss;
    }

    public void setRss(Rss rss) {
        this.rss = rss;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
