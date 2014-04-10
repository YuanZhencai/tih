/**
 * TihmQuestionResource.java
 * Created: 2013-1-15 上午10:50:38
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
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.wcs.common.controller.vo.DictVo;
import com.wcs.tih.interaction.controller.vo.SmartmstrVo;
import com.wcs.tih.model.Smartmstr;
import com.wcs.tihm.util.RestfulUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:chengchao@wcs-global.com">ChengChao</a>
 */
@Path("/questions")
@Stateless
public class TihmQuestionResource {
	
    @EJB
    private TihmQuestionService tihmQuestionService;
	/**
	 * <p>Description: 显示top10的问题</p>
	 * @return
	 */
	@GET
	@Path(value="/tops")
	@Produces("text/plain;charset=UTF-8")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getQuestionsTops() {
	    List<SmartmstrVo> questionsTops = tihmQuestionService.getQuestionsTops();
        return RestfulUtil.objectToJson(questionsTops);
	    
	}
	
	/**
	 * <p>Description: 获得所有税种列表</p>
	 * @return
	 */
	@GET
	@Path(value="/taxtypes")
	@Produces("text/plain;charset=UTF-8")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String getTaxTypes() {
	    List<DictVo> taxTypes = tihmQuestionService.getTaxTypes();
	    return RestfulUtil.objectToJson(taxTypes);
	}
	
	/**
	 * <p>Description: 按税种搜索 问题</p>
	 * @return
	 */
	@GET
	@Path(value="/taxtypes/{taxType}")
	@Produces("text/plain;charset=UTF-8")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String searchQuestionsByTaxType(@PathParam("taxType") String taxType) {
	    List<SmartmstrVo> serachResult = tihmQuestionService.searchQuestionsByTaxType(taxType);
	    return RestfulUtil.objectToJson(serachResult);
	}
	
	
	/**
	 * <p>Description: 按标题搜索 问题</p>
	 * @return
	 */
	@GET
	@Path(value="/title")
	@Produces("text/plain;charset=UTF-8")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String searchQuestionsByTitle(@QueryParam("search")String search) {
	    List<SmartmstrVo> serachResult = tihmQuestionService.searchQuestionsByTitle(search);
        return RestfulUtil.objectToJson(serachResult);
	}
	
}
