/**
 * 
 */
package com.wcs.common.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>Project: tih</p>
 * <p>Description:公用Service的单元测试. </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:zhaoqian@wcs-global.com">zhaoqian</a>
 */
public class CommonServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * <p>Description: 系统加载时查询DICT表，将所有defunct_ind!='Y'的记录放到map,key=CODE_CAT+"."+CODE_KEY,value=CODE+VAL并放到application级别的bean中。</p>
	 * Test method for {@link com.wcs.common.service.CommonService#loadDict()}.
	 */
	@Test
	public void testLoadDict() {
	}

	/**
	 * <p>Description:一系列的取值,刷新时以及程序刚运行时调用这个方法 </p>
	 * Test method for {@link com.wcs.common.service.CommonService#queryDict()}.
	 */
	@Test
	public void testQueryDict() {
	}

	/**
	 * <p>Description:Description: 从application级别的bean获取该值，不要直接从数据库获取
	 * 这里是以d.getCodeCat()+"."+d.getCodeKey()来dictMap中获取相应的value值. </p>
	 * Test method for {@link com.wcs.common.service.CommonService#getValueByDictCatKey(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetValueByDictCatKey() {
	}

	/**
	 * <p>Description: 根据cat值获得所有的Dict列表</p>
	 * Test method for {@link com.wcs.common.service.CommonService#getDictByCat(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetDictByCat() {
	}

}
