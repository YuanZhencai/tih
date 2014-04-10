package com.wcs.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.vo.SynclogSearchVo;
import com.wcs.common.controller.vo.SynclogVo;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.O;
import com.wcs.common.model.P;
import com.wcs.common.model.Synclog;
import com.wcs.tih.util.DateUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: MDS到TIH数据同步</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class MDSService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext
    private EntityManager em;
    private URL url ;
    private Map<String, String> propertiesMap;
    private ResourceBundle mds;
    private static ObjectMapper objectMapper;
    private static final String TIH_CAS_USR_P = "CAS_USR_P";
    private static final String CAS_USR_P = "CAS.USR.P";
    private static final String TIH_P = "P";
    private static final String SAP_HR_P = "SAP.HR.P";
    private static final String TIH_O = "O";
    private static final String SAP_HR_O = "SAP.HR.O";

    /**
     * <p>Description: 初始化</p>
     */
    @PostConstruct
    public void init(){
        objectMapper = new ObjectMapper();
        mds = ResourceBundle.getBundle("mds");
    }
    
    /**
     * <p>Description: 用户数据同步</p>
     * @return
     * @throws Exception 
     */
    public String userSynchronous() throws Exception {
        // 同步cas用户数据
        int updateCasUsrP = 0;
        int insertCasUsrP = 0;
        long timestampCasUsrP = getTimestampByTableName(TIH_CAS_USR_P);
        String jsonCasUsrPStr = getMDSByTableName(CAS_USR_P, timestampCasUsrP);
        if (jsonCasUsrPStr != null && !"".equals(jsonCasUsrPStr)) {
            // 取得时间戳
            long ts;
            try {
                ts = Long.parseLong(jsonCasUsrPStr.substring(8, 21));
            } catch (Exception e) {
                throw new Exception("没有可同步的用户数据");
            }
            // 将ts时间戳截取掉
            String str = "[" + jsonCasUsrPStr.substring(24, jsonCasUsrPStr.length());
            // 生成新的json字符串
            String newJsonStr = getJsonStrByJsonStr(str);
            List<CasUsrP> casUsrPList = jsonStrGenericObject(newJsonStr, new TypeReference<List<CasUsrP>>() {
            });
            if (null != casUsrPList && casUsrPList.size() != 0) {
                boolean b = false;
                Synclog synclog = null;
                String sql = "";
                for (CasUsrP cup : casUsrPList) {
                    if (cup.getPernr() != null && !"".equals(cup.getPernr())) {
                        sql = "select cup from CasUsrP cup where cup.pernr = '" + cup.getPernr() + "'";
                        int count = this.em.createQuery(sql).getResultList() == null ? 0 : this.em.createQuery(sql).getResultList().size();
                        if (count != 0) {
                            String updateSql = "update CasUsrP cup set cup.id = '" + cup.getId() + "',cup.defunctInd ='" + cup.getDefunctInd() + "' where cup.pernr ='" + cup.getPernr() + "'";
                            int n;
                            try {
                                n = this.em.createQuery(updateSql).executeUpdate();
                                if (n == 1) {
                                    // 存系统日志
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步更新" + TIH_CAS_USR_P + "表用户ID：" + cup.getId() + "，员工号：" + cup.getPernr() + "数据成功");
                                    synclog.setSyncType(TIH_CAS_USR_P);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                } else {
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步更新" + TIH_CAS_USR_P + "表用户ID：" + cup.getId() + "，员工号：" + cup.getPernr() + "数据失败");
                                    synclog.setSyncType(TIH_CAS_USR_P);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                }
                                updateCasUsrP++;
                            } catch (Exception e) {
                                throw new Exception("系统抛出异常,同步更新" + TIH_CAS_USR_P + "表用户ID：" + cup.getId() + "，员工号：" + cup.getPernr() + "数据失败");
                            }
                        } else {
                            try {
                                b = save(cup);
                                if (b) {
                                    // 存系统日志
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步新增" + TIH_CAS_USR_P + "表用户ID：" + cup.getId() + "，员工号：" + cup.getPernr() + "数据成功");
                                    synclog.setSyncType(TIH_CAS_USR_P);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                } else {
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步新增" + TIH_CAS_USR_P + "表用户ID：" + cup.getId() + "，员工号：" + cup.getPernr() + "数据失败");
                                    synclog.setSyncType(TIH_CAS_USR_P);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                }
                                insertCasUsrP++;
                            } catch (Exception e) {
                                throw new Exception("系统抛出异常,同步新增" + TIH_CAS_USR_P + "表用户ID：" + cup.getId() + "，员工号：" + cup.getPernr() + "数据失败");
                            }

                        }
                    }
                }
            }
        }
        int updateSapHrP = 0;
        int insertSapHrP = 0;
        // 同步HR的用户数据
        // 从数据库 取得最新 时间戳
        long timestamp = getTimestampByTableName(TIH_P);
        String jsonStr = getMDSByTableName(SAP_HR_P, timestamp);
        if (jsonStr != null && !"".equals(jsonStr)) {
            // 取得时间戳
            long ts;
            try {
                ts = Long.parseLong(jsonStr.substring(8, 21));
            } catch (Exception e) {
                throw new Exception("没有可同步的用户数据");
            }
            // 将ts时间戳截取掉
            String str = "[" + jsonStr.substring(24, jsonStr.length());
            // 生成新的json字符串
            String newJsonStr = getJsonStrByJsonStr(str);
            List<P> pList = jsonStrGenericObject(newJsonStr, new TypeReference<List<P>>() {
            });
            if (null != pList && pList.size() != 0) {
                boolean b = false;
                Synclog synclog = null;
                String sql = "";
                for (P p : pList) {
                    if (p.getId() != null && !"".equals(p.getId())) {
                        sql = "select p from P p where p.id ='" + p.getId() + "'";
                        int count = this.em.createQuery(sql).getResultList() == null ? 0 : this.em.createQuery(sql).getResultList().size();
                        if (count != 0) {
                            try {
                                b = update(p);
                                if (b) {
                                    // 存系统日志
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步更新" + TIH_P + "表用户id：" + p.getId() + "，用户名：" + p.getNachn() + "数据成功");
                                    synclog.setSyncType(TIH_P);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                } else {
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步更新" + TIH_P + "表用户id：" + p.getId() + "，用户名：" + p.getNachn() + "数据失败");
                                    synclog.setSyncType(TIH_P);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                }
                                updateSapHrP++;
                            } catch (Exception e) {
                                throw new Exception("系统抛出异常，同步更新" + TIH_P + "表用户ID：" + p.getId() + "，用户名：" + p.getNachn() + "数据失败");
                            }
                        } else {
                            try {
                                b = save(p);
                                if (b) {
                                    // 存系统日志
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步新增" + TIH_P + "表用户ID：" + p.getId() + "，用户名：" + p.getNachn() + "数据成功");
                                    synclog.setSyncType(TIH_P);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                } else {
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步新增" + TIH_P + "表用户ID：" + p.getId() + "，用户名：" + p.getNachn() + "数据失败");
                                    synclog.setSyncType(TIH_P);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                }
                                insertSapHrP++;
                            } catch (Exception e) {
                                throw new Exception("系统抛出异常，同步新增" + TIH_P + "表用户ID：" + p.getId() + "，用户名：" + p.getNachn() + "数据失败");
                            }
                        }
                    }
                }
            }
        }
        return "同步CAS_USR_P表:更新" + updateCasUsrP + "行数据成功，新增" + insertCasUsrP + "行数据成功；同步P表：更新" + updateSapHrP + "行数据成功，新增" + insertSapHrP + "行数据成功。";
    }

    /**
     * <p>Description: 组织数据同步</p>
     * @return
     * @throws Exception 
     */
    public String organizationSynchronous() throws Exception {
        int updateTihO = 0;
        int insertTihO = 0;
        // 从数据库 取得最新 时间戳
        long timestamp = getTimestampByTableName(TIH_O);
        String jsonStr = getMDSByTableName(SAP_HR_O, timestamp);
        if (jsonStr != null && !"".equals(jsonStr)) {
            // 取得时间戳
            long ts;
            try {
                ts = Long.parseLong(jsonStr.substring(8, 21));
            } catch (Exception e) {
                throw new Exception("没有可同步的组织数据");
            }
            // 将ts时间戳截取掉
            String str = "[" + jsonStr.substring(24, jsonStr.length());
            // 生成新的json字符串
            String newJsonStr = getJsonStrByJsonStr(str);
            List<O> oList = jsonStrGenericObject(newJsonStr, new TypeReference<List<O>>() {
            });
            if (null != oList && oList.size() != 0) {
                boolean b = false;
                Synclog synclog = null;
                String sql = "";
                for (O o : oList) {
                    if (null != o.getId() && !"".equals(o.getId())) {
                        sql = "select o from O o where o.id ='" + o.getId() + "'";
                        int count = this.em.createQuery(sql).getResultList() == null ? 0 : this.em.createQuery(sql).getResultList().size();
                        if (count != 0) {
                            try {
                                b = update(o);
                                if (b) {
                                    // 存系统日志
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步更新" + TIH_O + "表组织ID：" + o.getId() + "，组织名：" + o.getStext() + "数据成功");
                                    synclog.setSyncType(TIH_O);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                } else {
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步更新" + TIH_O + "表组织ID：" + o.getId() + "，组织名：" + o.getStext() + "数据失败");
                                    synclog.setSyncType(TIH_O);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                }
                                updateTihO++;
                            } catch (Exception e) {
                                throw new Exception("系统抛出异常，同步更新" + TIH_O + "表组织ID：" + o.getId() + "，组织名：" + o.getStext() + "数据失败");
                            }
                        } else {
                            try {
                                b = save(o);
                                if (b) {
                                    // 存系统日志
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步新增" + TIH_O + "表组织ID：" + o.getId() + "，组织名：" + o.getStext() + "数据成功");
                                    synclog.setSyncType(TIH_O);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                } else {
                                    synclog = new Synclog();
                                    synclog.setRemarks("同步新增" + TIH_O + "表组织ID：" + o.getId() + "，组织名：" + o.getStext() + "数据失败");
                                    synclog.setSyncType(TIH_O);
                                    synclog.setSyncDatetime(new Timestamp(new Date().getTime()));
                                    synclog.setVersion(new Timestamp(ts));
                                    saveSynclog(synclog);
                                }
                                insertTihO++;
                            } catch (Exception e) {
                                throw new Exception("系统抛出异常，同步新增" + TIH_O + "表组织ID：" + o.getId() + "，组织名：" + o.getStext() + "数据失败");
                            }
                        }
                    }
                }
            }
        }
        return "同步O表:更新" + updateTihO + "行数据成功，新增" + insertTihO + "行数据成功";
    }

    /**
     * 泛型保存
     * 
     * @param objectList
     * 
     * @return 返回boolean
     */
    private <T> boolean save(final T entity) {
        boolean b = false;
        try {
            this.em.persist(entity);
            b = true;
        } catch (Exception e) {
            b = false;
            logger.error(e.getMessage(), e);
        }
        return b;
    }

    /**
     * 泛型修改
     * 
     * @param objectList
     * 
     * @return 返回boolean
     */
    private <T> boolean update(final T entity) {
        boolean b = false;
        try {
            this.em.merge(entity);
            b = true;
        } catch (Exception e) {
            b = false;
            logger.error(e.getMessage(), e);
        }
        return b;
    }

    /**
     * 保存日志信息
     * 
     * @param sl 日志对象
     * 
     * @return 返回boolean
     */
    public boolean saveSynclog(Synclog sl) {
        boolean b = false;
        try {
            this.em.persist(sl);
            b = true;
        } catch (Exception e) {
            b = false;
        }
        return b;
    }

    /**
     * 取得所有的日志信息
     * 
     * @return 返回系统日志Vo集合
     */
    public List<SynclogVo> getAllSynclog(SynclogSearchVo synclogSearchVo) {
        StringBuffer sb = new StringBuffer();
        sb.append("select sl from Synclog sl where 1=1");
        if (null != synclogSearchVo) {
            if (null != synclogSearchVo.getSyncType() && !"".equals(synclogSearchVo.getSyncType().trim())) {
                sb.append(" and sl.syncType like '%" + synclogSearchVo.getSyncType().trim() + "%'");
            }
            if (null != synclogSearchVo.getRemarks() && !"".equals(synclogSearchVo.getRemarks().trim())) {
                sb.append(" and sl.remarks like '%" + synclogSearchVo.getRemarks().trim() + "%'");
            }
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (null != synclogSearchVo.getSyncStartDatetime()) {
                sb.append(" and sl.syncDatetime >= '" + df.format(synclogSearchVo.getSyncStartDatetime()) + " 00:00:00'");
            }
            if (null != synclogSearchVo.getSyncEndDatetime()) {
                sb.append(" and sl.syncDatetime <= '" + df.format(DateUtil.getNextDate(synclogSearchVo.getSyncEndDatetime())) + " 00:00:00'");
            }
        }
        sb.append(" order by sl.id desc");
        try {
            List<Synclog> synclogs = this.em.createQuery(sb.toString()).getResultList();
            List<SynclogVo> synclogVos = new ArrayList<SynclogVo>();
            if (synclogs != null && synclogs.size() != 0) {
                SynclogVo slv = null;
                long num = 0;
                for (Synclog sl : synclogs) {
                    slv = new SynclogVo();
                    num++;
                    slv.setId(num);
                    slv.setSynclog(sl);
                    synclogVos.add(slv);
                }
            }
            return synclogVos;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 取得某表的时间戳
     * 
     * @param tableName 表名
     * 
     * 
     * @return 返回时间戳
     */
    private long getTimestampByTableName(String tableName) {
        logger.info("tableName :" + tableName);
        return 0l;
    }

    /**
     * 泛型查找唯一对象
     * 
     * @param entity 实体类型
     * @param id
     * 
     * @param objectList
     * 
     * @return 返回实体
     */
    private <T> T get(Class<T> entity, Serializable id) {
        return this.em.find(entity, id);
    }

    /**
     * 将json字符串转化正确的json字符串
     * 
     * @param <T>
     * 
     * @param json json字符串
     * @return 返回泛型集合
     */
    @SuppressWarnings("unchecked")
    private String getJsonStrByJsonStr(String jsonStr) {
        try {
            List<LinkedHashMap<String, Object>> tempList = new ArrayList<LinkedHashMap<String, Object>>();
            // 将字符串转化成集合对象
            List<LinkedHashMap<String, Object>> list = objectMapper.readValue(jsonStr, List.class);
            // jackson泛型转化必须的对象和json对象一一对应 下面是把json对象key转换成小写
            if (null != list && list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    Map<String, Object> temp = new LinkedHashMap<String, Object>();
                    for (String key : map.keySet()) {
                        temp.put(key.toLowerCase(), map.get(key));
                    }
                    tempList.add((LinkedHashMap<String, Object>) temp);
                }
            }
            // 将新的集合转化成json字符串
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, tempList);
            return (writer.toString()).replaceAll("defunct_ind", "defunctInd");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
     * 
     * @param <T>
     * @param jsonString JSON字符串
     * @param tr TypeReference,例如: new TypeReference< List<FamousUser> >(){}
     * @return List对象列表
     */
    @SuppressWarnings("unchecked")
    private <T> T jsonStrGenericObject(String jsonString, TypeReference<T> typeReference) {
        if (jsonString == null || "".equals(jsonString)) {
            return null;
        } else {
            try {
                return ((T) objectMapper.readValue(jsonString, typeReference));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 通过type及ts筛选得到JSON格式的主数据信息
     * 
     * @param tableName 要去的那张表的数据
     * @param timestamp 取得某个时间戳之后的数据
     * @return 返回json字符串
     */
    private String getMDSByTableName(String tableName, long timestamp) {
        propertiesMap = new HashMap<String, String>();
        propertiesMap.put("ts", String.valueOf(timestamp));
        return getMds(mds.getString("MDS.RES_DATA"), tableName, propertiesMap);
    }

    /**
     * 通过HTTP GET方式获得资源信息
     * 
     * @param uri 属性文件ip 端口等
     * @param tableName 需要取得的表的数据
     * @param propertiesMap 属性集合 例如:ts 时间错
     * @return 返回json字符串
     * @throws IOException
     */
    private String getMds(String uri, String tableName, Map<String, String> propertiesMap) {
        String resource = null;
        StringBuffer resources = new StringBuffer();
        URL u = urlBulider(uri, tableName, propertiesMap);
        URLConnection urlc;
        try {
            urlc = u.openConnection();
            urlc.setDoOutput(false);
            urlc.setAllowUserInteraction(false);
            urlc.connect();//自己添加的代码
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF8"));
            while ((resource = br.readLine()) != null) {
                resources.append(resource);
            }
            br.close();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return resources.toString();
    }

    /**
     * 构建URL
     * 
     * @param uri 属性文件ip 端口等
     * @param tableName 需要取得的表的数据
     * @param propertiesMap 属性集合 例如:ts 时间错
     * @return 返回URL对象 例如：http://10.229.12.153:9081/rs/data/SAP.HR.P?ts=0
     * @throws UnsupportedEncodingException MalformedURLException
     */
    private URL urlBulider(String uri, String tableName, Map<String, String> propertiesMap) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(mds.getString("MDS.MARK_SLASH")).append(uri);
        sbf.append(mds.getString("MDS.MARK_SLASH")).append(tableName);
        sbf.append(mds.getString("MDS.MARK_QUE"));
        try {
            for (String key : propertiesMap.keySet()) {
                sbf.append(key + mds.getString("MDS.MARK_EUQ") + URLEncoder.encode(propertiesMap.get(key) == null ? "" : propertiesMap.get(key), mds.getString("MDS.UTF8"))).append(mds.getString("MDS.MARK_AMPERSAND"));
            }
            sbf.deleteCharAt(sbf.length() - 1);
            url = new URL(mds.getString("MDS.BASEPATH") + sbf.toString());
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
        }
        return url;
    }
}
