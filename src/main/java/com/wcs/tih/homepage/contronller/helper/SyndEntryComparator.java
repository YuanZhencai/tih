package com.wcs.tih.homepage.contronller.helper;

import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.tih.model.Rss;

/**
 * <p>Project: tih</p>
 * <p>Description: 新闻按时间排序</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
public class SyndEntryComparator implements Comparator<Rss> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public int compare(Rss rss1, Rss rss2) {
        try {
            return rss2.getPublishedDate().compareTo(rss1.getPublishedDate());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return 0;
        }
    }
}
