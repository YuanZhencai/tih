/**
 * DummyResource.java
 * Created: 2011-11-28 下午11:52:38
 */
package com.wcs.tihm.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.tih.model.Smartmstr;

/**
 * <p>Project: mds</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2011 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:gaoyuxiang@wcs-global.com">Gao Yuxiang</a>
 */

@Path("/dummy/{uid}")
@Stateless
public class DummyResource {
     
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * <p>Description: </p>
     * @param uid : 用户名
     * @return
     */
    @GET
    @Produces("text/plain;charset=UTF-8")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String getDummy(@PathParam("uid") String uid) {
        try {
            logger.info("Received request for /dummy/"+uid);
			List<Smartmstr> list = new ArrayList<Smartmstr>();
			Smartmstr sm = new Smartmstr();
			sm.setId(new Long(1));
			Smartmstr sm2 = new Smartmstr();
			sm.setId(new Long(101));
			sm.setId(new Long(201));
			list.add(sm);
			list.add(sm2);
			return new ObjectMapper().writeValueAsString(list);
        } catch (Exception ex) {
            return "[]";
        }
    }
}
