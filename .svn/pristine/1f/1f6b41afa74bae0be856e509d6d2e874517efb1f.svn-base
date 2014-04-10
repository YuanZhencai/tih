package com.wcs.tihm.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.wcs.tih.homepage.contronller.vo.NewschannelmstrVo;
import com.wcs.tih.homepage.contronller.vo.RssVo;
import com.wcs.tih.system.service.NewsManagerService;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class TihmNewsService {
    @EJB
    private NewsManagerService newsManagerService;

    public List<NewschannelmstrVo> getNewsChannels() {
        return newsManagerService.getNewschannels(10);
    }
    
    public List<RssVo> getNewsListsByChannel(long channel){
        return newsManagerService.getNewsListsByChannel(channel);
    }

}
