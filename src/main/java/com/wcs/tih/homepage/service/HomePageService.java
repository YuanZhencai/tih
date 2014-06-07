package com.wcs.tih.homepage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.filenet.api.core.Document;
import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.model.Resourcemstr;
import com.wcs.tih.filenet.ce.service.FileNetUploadDownload;
import com.wcs.tih.homepage.contronller.vo.LearningGardenAndCommonDataVo;
import com.wcs.tih.homepage.contronller.vo.NewschannelmstrVo;
import com.wcs.tih.homepage.contronller.vo.NoticeVo;
import com.wcs.tih.model.Newschannelmstr;
import com.wcs.tih.model.NotificationReceiver;
import com.wcs.tih.model.Rss;
import com.wcs.tih.system.controller.vo.CommonFunctionVO;
import com.wcs.tih.system.controller.vo.CommonLinkVO;
import com.wcs.tih.system.service.CommonFunctionService;
import com.wcs.tih.system.service.CommonLinkService;
import com.wcs.tih.system.service.NewsManagerService;
import com.wcs.tih.transaction.controller.vo.TaskSearchVo;
import com.wcs.tih.transaction.controller.vo.WfInstancemstrVo;
import com.wcs.tih.transaction.service.TaskManagementService;

@Stateless
public class HomePageService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;
    @EJB
    private TaskManagementService taskManagementService;
    @EJB
    private NewsManagerService newsManagerService;
    @EJB
    private CommonLinkService commonLinkService;
    @EJB
    private CommonFunctionService commonFunctionService;
    @EJB
    private FileNetUploadDownload fileNetUploadDownload;
    private static ResourceBundle filenetProperties = ResourceBundle.getBundle("filenet");
    private final static String ERROR_PAGE = "/pageauthority.xhtml";
    
	// =======================================Yuan========================================//

	public void setReadAll() {
		Date date = new Date();
		String userName = loginService.getCurrentUserName();
		StringBuilder jpql = new StringBuilder();
		jpql.append(" update NotificationReceiver r");
		jpql.append(" set r.readInd = 'N'");
		jpql.append(" ,r.readDate = :date");
		jpql.append(" ,r.updatedBy = :userName");
		jpql.append(" ,r.updatedDatetime = :date");
		jpql.append(" where r.readInd = 'Y'");
		jpql.append(" and r.receivedBy = :userName");
		jpql.append(" and r.notificationSender.sendOption = '" + DictConsts.TIH_TAX_MSG_TYPE_2 + "'");
		em.createQuery(jpql.toString()).setParameter("date", date).setParameter("userName", userName).executeUpdate();
	}

	// =======================================Yuan========================================//

    /**
     * <p>Description: 加载我的消息</p>
     * @param readFlag 消息阅读标识
     * @return 消息Vo集合
     */
    public List<NoticeVo> loadNotices(String readFlag) {
        String sql = "select nr from NotificationReceiver nr where nr.readInd='" + readFlag + "' and nr.notificationSender.sendOption = '" + DictConsts.TIH_TAX_MSG_TYPE_2 + "' and nr.receivedBy ='" + this.loginService.getCurrentUserName() + "' order by nr.notificationSender.sendDatetime desc";
        List<NotificationReceiver> notificationReceivers = this.em.createQuery(sql).setMaxResults(20).getResultList();
        List<NoticeVo> noticeVos = new ArrayList<NoticeVo>();
        if (notificationReceivers != null && notificationReceivers.size() != 0) {
            NoticeVo noticeVo = null;
            for (NotificationReceiver notificationReceiver : notificationReceivers) {
                noticeVo = new NoticeVo();
                noticeVo.setReceiver(notificationReceiver);
                noticeVos.add(noticeVo);
            }
        }
        return noticeVos;
    }

    /**
     * <p>Description: 加载我的待处理任务</p>
     * @return 任务实体Vo集合
     */
    public List<WfInstancemstrVo> loadMyWaitDealWithTask(String browserLang) {
        return taskManagementService.getMyWaitTask(browserLang,new TaskSearchVo());
    }

    /**
     * <p>Description: 加载新闻频道</p>
     * @return 新闻频道Vo集合
     */
    public List<NewschannelmstrVo> loadNewsChannel() {
        List<NewschannelmstrVo> newschannelmstrVos = new ArrayList<NewschannelmstrVo>();
        List<Newschannelmstr> newsChannelList = newsManagerService.getHomePageNewschannel(10);
        if (null != newsChannelList && newsChannelList.size() != 0) {
            NewschannelmstrVo ncv = null;
            List<Rss> rssList = null;
            for (Newschannelmstr nc : newsChannelList) {
                ncv = new NewschannelmstrVo();
                rssList = getAllRss(nc.getId());
                ncv.setNewschannelmstr(nc);
                ncv.setRssList(rssList);
                newschannelmstrVos.add(ncv);
            }
        }
        return newschannelmstrVos;
    }

    /**
     * <p>Description: 加载常用链接</p>
     * @return 常用链接Vo集合
     */
    public List<CommonLinkVO> loadCommonLink() {
        return commonLinkService.searchData(null, "N");
    }

    /**
     * <p>Description: 加载常用功能</p>
     * @return 常用功能Vo集合
     */
    public List<CommonFunctionVO> loadCommonFunction() {
        List<CommonFunctionVO> commonFunctionList = new ArrayList<CommonFunctionVO>();
        List<CommonFunctionVO> cfvList = commonFunctionService.queryData(null);
        List<Resourcemstr> rList = loginService.getCurrentResources();
        if (cfvList.size() != 0 && rList.size() != 0) {
            CommonFunctionVO cfv = null;
            for (int i = 0; i < cfvList.size(); i++) {
                cfv = cfvList.get(i);
                for (Resourcemstr r : rList) {
                    if ((long) cfv.getRid() == (long) r.getId()) {
                        commonFunctionList.add(cfv);
                        break;
                    }
                }
            }
        }
        return commonFunctionList;
    }

    /**
     * <p>Description: 加载学习园地</p>
     * @return 
     */
    public List<LearningGardenAndCommonDataVo> loadLearningGarden() {
        String parentName = filenetProperties.getString("ce.folder.study");
        return getFileByFileFolderName(parentName);
    }

    /**
     * <p>Description: 加载常用资料</p>
     * @return
     */
    public List<LearningGardenAndCommonDataVo> loadCommonData() {
        String parentName = filenetProperties.getString("ce.folder.reference");
        return getFileByFileFolderName(parentName);
    }

    /**
     * <p>Description: 从FileNet上取得某个文件夹的文件</p>
     * @param name 文件夹名
     * @return
     */
    private List<LearningGardenAndCommonDataVo> getFileByFileFolderName(String name) {
        List<LearningGardenAndCommonDataVo> learningGardenAndCommonDataVos = new ArrayList<LearningGardenAndCommonDataVo>();
        try {
            List<Document> dList = fileNetUploadDownload.getDocsInThisFolder(name);
            LearningGardenAndCommonDataVo lgv = null;
            Document d = null;
            long id = 0;
            for (int i = 0; i < dList.size(); i++) {
                d = dList.get(i);
                lgv = new LearningGardenAndCommonDataVo();
                id = (i + 1);
                lgv.setId(id);
                lgv.setDocumentId(d.get_Id().toString());
                lgv.setName(d.get_Name());
                learningGardenAndCommonDataVos.add(lgv);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return learningGardenAndCommonDataVos;
    }

    /**
     * <p>Description: 通过资源的ID取得资源的路径</p>
     * @param id 资源的ID
     * @return
     */
    public String getUri(long id) {
        String uri = ERROR_PAGE;
        List<Resourcemstr> resourcemstrs = loginService.getCurrentResources();
        // 判断是否有页面权限
        if (resourcemstrs != null && resourcemstrs.size() != 0) {
            for (Resourcemstr r : resourcemstrs) {
                if ((long) r.getId() == (long) id) {
                    uri = r.getUri();
                    break;
                }
            }
        }
        return uri;
    }

    /**
     * <p>Description: 从FileNet上下载指定的文件</p>
     * @param documentId 文件的ID
     * @return
     */
    public StreamedContent download(String documentId) {
        try {
            return fileNetUploadDownload.downloadDocumentEncoding(documentId.trim(), "UTF-8", "ISO8859-1");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * <p>Description: 更新阅读标识</p>
     * @param id
     * @return
     */
    public boolean updateReadInd(long id) {
        boolean b = false;
        try {
            String sql = "update NotificationReceiver n set n.readInd = 'N' where n.id = " + id;
            this.em.createQuery(sql).executeUpdate();
            b = true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            b = false;
        }
        return b;
    }

    /**
     * <p>Description: 取得所以的RSS资源</p>
     * @param newschannelmstrId 频道的ID
     * @return
     */
    public List<Rss> getAllRss(Long newschannelmstrId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select r from Rss r where r.newschannelmstrId=" + newschannelmstrId + " order by r.publishedDate desc");
        String sql = sb.toString();
        return this.em.createQuery(sql).getResultList();
    }
    
    public Map<String,Object> loadMyNotices(int first,int pageSize,String readFlag){
    	String sql = "select nr from NotificationReceiver nr where nr.readInd='" + readFlag + "' and nr.notificationSender.sendOption = '" + DictConsts.TIH_TAX_MSG_TYPE_2 + "' and nr.receivedBy ='" + this.loginService.getCurrentUserName() + "' order by nr.notificationSender.sendDatetime desc";
    	String sqlCount = "select count(1) from NotificationReceiver nr where nr.readInd='" + readFlag + "' and nr.notificationSender.sendOption = '" + DictConsts.TIH_TAX_MSG_TYPE_2 + "' and nr.receivedBy ='" + this.loginService.getCurrentUserName() + "'";
    	long queryCount = (Long) this.em.createQuery(sqlCount).getSingleResult();
    	logger.info("queryCount"+queryCount);
    	Query queryDate = this.em.createQuery(sql);
    	queryDate.setFirstResult(first);
    	queryDate.setMaxResults(pageSize);
    	List<NotificationReceiver> list = queryDate.getResultList();
    	List<NoticeVo> noticeVos = new ArrayList<NoticeVo>();
    	for (NotificationReceiver n : list) {
			NoticeVo noticeVo = new NoticeVo();
			noticeVo.setReceiver(n);
			noticeVos.add(noticeVo);
		}
    	Map<String,Object> map = new HashMap<String, Object>();
    	map.put("queryCount", queryCount);
    	map.put("queryDate", noticeVos);
    	return map;
    }
}
