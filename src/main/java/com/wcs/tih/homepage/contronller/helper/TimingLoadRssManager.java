package com.wcs.tih.homepage.contronller.helper;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.tih.homepage.service.RssService;

/**
 * <p>Project: tih</p>
 * <p>Description: 定时加载RSS数据</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Singleton
public class TimingLoadRssManager {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @EJB
    private RssService rssService;

    /* 每天每小时每分钟 */
    // @Schedule(minute = "*", dayOfWeek = "*", hour = "*")
    /* 每天8,13,16,20点执行 */
    @Schedule(dayOfWeek = "*", hour = "8,13,16,20",persistent = false)
    public void run() {
    	Date date = new Date();
    	logger.info("begin TimingLoadRssManager.run()"+date.toString());
    	rssService.refreshAllRss();
        Date date1 = new Date();
        logger.info("end TimingLoadRssManager.run()"+date1.toString());
    }
}