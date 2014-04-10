package com.wcs.tih.interaction.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.service.CommonService;
import com.wcs.common.service.TDSLocal;
import com.wcs.common.util.JasperUtil;
import com.wcs.tih.document.service.DocumentService;
import com.wcs.tih.filenet.ce.service.CEserviceLocal;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.filenet.ce.util.MimeException;
import com.wcs.tih.filenet.helper.ce.ObjectStoreProvider;
import com.wcs.tih.filenet.helper.ce.util.CeConfigOptions;
import com.wcs.tih.filenet.model.DocVo;
import com.wcs.tih.filenet.pe.consts.WorkflowConsts;
import com.wcs.tih.interaction.controller.vo.SmartImportVO;
import com.wcs.tih.model.SmartAttachmentmstr;
import com.wcs.tih.model.Smartmstr;
import com.wcs.tih.model.WfInstancemstr;
import com.wcs.tih.model.WfInstancemstrProperty;
import com.wcs.tih.model.WfStepmstr;
import com.wcs.tih.model.WfStepmstrProperty;
import com.wcs.tih.util.CompressUtil;
import com.wcs.tih.util.DateUtil;

/** 
* <p>Project: tih</p> 
* <p>Title: SmartImportService.java</p> 
* <p>Description: </p> 
* <p>Copyright (c) 2014 Wilmar Consultancy Services</p>
* <p>All Rights Reserved.</p>
* @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a> 
*/
@Stateless
public class SmartImportService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext
	private EntityManager em;
	@EJB
	private LoginService loginService;
	@EJB
	private CEserviceLocal ceservice;
	@EJB
	private ApplyQuestionService applyQuestionService;
	@EJB
	private FileNetUploadDownload fileUpService;
	@EJB
	private DocumentService documentService;
	@EJB
	private CommonService commonService;
	@EJB 
	private TDSLocal tdsService;

	private static ResourceBundle rb = ResourceBundle.getBundle("filenet");
	private String userPassword = "user.password";
	private String folderMs = "ce.folder.mission";

	private static final String DOC_CLASS_NAME = CeConfigOptions.getString("ce.document.classid");
	private static final String SMART_EXPORT_FOLDER = CeConfigOptions.getString("ce.folder.smart.export");
	private static final String USER_PASSWORD = CeConfigOptions.getUserPassword();

	// ======================================Yuan=================================//
	public List<DocVo> findCompressFiles(Map<String, Object> filters) {
		List<DocVo> docVos = new ArrayList<DocVo>();
		try {
			ObjectStoreProvider provider = new ObjectStoreProvider(tdsService.addPre(this.loginService.getCurrentUserName()), USER_PASSWORD);
			StringBuilder whereClause = new StringBuilder();
			whereClause.append(" 1 = 1");
			whereClause.append(" and d.This INFOLDER '").append(SMART_EXPORT_FOLDER).append("'");

			List<Document> docs = provider.searchObjects(DOC_CLASS_NAME, whereClause.toString(), false, "DateCreated Desc");
			for (Document doc : docs) {
				DocVo docVo = new DocVo(doc);
				docVo.setCreator(tdsService.removePre(docVo.getCreator()));
				docVos.add(docVo);
			}
		} catch (Exception e) {
			this.logger.error("查找精灵历史出错", e);
		}
		return docVos;
	}

	@Asynchronous
	public Future<String> compressSmarts(String contextPath, String user) {
		List<Smartmstr> smarts = findSmarts();
		this.em.clear();
		compress(smarts, contextPath, user);
		return new AsyncResult<String>("1");
	}

	public List<Smartmstr> findSmarts() {
		StringBuilder jpql = new StringBuilder();
		jpql.append(" select s from Smartmstr s left join fetch s.smartAttachmentmstrs where s.defunctInd <> 'Y'");
		return this.em.createQuery(jpql.toString()).getResultList();
	}

	private void compress(List<Smartmstr> smarts, String contextPath, String user) {
		try {
			ObjectStoreProvider provider = new ObjectStoreProvider(tdsService.addPre(user), USER_PASSWORD);
			String zipFileName = "SmartAnwser";

			List<Map<String, InputStream>> smartAttachments = convertEntryMap(smarts, provider, zipFileName);

			String subRrportDir = new StringBuilder().append(contextPath).append("/faces/report/excel/jasper/").toString();

			File file = new File(new StringBuilder().append(new Date().getTime()).append(".xls").toString());
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("SUBREPORT_DIR", subRrportDir);
			JasperUtil.createXlsReport(new StringBuilder().append(subRrportDir).append("Smartmstr.jasper").toString(), smarts, new FileOutputStream(
					file), parameters);

			InputStream is = new FileInputStream(file);
			HashMap<String, InputStream> xlsMap = new HashMap<String, InputStream>();
			xlsMap.put(new StringBuilder().append(zipFileName).append("/SmartAnwser.xls").toString(), is);
			smartAttachments.add(xlsMap);

			String documentTitle = new StringBuilder().append(zipFileName).append(" ")
					.append(new SimpleDateFormat("yyyy-MM-dd-HHmmss").format(new Date())).toString();
			File zipFile = new File(new StringBuilder().append(documentTitle).append(".zip").toString());

			CompressUtil.zip(smartAttachments, new FileOutputStream(zipFile));

			file.delete();

			FileInputStream zipIs = new FileInputStream(zipFile);
			provider.createDocument(DOC_CLASS_NAME, documentTitle, null, SMART_EXPORT_FOLDER, "zip", zipIs);
			zipIs.close();
			zipFile.delete();
		} catch (Exception e) {
			this.logger.error(e.getMessage(), e);
		}
	}

	private List<Map<String, InputStream>> convertEntryMap(List<Smartmstr> smarts, ObjectStoreProvider provider, String zipFileName) {
		Map<String, String> attachDicts = this.commonService.getDictMapByCat("TIH.TAX.ATTACH.TYPE", "zh_CN");
		Map<String, String> taxDicts = this.commonService.getDictMapByCat("TIH.TAX.TYPE", "zh_CN");
		Map<String, String> regionDicts = this.commonService.getDictMapByCat("TIH.TAX.REGION", "zh_CN");

		List<Map<String, InputStream>> smartAttachments = new ArrayList<Map<String, InputStream>>();
		Long smartId = Long.valueOf(1L);
		for (Smartmstr smart : smarts) {
			smart.setId(smartId);
			smart.setTaxType((String) taxDicts.get(smart.getTaxType()));
			smart.setRegion((String) regionDicts.get(smart.getRegion()));
			List<SmartAttachmentmstr> attachmentmstrs = smart.getSmartAttachmentmstrs();
			List<SmartAttachmentmstr> effictiveAttachs = new ArrayList<SmartAttachmentmstr>();
			for (SmartAttachmentmstr sttachmentmstr : attachmentmstrs) {
				if ("N".equals(sttachmentmstr.getDefunctInd())) {
					String type = attachDicts.get(sttachmentmstr.getType());
					sttachmentmstr.setType(type);
					Map<String, InputStream> attachment = new HashMap<String, InputStream>();
					try {
						attachment = provider.getAttachment(sttachmentmstr.getFilemstrId());
					} catch (Exception e) {
						this.logger.debug(new StringBuilder().append("get attachment id ").append(sttachmentmstr.getFilemstrId()).append(" failed.")
								.toString(), e);
					}
					Map<String, InputStream> entryMap = new HashMap<String, InputStream>();
					for (String fileName : attachment.keySet()) {
						sttachmentmstr.setName(fileName);
						entryMap.put(new StringBuilder().append(zipFileName).append("/").append(smart.getId()).append("/").append(type).append("/")
								.append(fileName).toString(), attachment.get(fileName));
					}
					smartAttachments.add(entryMap);
					effictiveAttachs.add(sttachmentmstr);
				}
			}
			smart.setSmartAttachmentmstrs(effictiveAttachs);
			smartId++;
		}
		return smartAttachments;
	}

	// ======================================Yuan=================================//

	// ======================code:public方法都可以追加事务==================

	/**
	 * <p>
	 * Description:自动导入页面提交数据处理.
	 * </p>
	 * 
	 * @throws Exception
	 *             fileNet删除异常.
	 */
	public void autoImportSubmit(Map<String, String> autoImportQuestionMap, Map<String, String> autoImportReplyMap, String autoUsedDefunct,
			List<SmartImportVO> autoImportQuestionList, List<SmartImportVO> autoImportReplyList, List<String> autoImportQuestionNewAddFile,
			List<String> autoImportReplyNewAddFile) throws Exception {
		Smartmstr st = this.saveSmartMstrWithAutoImport(autoImportQuestionMap, autoImportReplyMap, autoUsedDefunct);
		// 附件有2个List,分别是autoImportQuestionList(循环添加提问的附件信息),autoImportReplyList(循环添加回复的附件信息)
		this.saveManySmartMstrAttWithAutoImport(autoImportQuestionList, st, DictConsts.TIH_TAX_ATTACH_TYPE_4);
		this.saveManySmartMstrAttWithAutoImport(autoImportReplyList, st, DictConsts.TIH_TAX_ATTACH_TYPE_5);
	}

	/**
	 * <p>
	 * Description: update页面.点击取消按钮的时候删除页面上的两个list里面的fileID对应的filenet上的文件.
	 * </p>
	 */
	public void btnCancel(List<String> tempA, List<String> tempB) {
		this.batchDelete(tempA);
		this.batchDelete(tempB);
	}

	public void addXhtmlSubmitData(Smartmstr smtAdd, String addPageDefunt, List<SmartImportVO> addQuestionList, List<SmartImportVO> addAnswerList) {
		Smartmstr smtAddNow = this.saveSmartMstrWithaddPageImportBtn(smtAdd, addPageDefunt);
		this.saveManySmartMstrAttWithAutoImport(addQuestionList, smtAddNow, DictConsts.TIH_TAX_ATTACH_TYPE_4);
		this.saveManySmartMstrAttWithAutoImport(addAnswerList, smtAddNow, DictConsts.TIH_TAX_ATTACH_TYPE_5);
	}

	/**
	 * <p>
	 * Description:update页面点击提交时候的操作.
	 * </p>
	 */
	public void updateXhtmlSumbitData(Smartmstr smt, String secondUsedDefunct, List<String> updteQueAttListDelete,
			List<String> updteRepAttListDelete, Map<String, List<SmartImportVO>> sqlAndSubmitMap) {
		// 执行更新操作
		this.updateSmartMstrWithHandImport(smt, secondUsedDefunct);
		// 在提交后,可以去fileNet上进行物理删除,先要删除掉放在deleteList里的数据.其数据不能存在于SqlList中.所以要弄一个真正可以删除的list集合.
		this.dedicatedBatchDelete(updteQueAttListDelete, sqlAndSubmitMap.get("QUESQL"));
		this.dedicatedBatchDelete(updteRepAttListDelete, sqlAndSubmitMap.get("REPSQL"));
		// 下面就是Map<String,List<SmartImportVO>> sqlAndSubmitMap里各个数据的对比.
		// B:对比updateQueAttListSubmit和 updateQueAttListSQL,Sumbit中不存在于SQL中的执行添加操作.反之不操作.
		this.submitDataCompareSQLData(sqlAndSubmitMap.get("QUESUB"), sqlAndSubmitMap.get("QUESQL"), DictConsts.TIH_TAX_ATTACH_TYPE_4, smt);
		this.submitDataCompareSQLData(sqlAndSubmitMap.get("REPSUB"), sqlAndSubmitMap.get("REPSQL"), DictConsts.TIH_TAX_ATTACH_TYPE_5, smt);
		// C:对比updteQueAttListDelete和updateQueAttListSQL,delete中存在于SQL中的执行更新defunt="N"为"Y".反之不操作.
		this.submitDataCompareDeleteData(sqlAndSubmitMap.get("QUESQL"), updteQueAttListDelete);
		this.submitDataCompareDeleteData(sqlAndSubmitMap.get("REPSQL"), updteRepAttListDelete);
	}

	// sqlList中的fileId为不可删,避免提问流程中的附件被删除.提交专用.
	private void dedicatedBatchDelete(List<String> fakeDeleteList, List<SmartImportVO> sqlList) {
		List<String> tempList = new ArrayList<String>();
		List<String> sqlFileIdList = new ArrayList<String>();
		for (SmartImportVO vo : sqlList) {
			sqlFileIdList.add(vo.getFileIdRF());
		}
		for (String fakeSqlId : fakeDeleteList) {
			if (!sqlFileIdList.contains(fakeSqlId)) {
				tempList.add(fakeSqlId);
			}
		}
		if (tempList.size() < 1) {
			// 没有要删除的东西
		} else {
			this.deleteBatchFileCE(tempList);
		}
	}

	// submit中有,但sql里没有,执行添加操作.提交专用方法体
	private void submitDataCompareSQLData(List<SmartImportVO> submitList, List<SmartImportVO> sqlList, String smartAttType, Smartmstr smt) {
		List<String> temp = new ArrayList<String>();
		for (SmartImportVO vo : sqlList) {
			String fileId = vo.getFileIdRF();
			temp.add(fileId);
		}
		for (SmartImportVO vo : submitList) {
			String fileId = vo.getFileIdRF();
			if (fileId != null && !temp.contains(fileId)) {
				// 如果不包含这个id,证明原数据库中是没有的,执行添加操作!
				this.saveSingleSmartMstrAtt(smt, fileId, vo.getFileNameRF(), smartAttType);
			}
		}
	}

	// delete中有,但sql中也有,那就N-->Y,提交专用方法体
	private void submitDataCompareDeleteData(List<SmartImportVO> sqlList, List<String> deleteList) {
		for (SmartImportVO vo : sqlList) {
			String fileId = vo.getFileIdRF();
			if (deleteList.contains(fileId)) {
				// 如果deleteList中包含这个ID,那么数据库需要更新这个ID的行数据N为Y
				this.updateSmartAttDufunctToY(fileId);
			}
		}
	}

	// ==================PE AND CE================

	/**
	 * <p>
	 * Description: 删除单个文件
	 * </p>
	 * 
	 * @param fileId
	 *            文件ID
	 * @throws Exception
	 *             连接FileNet CE异常
	 */
	public void deleteSingleFile(String fileId) throws Exception {
		ceservice.connect(loginService.getCurrentUserName(), rb.getString(userPassword));
		Document d = ceservice.getDocument(fileId);
		d.delete();
		d.save(RefreshMode.REFRESH);
	}

	/**
	 * <p>
	 * Description: 批量删除文件
	 * </p>
	 * 
	 * @param list
	 *            文件Id集合
	 */
	public void deleteBatchFileCE(List<String> list) {
		applyQuestionService.deleteBatchFileCE(list);
	}

	private void batchDelete(List<String> tempList) {
		if (tempList.size() < 1) {
			// 没有要删除的东西
		} else {
			this.deleteBatchFileCE(tempList);
		}
	}

	/**
	 * <p>
	 * Description:上传文档
	 * </p>
	 * 
	 * @param upFile
	 *            上传的对象
	 * @return Document对象
	 * @throws MimeException
	 *             文档类型不正确
	 * @throws Exception
	 *             上传失败
	 */
	public Document addFileCE(UploadedFile upFile) throws MimeException, Exception {
		logger.info("上传Service!");
		String fileName = upFile.getFileName();
		InputStream inputStream;
		com.filenet.api.core.Document document;
		inputStream = upFile.getInputstream();
		String folder = rb.getString("ce.folder.smart");
		String tihDoc = rb.getString("ce.document.classid");
		document = fileUpService.upLoadDocumentCheckIn(inputStream, new HashMap<String, Object>(), fileName, tihDoc, folder);
		logger.info("上传Service!22222222");
		return document;

	}

	/**
	 * <p>
	 * Description: 下载文档
	 * </p>
	 * 
	 * @param fileId
	 *            文件ID.
	 * @return 内容流.文件.
	 * @throws MimeException
	 *             下载引擎异常/无法找到文件
	 * @throws Exception
	 *             下载出现异常
	 */
	public StreamedContent downloadFile(String fileId) throws MimeException, Exception {
		return applyQuestionService.downloadFile(fileId);
	}

	// =================
	/**
	 * <p>
	 * Description: 保存到精灵主表,自动导入页面
	 * </p>
	 * 
	 * @param autoImportQuestionMap
	 * @param autoImportReplyMap
	 * @param autoUsedDefunct
	 * @return
	 */
	public Smartmstr saveSmartMstrWithAutoImport(Map<String, String> autoImportQuestionMap, Map<String, String> autoImportReplyMap,
			String autoUsedDefunct) {
		Smartmstr st = new Smartmstr();
		st.setTaxType(autoImportQuestionMap.get("TAXTYPE")); // 税种
		st.setRegion(autoImportQuestionMap.get("REGION")); // 地域
		st.setName(autoImportQuestionMap.get("QUESTIONHEAD")); // 标题
		st.setQuestion(autoImportQuestionMap.get("QUESTIONMORE")); // 问题
		st.setAnswer(autoImportReplyMap.get("ANSWER")); // 回答
		st.setDefunctInd(autoUsedDefunct);
		st.setCreatedBy(loginService.getCurrentUserName());
		st.setCreatedDatetime(new Date());
		st.setUpdatedBy(loginService.getCurrentUserName());
		st.setUpdatedDatetime(new Date());
		// 这里存的是最后加上去的外键Id,可以为空.为0.这里是自动导入,所以肯定有值.
		st.setWfInstancemstrId(Long.parseLong(autoImportQuestionMap.get("WFINSID")));
		this.saveSmartMstr(st);
		return st;
	}

	/**
	 * <p>
	 * Description: 手动导入页面保存到精灵主表
	 * </p>
	 * 
	 * @param smtAdd
	 *            Smartmstr对象
	 * @param addPageDefunt
	 * @return
	 */
	public Smartmstr saveSmartMstrWithaddPageImportBtn(Smartmstr smtAdd, String addPageDefunt) {
		smtAdd.setDefunctInd(addPageDefunt);
		smtAdd.setCreatedBy(loginService.getCurrentUserName());
		smtAdd.setCreatedDatetime(new Date());
		smtAdd.setUpdatedBy(loginService.getCurrentUserName());
		smtAdd.setUpdatedDatetime(new Date());
		smtAdd.setWfInstancemstrId(0); // 如果不是导入就填充0,jpql操作,默认也一样给0
		this.saveSmartMstr(smtAdd);
		return smtAdd;
	}

	/**
	 * <p>
	 * Description: 保存单个的文件信息到精灵附件表
	 * </p>
	 * 
	 * @param smt
	 * @param fileId
	 * @param fileName
	 * @param type
	 */
	public void saveSingleSmartMstrAtt(Smartmstr smt, String fileId, String fileName, String type) {
		SmartAttachmentmstr sta = new SmartAttachmentmstr();
		sta.setFilemstrId(fileId);
		sta.setName(fileName);
		sta.setSmartmstr(smt);
		sta.setType(type);
		sta.setDefunctInd("N");
		sta.setCreatedBy(loginService.getCurrentUserName());
		sta.setCreatedDatetime(new Date());
		sta.setUpdatedBy(loginService.getCurrentUserName());
		sta.setUpdatedDatetime(new Date());
		this.saveSmartMstrAtt(sta);
	}

	/**
	 * <p>
	 * Description:批量保存附件信息到精灵附件表
	 * </p>
	 * 
	 * @param manyDataList
	 *            可能为空.
	 * @param st
	 * @param attType
	 */
	public void saveManySmartMstrAttWithAutoImport(List<SmartImportVO> manyDataList, Smartmstr st, String attType) {
		if (null != manyDataList && manyDataList.size() != 0) {
			for (int i = 0; i < manyDataList.size(); i++) {
				this.saveSingleSmartMstrAtt(st, manyDataList.get(i).getFileIdRF(), manyDataList.get(i).getFileNameRF(), attType);
			}
		}
	}

	/**
	 * <p>
	 * Description: 手动导入页面的更新精灵主表
	 * </p>
	 */
	public void updateSmartMstrWithHandImport(Smartmstr smt, String secondUsedDefunct) {
		smt.setDefunctInd(secondUsedDefunct);
		smt.setUpdatedBy(loginService.getCurrentUserName());
		smt.setUpdatedDatetime(new Date());
		// 执行更新操作
		this.updateSmartMstr(smt);
	}

	/**
	 * <p>
	 * Description: 删除无效的文件信息,实际更新精灵附件表N为Y
	 * </p>
	 */
	public void updateSmartAttDufunctToY(String fileId) {
		SmartAttachmentmstr sta = this.querySmAttByFileId(fileId);
		sta.setDefunctInd("Y");
		sta.setUpdatedBy(loginService.getCurrentUserName());
		sta.setUpdatedDatetime(new Date());
		this.updateSmartMstrAtt(sta);
	}

	// 查询index页面点击自动导入后显示的dataTable TIH.TAX.WORKFLOWSTATUS.3 TIH_TAX_WORKFLOWSTATUS_3
	@SuppressWarnings("unchecked")
	public List<SmartImportVO> queryApplyQuestion(String questionHead) {
		List<SmartImportVO> result = new ArrayList<SmartImportVO>();
		String jpql = "SELECT DISTINCT w.id,wp.value,sp.value,w.no,w.submitDatetime FROM WfInstancemstr w,WfInstancemstrProperty wp,WfStepmstr s,WfStepmstrProperty sp "
				+ " WHERE w.type='"
				+ DictConsts.TIH_TAX_REQUESTFORM_3
				+ "' "
				+ " AND w.status='"
				+ DictConsts.TIH_TAX_WORKFLOWSTATUS_3
				+ "' "
				+ " AND w.defunctInd = 'N' "
				+ " AND w.id=wp.wfInstancemstr.id "
				+ " AND wp.name='"
				+ WorkflowConsts.TIH_WORKFLOW_APPLYQUE_QUESTIONHEAD
				+ "' "
				+ " AND s.wfInstancemstr.id=w.id"
				+ " AND s.code ='0' "
				+ " AND sp.wfStepmstr.wfInstancemstr.id=w.id "
				+ " AND sp.wfStepmstr.id = s.id  "
				+ " AND sp.name='"
				+ WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION + "'  ";
		if (questionHead != null && !questionHead.trim().equals("")) {
			jpql = jpql + " AND wp.value LIKE '%" + questionHead + "%' ";
		}
		logger.info("queryApplyQuestion jpql:" + jpql);
		List<Object[]> dataList = em.createQuery(jpql).getResultList();

		String jpqlSecond = "SELECT DISTINCT s.wfInstancemstrId from Smartmstr s order by s.id ";
		List<String> idList = em.createQuery(jpqlSecond).getResultList();
		logger.info("idList.size:" + idList.size());
		for (Object[] obj : dataList) {
			if (idList.contains(obj[0])) {
				// id, firstHead, firstMore, workflowNumber, createTime, isImport
				result.add(new SmartImportVO((Long) obj[0], obj[1].toString(), obj[2].toString(), obj[3].toString(), (Date) obj[4], "已导入"));
			} else {
				result.add(new SmartImportVO((Long) obj[0], obj[1].toString(), obj[2].toString(), obj[3].toString(), (Date) obj[4], "未导入"));
			}
		}
		return result;
	}

	// =========================index dataTable
	@SuppressWarnings("unchecked")
	public List<SmartImportVO> queryQuestion(Smartmstr st, Date beginTime, Date endTime) {
		// 查询有自动导入的List
		StringBuilder sb = new StringBuilder();
		sb.append("select  NEW com.wcs.tih.interaction.controller.vo.SmartImportVO(s,w) "
				+ "from Smartmstr s,WfInstancemstr w where s.wfInstancemstrId=w.id ");
		sb = this.stNotNull(st, sb, beginTime, endTime);
		List<SmartImportVO> li = em.createQuery(sb.toString()).getResultList();
		logger.info("li1.size:" + li.size());
		logger.info("sb1:" + sb.toString());
		// 查询有手动导入的List
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		sb3.append("s.wfInstancemstrId<>0 and s.wfInstancemstrId not in(select w.id from WfInstancemstr w)");
		sb3 = this.stNotNull(st, sb3, beginTime, endTime);
		sb2.append("select DISTINCT NEW com.wcs.tih.interaction.controller.vo.SmartImportVO(s) "
				+ "from Smartmstr s,WfInstancemstr w where s.wfInstancemstrId=0  ");
		sb2 = this.stNotNull(st, sb2, beginTime, endTime);
		sb2.append("or (" + sb3.toString() + ")");
		List<SmartImportVO> li2 = em.createQuery(sb2.toString()).getResultList();
		logger.info("li2.size:" + li2.size());
		logger.info("sb2:" + sb2.toString());
		List<SmartImportVO> smartImportVos = new ArrayList<SmartImportVO>();
		// 合并
		if (!li.isEmpty()) {
			smartImportVos.addAll(li);
		}
		if (!li2.isEmpty()) {
			smartImportVos.addAll(li2);
		}

		Collections.sort(smartImportVos, new Comparator<SmartImportVO>() {
			@Override
			public int compare(SmartImportVO o1, SmartImportVO o2) {
				return o1.getS().getId().compareTo(o2.getS().getId());
			}
		});
		logger.info("IlistLast.size:" + smartImportVos.size());

		return smartImportVos;
	}

	private StringBuilder stNotNull(Smartmstr st, StringBuilder sb, Date beginTime, Date endTime) {
		if (st != null) {
			if (st.getQuestion() != null && !st.getQuestion().trim().equals("")) {
				sb.append(" and s.question like '%").append(st.getQuestion()).append("%'");
			}
			if (st.getAnswer() != null && !st.getAnswer().trim().equals("")) {
				sb.append(" and s.answer like '%").append(st.getAnswer()).append("%'");
			}
			if (st.getTaxType() != null && !st.getTaxType().equals("")) {
				sb.append(" and s.taxType = '").append(st.getTaxType()).append("'");
			}
			if (st.getRegion() != null && !st.getRegion().equals("")) {
				sb.append(" and s.region ='").append(st.getRegion()).append("'");
			}
			if (st.getName() != null && !st.getName().trim().equals("")) {
				sb.append(" and s.name  like '%").append(st.getName()).append("%'");
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (beginTime != null) {
			sb.append(" and s.createdDatetime >= '" + sdf.format(beginTime) + " 00:00:00'");
		}
		if (endTime != null) {
			sb.append(" and s.createdDatetime <= '" + sdf.format(DateUtil.getNextDate(endTime)) + " 00:00:00'");
		}
		return sb;
	}

	// 根据ID查出本条wfInstancemstr记录
	@SuppressWarnings("rawtypes")
	public WfInstancemstr queryWfInsAutoById(Long id) {
		String jpql = "select w from WfInstancemstr w where w.id=:id";
		List list = this.em.createQuery(jpql).setParameter("id", id).getResultList();
		return (WfInstancemstr) list.get(0);
	}

	// 根据wfins的ID来获取一个一表属性表list,然后分批装入MAP中去
	@SuppressWarnings("unchecked")
	public Map<String, String> queryWfInsProData(WfInstancemstr wfIns) {
		Map<String, String> maps = new HashMap<String, String>();
		String jpql = "Select wp from WfInstancemstrProperty wp where wp.wfInstancemstr.id=" + wfIns.getId() + " order by wp.id ";
		List<WfInstancemstrProperty> list = this.em.createQuery(jpql).getResultList();
		for (int i = 0; i < list.size(); i++) {
			String nameB = list.get(i).getName();
			String valueA = list.get(i).getValue();
			maps.put(nameB, valueA);
		}
		return maps;
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryStepmstrProFirstData(String workflowNumber) {
		Map<String, String> maps = new HashMap<String, String>();
		String sql = " select s from WfStepmstr s where s.wfInstancemstr.no='" + workflowNumber + "' order by s.id";
		List<WfStepmstr> wfs = this.em.createQuery(sql).getResultList();
		List<WfStepmstrProperty> wfsProList = wfs.get(0).getWfStepmstrProperties();// 取第一条,id最小的.
		for (WfStepmstrProperty wfsp : wfsProList) {
			logger.info("1.name:" + wfsp.getName());
			logger.info("2.value:" + wfsp.getValue());
			if ((WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION).equals(wfsp.getName())) {
				logger.info("wfsp.getValue():" + wfsp.getValue());
				maps.put(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_OPIONION, wfsp.getValue());
			} else if ((WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID).equals(wfsp.getName())) {
				logger.info("wfsp.getValue():" + wfsp.getValue());
				maps.put(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILEID, wfsp.getValue());
			} else {
				logger.info("wfsp.getValue():" + wfsp.getValue());
				maps.put(WorkflowConsts.TIH_WORKFLOW_APPLYQUE_FILENAME, wfsp.getValue());
			}
		}
		return maps;
	}

	/**
	 * @param workflowNumber
	 * @return 获取除第一步LaunchStep外的所有步骤信息
	 */
	@SuppressWarnings("unchecked")
	public List<WfStepmstr> queryStepmstr(String workflowNumber) {
		String sql = " select s from WfStepmstr s where s.wfInstancemstr.no='" + workflowNumber + "' and s.fromStepId <> 0 order by s.id";
		return this.em.createQuery(sql).getResultList();
	}

	/**
	 * 保存到精灵主表
	 * 
	 * @param st
	 */
	public void saveSmartMstr(Smartmstr st) {
		em.persist(st);
	}

	/**
	 * 保存到精灵附件表
	 * 
	 * @param statt
	 */
	public void saveSmartMstrAtt(SmartAttachmentmstr stAtt) {
		em.persist(stAtt);
	}

	/**
	 * 根据ID查找出一个Smartmstr对象
	 * 
	 * @param id
	 * @return
	 */
	public Smartmstr querySmtById(long id) {
		String jpql = "select s from Smartmstr s where s.id=" + id + " ";
		Smartmstr smt = (Smartmstr) this.em.createQuery(jpql).getResultList().get(0);
		em.clear();
		return smt;
	}

	/**
	 * @param id
	 *            关联表smartmstr的主键
	 * @return 一个List<SmartImportVO>
	 */
	@SuppressWarnings("unchecked")
	public List<SmartImportVO> querySmtAttByIdAndType(long id, String type) {

		String jpql = "select st from SmartAttachmentmstr st where st.smartmstr.id=" + id + " and st.type='" + type + "'  and st.defunctInd='N'";
		List<SmartAttachmentmstr> list = this.em.createQuery(jpql).getResultList();
		List<SmartImportVO> result = new ArrayList<SmartImportVO>();
		for (int i = 0; i < list.size(); i++) {
			result.add(new SmartImportVO((long) i, list.get(i).getFilemstrId(), list.get(i).getName()));
		}
		return result;
	}

	// 更新精灵主表
	public void updateSmartMstr(Smartmstr st) {
		em.merge(st);
	}

	// 更新精灵附件表
	public void updateSmartMstrAtt(SmartAttachmentmstr stAtt) {
		em.merge(stAtt);
	}

	// 根据fileNet文件的fileId来查找对象
	public SmartAttachmentmstr querySmAttByFileId(String fileId) {
		String jpql = "select st from SmartAttachmentmstr st where st.filemstrId='" + fileId + "'";
		return (SmartAttachmentmstr) this.em.createQuery(jpql).getResultList().get(0);
	}

	// 文档拷贝
	public String copyDocument(String id) throws Exception {
		logger.info("老附件ID为:" + id);
		Document d = documentService.copyDocument(id, rb.getString(folderMs));
		String newid = d.get_Id().toString();
		logger.info("新附件ID为:" + newid);
		return newid;
	}
}
