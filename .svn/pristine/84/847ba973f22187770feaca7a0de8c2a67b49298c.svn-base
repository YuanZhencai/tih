package com.wcs.tih.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.tih.homepage.contronller.vo.NewschannelmstrVo;
import com.wcs.tih.homepage.contronller.vo.RssVo;
import com.wcs.tih.model.Newschannelmstr;
import com.wcs.tih.model.Rss;

@Stateless
public class NewsManagerService {
	@PersistenceContext
	private EntityManager em;

	public List<Newschannelmstr> searchNews(Newschannelmstr news) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select n from Newschannelmstr n  where 1=1 ");
		if (news.getName() != null && !news.getName().trim().equals("")) {
			sb.append(" and n.name like '%").append(news.getName())
					.append("%'");
		}
		if (news.getDefunctInd() != null && !news.getDefunctInd().equals("")) {
			sb.append(" and n.defunctInd ='").append(news.getDefunctInd())
					.append("'");
		}
		if(news.getRss()!=null&& !news.getRss().trim().equals("")){
		    sb.append(" and n.rss like '%").append(news.getRss()) .append("%'");
		}
		return em.createQuery(sb.toString()).getResultList();
	}

	public void saveOrUpdateNews(Newschannelmstr news, boolean flag,String id) {
		if (flag) {
			this.em.merge(news);
		} else {
			news.setCreatedDatetime(new Date());
			news.setCreatedBy(id);
			this.em.persist(news); 
		}
	}
	public Integer countVidate(Newschannelmstr news){
		String sql="SELECT count(*) FROM NEWSCHANNELMSTR where defunct_ind <> 'Y'";
		if(news.getId()!=null){
			sql+=" and id != "+news.getId();
		}
		if(news.getDefunctInd().equals("N")){
		return (Integer)this.em.createNativeQuery(sql).getSingleResult();
		}else{
			return 0;
		}
	}
	
	public List<Newschannelmstr> getHomePageNewschannel(int num){
		StringBuilder sb = new StringBuilder();
		sb.append("select n from Newschannelmstr n where n.defunctInd='N' order by n.priority");
		return em.createQuery(sb.toString()).setFirstResult(0).setMaxResults(num).getResultList();
	}
	
	public List<NewschannelmstrVo> getNewschannels(int num){
	    NewschannelmstrVo ncv = null;
	    List<NewschannelmstrVo> newschannelmstrVos = new ArrayList<NewschannelmstrVo>();
	    List<Newschannelmstr> newschannels = this.getHomePageNewschannel(num);
	    for (Newschannelmstr nc : newschannels) {
	        ncv = new NewschannelmstrVo();
            ncv.setNewschannelmstr(nc);
            newschannelmstrVos.add(ncv);
        }
        return newschannelmstrVos;
	}
	
	public List<RssVo> getNewsListsByChannel(long channel){
	    StringBuffer sb = new StringBuffer();
        sb.append("select r from Rss r where r.newschannelmstrId=" + channel + " order by r.publishedDate desc");
        String sql = sb.toString();
        List<Rss> list = this.em.createQuery(sql).getResultList();
        List<RssVo> rssVos= new ArrayList<RssVo>();
        RssVo rssVo = null;
        for (Rss rss : list) {
            rssVo = new RssVo();
            rssVo.setRss(rss);
            rssVos.add(rssVo);
        }
        return rssVos;
	}
}
