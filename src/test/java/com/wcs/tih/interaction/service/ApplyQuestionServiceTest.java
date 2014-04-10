/**
 * 
 */
package com.wcs.tih.interaction.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>Project: tih</p>
 * <p>Description: 提问申请单的单元测试</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:zhaoqian@wcs-global.com">zhaoqian</a>
 */
public class ApplyQuestionServiceTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * <p>Description:保存到草稿箱 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#saveToDraft(java.util.Map, java.lang.StringBuffer, java.lang.StringBuffer)}.
	 */
	@Test
	public void testSaveToDraft() {
		
	}

	/**
	 * <p>Description:更新草稿箱 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#updateToDraft(com.wcs.tih.model.WfInstancemstr, java.util.Map, java.lang.StringBuffer, java.lang.StringBuffer)}.
	 */
	@Test
	public void testUpdateToDraft() {
		
	}

	/**
	 * <p>Description: 删除草稿箱</p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#deleteWorkflowFromDraft(com.wcs.tih.model.WfInstancemstr, java.lang.StringBuffer)}.
	 */
	@Test
	public void testDeleteWorkflowFromDraft() {
		
	}

	/**
	 * <p>Description: 批量删除附件</p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#deleteBatchFileCE(java.util.List)}.
	 */
	@Test
	public void testDeleteBatchFileCE() {
		
	}

	/**
	 * <p>Description:删除单个附件</p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#deleteSingleFileCE(java.lang.String)}.
	 */
	@Test
	public void testDeleteSingleFileCE() {
		
	}

	/**
	 * <p>Description:添加附件 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#addFileCE(org.primefaces.model.UploadedFile)}.
	 */
	@Test
	public void testAddFileCE() {
		
	}

	/**
	 * <p>Description:下载附件 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#downloadFile(java.lang.String)}.
	 */
	@Test
	public void testDownloadFile() {
		
	}

	/**
	 * <p>Description:创建一个工作流程 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#createWorkFlow(boolean, com.wcs.tih.model.WfInstancemstr, java.util.Map, java.lang.StringBuffer, java.lang.StringBuffer, java.util.List)}.
	 */
	@Test
	public void testCreateWorkFlow() {
		
	}

	/**
	 * <p>Description:执行工作流程 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#workflowExecuteAction(com.wcs.tih.model.WfInstancemstr, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.StringBuffer, java.lang.StringBuffer, java.lang.String, java.util.Map)}.
	 */
	@Test
	public void testWorkflowExecuteAction() {
		
	}

	/**
	 * <p>Description:查询当前工作流的位置 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#queryNowWorkflowPlace(java.lang.String)}.
	 */
	@Test
	public void testQueryNowWorkflowPlace() {
		
	}

	/**
	 * <p>Description:判断转签人是否有拒绝操 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#queryAssignerHaveRefuse(java.lang.Long)}.
	 */
	@Test
	public void testQueryAssignerHaveRefuse() {
		
	}

	/**
	 * <p>Description: 保存到主表</p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#saveToWfIns(com.wcs.tih.model.WfInstancemstr)}.
	 */
	@Test
	public void testSaveToWfIns() {
		
	}

	/**
	 * <p>Description: 保存到主表属性表</p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#saveToWfInsPro(com.wcs.tih.model.WfInstancemstrProperty)}.
	 */
	@Test
	public void testSaveToWfInsPro() {
		
	}

	/**
	 * <p>Description:保存到步骤表 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#saveToStep(com.wcs.tih.model.WfStepmstr)}.
	 */
	@Test
	public void testSaveToStep() {
		
	}

	/**
	 * <p>Description:保存到步骤属性表 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#saveToStepPro(com.wcs.tih.model.WfStepmstrProperty)}.
	 */
	@Test
	public void testSaveToStepPro() {
		
	}

	/**
	 * <p>Description: 更新主表属性表</p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#updateWfInsProAll(com.wcs.tih.model.WfInstancemstrProperty)}.
	 */
	@Test
	public void testUpdateWfInsProAll() {
		
	}

	/**
	 * <p>Description:更新主表 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#updateWfInsAll(com.wcs.tih.model.WfInstancemstr)}.
	 */
	@Test
	public void testUpdateWfInsAll() {
		
	}

	/**
	 * <p>Description:查询最后一个步骤信息 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#queryLastWfsByWfIns(com.wcs.tih.model.WfInstancemstr)}.
	 */
	@Test
	public void testQueryLastWfsByWfIns() {
		
	}

	/**
	 * <p>Description:查询提问人 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#queryAsker(com.wcs.tih.model.WfInstancemstr)}.
	 */
	@Test
	public void testQueryAsker() {
		
	}

	/**
	 * <p>Description:根据主表查询主表属性表 </p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#queryWfInsProByWfIns(com.wcs.tih.model.WfInstancemstr, java.lang.String)}.
	 */
	@Test
	public void testQueryWfInsProByWfIns() {
		
	}

	/**
	 * <p>Description: 查询流程最后的处理人</p>
	 * Test method for {@link com.wcs.tih.interaction.service.ApplyQuestionService#queryLastChargedby(com.wcs.tih.model.WfInstancemstr)}.
	 */
	@Test
	public void testQueryLastChargedby() {
		
	}

}
