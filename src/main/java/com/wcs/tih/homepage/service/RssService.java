package com.wcs.tih.homepage.service;

import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import com.wcs.tih.model.Newschannelmstr;
import com.wcs.tih.model.Rss;

/**
 * <p>Project: tih</p>
 * <p>Description: RSS</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class RssService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @PersistenceContext
    private EntityManager em;
    
    /**
     * <p>Description: 保存Rss</p>
     * @param r
     * @return
     */
    public boolean saveRss(Rss r) {
        boolean b = false;
        try {
            if(r.getLink()!=null&&r.getLink().length()<1000){
            	if(r.getTitle()!=null&&r.getTitle().length()>=300){
            		r.setTitle(r.getTitle().substring(0,300));
            	}
            	this.em.persist(r);
            	b = true;
            }
        } catch (Exception e) {
            b = false;
            logger.error(e.getMessage(), e);
        }
        return b;
    }

    /**
     * <p>Description: 删除Rss</p>
     * @param newschannelmstrId
     * @return
     */
    public void deleteRss(long newschannelmstrId) {
        logger.info("RssService.deleteRss()" + new Date());
        String sql = "delete from Rss r where r.newschannelmstrId=" + newschannelmstrId + "";
        this.em.createQuery(sql).executeUpdate();
    }

    /**
     * <p>Description: 取得Rss资源</p>
     * @param source Rss地址
     * @param keywords 关键字
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<SyndEntry> getRssSource(String source, String keywords) {
        logger.info("RssService.getRssSource() source:" + source);
        List<SyndEntry> seList = new ArrayList<SyndEntry>();
        try {
            URL feedUrl = new URL(source);
            //设置超时 
            URLConnection uc = (HttpURLConnection) feedUrl.openConnection(); 
            uc.setConnectTimeout(3000);
            uc.setReadTimeout(3000);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(uc));
            List<SyndEntry> list = feed.getEntries();
            if (list.size() != 0) {
                for (SyndEntry syndEntry : list) {
                	if(syndEntry.getPublishedDate()==null){
                		syndEntry.setPublishedDate(new Date());
                	}
                }
                if (keywords != null && !"".equals(keywords.trim())) {
                    // 过滤关键字
                    SyndEntry se = null;
                    for (int i = 0; i < list.size(); i++) {
                        se = list.get(i);
                        if (se.getTitle().contains(keywords.trim())) {
                            seList.add(se);
                        }
                    }
                } else {
                    seList = list;
                }
            }
            logger.info("getRssSource success:" + seList.size());
        } catch (ConnectException ce) {
            logger.info("url:" + source);
            logger.error("连接超时", ce);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            logger.info("url:" + source);
        }
        return seList;
    }
    
    /**
     * <p>Description: 测试RSS资源是否正确</p>
     * @param source RSS资源地址
     * @return
     */
    public boolean testRssSource(String source) {
        try {
            URL feedUrl = new URL(source);
            HttpURLConnection uc = (HttpURLConnection) feedUrl.openConnection(); 
            uc.setConnectTimeout(3000);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            List<SyndEntry> list = feed.getEntries();
            if (null != list && list.size() != 0) { 
            	return true; 
            }
        } catch (ConnectException ce) {
            logger.error(ce.getMessage(), ce);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return false;
    }
    
    //copy from NewsManagerService
	public List<Newschannelmstr> getHomePageNewschannel(int num) {
		StringBuilder sb = new StringBuilder();
		sb.append("select n from Newschannelmstr n where n.defunctInd='N' order by n.priority");
		return em.createQuery(sb.toString()).setFirstResult(0).setMaxResults(num).getResultList();
	}
	
	/**
	 * <p>Description: 刷新所有10个频道的RSS数据</p>
	 */
	public void refreshAllRss() {
		List<Newschannelmstr> newsChannelList = this.getHomePageNewschannel(10);
		if (newsChannelList.size() != 0) {
		    for (Newschannelmstr channel : newsChannelList) {
		        refreshRssByChannel(channel);
            }
		}
	}
	
	/**
     * <p>Description: 批量删除某一些频道的Rss</p>
     * @param newschannelmstrIds
     * @return
     */
    public boolean deleteAllRss(List<Long> newschannelmstrIds) {
        boolean b = false;
        try {
            String sql = "delete from Rss r where r.newschannelmstrId in(:param)";
            this.em.createQuery(sql).setParameter("param", newschannelmstrIds).executeUpdate();
            b = true;
        } catch (Exception e) {
            b = false;
            logger.error(e.getMessage(), e);
        }
        return b;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean refreshRssByChannel(Newschannelmstr channel){
        logger.info("RssService.refreshRssByChannel() channel:" + channel.getId());
        boolean success = false ;
        try {
            List<SyndEntry> rssSource = getRssSource(channel.getRss(), channel.getKeywords());
            if(rssSource != null && rssSource.size() > 0){
                saveNewRssByChannel(channel, rssSource);
            }
            success = true;
            logger.info("refreshRssByChannel success:" + rssSource.size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return success;
    }
    
    public void saveNewRssByChannel(Newschannelmstr channel, List<SyndEntry> source){
        deleteRss(channel.getId());
        Rss r = null;
        logger.info("save rsss:" + new Date());
        for (SyndEntry se : source) {
            r = new Rss();
            r.setNewschannelmstrId(channel.getId());
            r.setLink(se.getLink());
            r.setTitle(se.getTitle());
            r.setPublishedDate(se.getPublishedDate());
            this.saveRss(r);
            logger.info("rss:" + r.getId());
        }
    }
    
}
