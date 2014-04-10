/**
 * TihmNewsResource.java
 * Created: 2013-1-15 上午10:32:53
 */
package com.wcs.tihm.service;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wcs.tih.homepage.contronller.vo.NewschannelmstrVo;
import com.wcs.tih.homepage.contronller.vo.RssVo;
import com.wcs.tih.model.Rss;
import com.wcs.tihm.util.RestfulUtil;


/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:chengchao@wcs-global.com">ChengChao</a>
 */
@Path("/news")
@Stateless
public class TihmNewsResource {
    @EJB
    private TihmNewsService tihmNewsService;
	
	/**
	 * <p>Description: 获得新闻频道</p>
	 * @return
	 */
	@GET
	@Path(value="/channels")
	@Produces("text/plain;charset=UTF-8")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getNewsChannels() {
	    List<NewschannelmstrVo> newsChannels = tihmNewsService.getNewsChannels();
        return RestfulUtil.objectToJson(newsChannels);
	}

	/**
	 * <p>Description: 按照新闻频道显示新闻列表</p>
	 * @param channelId
	 * @return
	 */
	@GET
	@Path(value="/channels/{id}/lists")
	@Produces("text/plain;charset=UTF-8")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getNewsListsByChannel(@PathParam("id") long channelId) {
	    List<RssVo> news = tihmNewsService.getNewsListsByChannel(channelId);
	    return RestfulUtil.objectToJson(news);
	}
	
}
