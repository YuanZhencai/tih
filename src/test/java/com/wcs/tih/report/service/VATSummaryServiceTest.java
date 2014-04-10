/**
 * 
 */
package com.wcs.tih.report.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>Project: tih</p>
 * <p>Description: 增值税进项税额抵扣汇总的单元测试</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:zhaoqian@wcs-global.com">zhaoqian</a>
 */
public class VATSummaryServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * <p>Description: 查询出VAT主表的所有的公司名,公司ID,去掉重复的. </p>
	 * Test method for {@link com.wcs.tih.report.service.VATSummaryService#queryData(java.util.Date, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testQueryData() {
	}

	/**
	 * <p>Description:准备好excel的数据 </p>
	 * Test method for {@link com.wcs.tih.report.service.VATSummaryService#prepareExcelData(com.wcs.tih.report.controller.vo.VATSummaryVO[], java.lang.String, java.util.Date)}.
	 */
	@Test
	public void testPrepareExcelData() {
	}

	/**
	 * <p>Description:下载vat的excel </p>
	 * Test method for {@link com.wcs.tih.report.service.VATSummaryService#downVATExcel(java.util.Map, java.util.Date, java.lang.String)}.
	 */
	@Test
	public void testDownVATExcel() {
	}

	/**
	 * <p>Description:查询vat统计信息 </p>
	 * Test method for {@link com.wcs.tih.report.service.VATSummaryService#queryVATStatisticalInfo()}.
	 */
	@Test
	public void testQueryVATStatisticalInfo() {
	}

	/**
	 * <p>Description: 下载文件</p>
	 * Test method for {@link com.wcs.tih.report.service.VATSummaryService#downloadFile(java.lang.String)}.
	 */
	@Test
	public void testDownloadFile() {
	}

	/**
	 * <p>Description:根据vatId查询文件id </p>
	 * Test method for {@link com.wcs.tih.report.service.VATSummaryService#queryFileIdByVatId(long)}.
	 */
	@Test
	public void testQueryFileIdByVatId() {
	}

	/**
	 * <p>Description:根据vatId查询文件名 </p>
	 * Test method for {@link com.wcs.tih.report.service.VATSummaryService#queryFileNameByVatId(long)}.
	 */
	@Test
	public void testQueryFileNameByVatId() {
	}

	/**
	 * <p>Description: 根据公司ID查找vatId</p>
	 * Test method for {@link com.wcs.tih.report.service.VATSummaryService#queryVatIdByCompanyId(long, java.lang.String, java.util.Date)}.
	 */
	@Test
	public void testQueryVatIdByCompanyId() {
	}

}
