package com.wcs.tih.transaction.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.P;
import com.wcs.common.service.UserCommonService;
import com.wcs.tih.model.InvsTransferPrice;
import com.wcs.tih.model.InvsTransferPriceHistory;
import com.wcs.tih.model.InvsVerifyTransType;
import com.wcs.tih.model.InvsVerifyTransTypeHistory;
import com.wcs.tih.transaction.controller.vo.TransTypeVo;
import com.wcs.tih.transaction.controller.vo.TransferPriceVo;
import com.wcs.tih.util.ArithUtil;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class TransferPriceService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext 
    private EntityManager em;
    
    @EJB
    private LoginService loginService;
    @EJB
    private UserCommonService userservice;
    

    public TransferPriceVo saveTransferPrice(TransferPriceVo tpVo, List<TransTypeVo> ttVos) throws Exception{
        String currentUser = loginService.getCurrentUserName();
        Date date = new Date();
        InvsTransferPrice tp = tpVo.getTransferPrice();
        boolean same = true;
        if(!same || tpVo.getCompanymstrId()!=tp.getCompanymstrId()){
            tp.setCompanymstrId(tpVo.getCompanymstrId());
            same = false;
        }
        if(!same || tpVo.getCompanyName()!=tp.getCompanyName()){
            tp.setCompanyName(tpVo.getCompanyName());
            same = false;
        }
        if(!same || tpVo.getDecade().compareTo(tp.getDecade())!=0){
            tp.setDecade(tpVo.getDecade());
            same = false;
        }
        if(!same || tpVo.getAssoDebtEquityRatio()!=tp.getAssoDebtEquityRatio()){
            tp.setAssoDebtEquityRatio(tpVo.getAssoDebtEquityRatio());
            same = false;
        }
        String prepareDocInd = tpVo.getPrepareDocInd();
        if(!same || !tpVo.getPrepareDocInd().equals(tp.getPrepareDocInd())){
            tp.setPrepareDocInd(prepareDocInd);
            same = false;
        }
        if(prepareDocInd!=null&&"Y".equals(prepareDocInd)){
            String submitDocInd = tpVo.getSubmitDocInd();
            if(!same || !tpVo.getSubmitDocInd().equals(tp.getSubmitDocInd())){
                tp.setSubmitDocInd(submitDocInd);
                same = false;
            }
            if(submitDocInd!=null && "Y".equals(submitDocInd)){
                if(!same || tpVo.getDocSubmitDatetime().compareTo(tp.getDocSubmitDatetime())!=0){
                    tp.setDocSubmitDatetime(tpVo.getDocSubmitDatetime());
                    same = false;
                }
            }
        }
        if(!same || !tpVo.getRemarks().equals(tp.getRemarks())){
            tp.setRemarks(tpVo.getRemarks());
            same = false;
        }
        List<InvsVerifyTransType> vtts = new ArrayList<InvsVerifyTransType>();
        InvsVerifyTransType vtt = null;
        if(!same || (tp.getInvsVerifyTransTypes()!=null && tp.getInvsVerifyTransTypes().size()!= ttVos.size())){
            same = false;
        }
        for (int i = 0; i < ttVos.size(); i++) {
            InvsVerifyTransType oldTransType = null;
            TransTypeVo transTypeVo = ttVos.get(i);
            if(same && transTypeVo.getVerifyTransType()!=null){
                oldTransType = transTypeVo.getVerifyTransType();
            }else{
                same = false;
            }
            
            vtt = new InvsVerifyTransType();
            vtt.setInvsTransferPrice(tp);
            vtt.setDefunctInd("N");
            vtt.setCreatedBy(currentUser);
            vtt.setCreatedDatetime(date);
            vtt.setUpdatedBy(currentUser);
            vtt.setUpdatedDatetime(date);
            
            if(!same || !transTypeVo.getTransType().equals(oldTransType.getTransType())){
                same = false;
            }
            if(!same || !transTypeVo.getValidationMethod().equals(oldTransType.getValidationMethod())){
                same = false;
            }
            if(!same || ArithUtil.div(transTypeVo.getCompareCompanyMedian(), 100)!=oldTransType.getCompareCompanyMedian()){
                same = false;
            }
            if(!same || ArithUtil.div(transTypeVo.getBeforeAdjustRatio(), 100)!=oldTransType.getBeforeAdjustRatio()){
                same = false;
            }
            if(!same || ArithUtil.div(transTypeVo.getAfterAdjustRatio(), 100)!=oldTransType.getAfterAdjustRatio()){
                same = false;
            }
            if(!same || !transTypeVo.getAdjustSpecialReason().equals(oldTransType.getAdjustSpecialReason())){
                same = false;
            }
            vtt.setTransType(transTypeVo.getTransType());
            vtt.setValidationMethod(transTypeVo.getValidationMethod());
            vtt.setCompareCompanyMedian(ArithUtil.div(transTypeVo.getCompareCompanyMedian(), 100));
            vtt.setBeforeAdjustRatio(ArithUtil.div(transTypeVo.getBeforeAdjustRatio(), 100));
            vtt.setAfterAdjustRatio(ArithUtil.div(transTypeVo.getAfterAdjustRatio(), 100));
            vtt.setAdjustSpecialReason(transTypeVo.getAdjustSpecialReason());
            vtts.add(vtt);
        }
        if(!same){
            tp.setInvsVerifyTransTypes(vtts);
            if(tp.getId() == null || tp.getId() == 0){
                tp.setDefunctInd("N");
                tp.setCreatedBy(currentUser);
                tp.setCreatedDatetime(date);
                tp.setUpdatedBy(currentUser);
                tp.setUpdatedDatetime(date);
                em.persist(tp);
            }else{
                //merge
                tp = editTransPrice(tp,DictConsts.TIH_TAX_OPERATETYPE_TYPE_2);
            }
        }
        TransferPriceVo tmpPriceVo = new TransferPriceVo();
        tmpPriceVo.setTransferPrice(tp);
        tmpPriceVo.setSame(same);
        return tmpPriceVo;
    }
    
    public InvsTransferPrice deleteTransPrice(InvsTransferPrice tp) throws Exception {
        tp.setDefunctInd("Y");
        tp = editTransPrice(tp,DictConsts.TIH_TAX_OPERATETYPE_TYPE_3);
        return tp;
    }
    
    public InvsTransferPrice editTransPrice(InvsTransferPrice newTransPrice,String operate) throws Exception{
        String operateInd = DictConsts.TIH_TAX_OPERATETYPE_TYPE_2;
        InvsTransferPrice oldTransPrice = findTransferPrice(newTransPrice.getId());
        List<InvsTransferPriceHistory> oldHistories = oldTransPrice.getInvsTransferPriceHistories();
        if(oldHistories == null || oldHistories.size() == 0){
            operateInd = DictConsts.TIH_TAX_OPERATETYPE_TYPE_1;
        }
        InvsTransferPriceHistory oldHistory = copyTransPriceHistory(oldTransPrice, operateInd);
        if("N".equals(newTransPrice.getDefunctInd())){
            deleteTransTypes(newTransPrice.getId());
        }
        String currentUser = loginService.getCurrentUserName();
        Date date = new Date();
        newTransPrice.setUpdatedBy(currentUser);
        newTransPrice.setUpdatedDatetime(date);
        InvsTransferPrice merge = em.merge(newTransPrice);
        oldHistory.setInvsTransferPrice(merge);
        em.persist(oldHistory);
        return merge;
    }
    
    public InvsTransferPriceHistory copyTransPriceHistory(InvsTransferPrice transPrice, String operateInd){
        Date date = transPrice.getUpdatedDatetime();
        String lastUpdateBy = transPrice.getUpdatedBy();
        InvsTransferPriceHistory history = new InvsTransferPriceHistory();
        history.setDefunctInd("N");
        history.setCreatedBy(lastUpdateBy);
        history.setCreatedDatetime(date);
        history.setUpdatedBy(lastUpdateBy);
        history.setUpdatedDatetime(date);
        
        history.setOperateInd(operateInd);
        history.setCompanymstrId(transPrice.getCompanymstrId());
        history.setCompanyName(transPrice.getCompanyName());
        history.setDecade(transPrice.getDecade());
        history.setAssoDebtEquityRatio(transPrice.getAssoDebtEquityRatio());
        history.setPrepareDocInd(transPrice.getPrepareDocInd());
        history.setSubmitDocInd(transPrice.getSubmitDocInd());
        history.setDocSubmitDatetime(transPrice.getDocSubmitDatetime());
        history.setRemarks(transPrice.getRemarks());
        
        List<InvsVerifyTransTypeHistory> typeHistories = new ArrayList<InvsVerifyTransTypeHistory>();
        InvsVerifyTransTypeHistory typeHistory = null;
        for (InvsVerifyTransType type : transPrice.getInvsVerifyTransTypes()) {
            typeHistory = copyTypeHistory(type, operateInd);
            typeHistory.setInvsTransferPriceHistory(history);
            typeHistories.add(typeHistory);
        }
        history.setInvsVerifyTransTypeHistories(typeHistories);
        return history;
    }
    
    public InvsVerifyTransTypeHistory copyTypeHistory(InvsVerifyTransType type, String operateInd){
        Date date = type.getUpdatedDatetime();
        String lastUpdateBy = type.getUpdatedBy();
        InvsVerifyTransTypeHistory typeHistory = new InvsVerifyTransTypeHistory();
        typeHistory.setDefunctInd("N");
        typeHistory.setCreatedBy(lastUpdateBy);
        typeHistory.setCreatedDatetime(date);
        typeHistory.setUpdatedBy(lastUpdateBy);
        typeHistory.setUpdatedDatetime(date);
        
        typeHistory.setOperateInd(operateInd);
        typeHistory.setTransType(type.getTransType());
        typeHistory.setValidationMethod(type.getValidationMethod());
        typeHistory.setCompareCompanyMedian(type.getCompareCompanyMedian());
        typeHistory.setBeforeAdjustRatio(type.getBeforeAdjustRatio());
        typeHistory.setAfterAdjustRatio(type.getAfterAdjustRatio());
        typeHistory.setAdjustSpecialReason(type.getAdjustSpecialReason());
        return typeHistory;
    }
    
    public InvsTransferPrice updateTransferPrice(InvsTransferPrice tp){
        return em.merge(tp);
    }
    
    public InvsTransferPrice findTransferPrice(long id){
        return em.find(InvsTransferPrice.class, id);
    }
    
    public void deleteTransTypes(long id){
        StringBuffer jpql = new StringBuffer();
        jpql.append("delete from InvsVerifyTransType tt");
        jpql.append(" where tt.invsTransferPrice.id = ?1");
        em.createQuery(jpql.toString()).setParameter(1, id).executeUpdate();
    }
    
    public List<InvsTransferPriceHistory> findTransHistores(long id) throws Exception{
        StringBuffer jpql = new StringBuffer();
        jpql.append("select tph from InvsTransferPriceHistory tph join fetch tph.invsVerifyTransTypeHistories");
        jpql.append(" where tph.invsTransferPrice.id = ?1");
        jpql.append(" order by tph.id desc");
        return em.createQuery(jpql.toString()).setParameter(1, id).getResultList();
    }
    
    public List<TransferPriceVo> findTransPricesBy(TransferPriceVo tpVo, boolean isSpecial) throws Exception{
        
        StringBuffer jpql = new StringBuffer();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jpql.append("select tp,c.code,lower(c.code) as lowerCode from Companymstr c,InvsTransferPrice tp join fetch tp.invsVerifyTransTypes left join fetch tp.invsTransferPriceHistories");
        jpql.append(" where 1=1");
        jpql.append(" and tp.companymstrId = c.id");
        List<Long> companyIds = null;
        if(tpVo!=null){
            String companyName = tpVo.getCompanyName();
            if(companyName!=null && !"".equals(companyName)){
                jpql.append(" and tp.companyName like '%"+companyName+"%'");
            }
            Date decade = tpVo.getDecade();
            if(decade!=null){
                jpql.append(" and tp.decade = '"+df.format(decade)+"'");
            }
            String defunctInd = tpVo.getDefunctInd();
            if(defunctInd!=null && !"".equals(defunctInd)){
                jpql.append(" and tp.defunctInd = '"+defunctInd+"'");
            }
            companyIds = tpVo.getCompanys();
        }
        if(isSpecial){
            ResourceBundle rb = ResourceBundle.getBundle("positons");
            String transAdm = rb.getString("TRANSADM");
            String transObsv = rb.getString("TRANSOBSV");
            jpql.append(" and tp.companymstrId in");
            jpql.append(" (select c.id from Usermstr u,Userpositionorg up,Companymstr c,Positionorg po,Position p");
            jpql.append(" where u.id = up.usermstr.id and up.positionorg.id = po.id and po.oid = c.oid and po.position.id = p.id");
            jpql.append(" and po.defunctInd <> 'Y'");
            jpql.append(" and up.defunctInd <> 'Y'");
            jpql.append(" and (p.code = '"+transAdm+"' or p.code = '"+transObsv+"')");
            jpql.append(" and u.adAccount = '"+loginService.getCurrentUserName()+"')");
        }
        if(companyIds!=null && companyIds.size() > 0){
            jpql.append(" and tp.companymstrId in ?1");
        }
        if(isSpecial){
            jpql.append(" order by lowerCode");
        } else {
            jpql.append(" order by tp.id desc");
        }
        Query createQuery = em.createQuery(jpql.toString());
        if(companyIds!=null && companyIds.size() > 0){
            createQuery.setParameter(1, companyIds);
        }
        return convertTransPriceVo(createQuery.getResultList());
    }
    
    public List<TransferPriceVo> convertTransPriceVo(List resultList){
        TransferPriceVo transPriceVo = null;
        List<TransferPriceVo> transPriceVos = new ArrayList<TransferPriceVo>();
        long id = 1;
        for (int i = 0; i < resultList.size(); i++) {
            Object[] result = (Object[]) resultList.get(i);
            InvsTransferPrice tp = result[0] == null ? null:(InvsTransferPrice)result[0];
            if(tp !=null){
                List<InvsVerifyTransType> tts = tp.getInvsVerifyTransTypes();
                for (int j = 0; j < tts.size(); j++) {
                    transPriceVo = new TransferPriceVo();
                    transPriceVo.setId(id);
                    if(j==0){
                        transPriceVo.setTransferPrice(tp);
                        transPriceVo.setParent(true);
                        transPriceVo.setCompanyCode(result[1] == null ? "":(String)result[1]);
                        transPriceVo.setLowerCode(result[2] == null ? "":(String)result[2]);
                    }else{
                        transPriceVo.setParent(false);
                    }
                    InvsVerifyTransType transType = tts.get(j);
                    transPriceVo.setTransType(transType);
                    transPriceVo.setTransTypeVo(getTransTypeVo(transType));
                    transPriceVos.add(transPriceVo);
                }
                id++;
            }
        }
        return transPriceVos;
    }
    
    public List<InvsTransferPriceHistory> searchPriceHistories(InvsTransferPrice transPrice){
        List<InvsTransferPriceHistory> histories = new ArrayList<InvsTransferPriceHistory>();
        if("N".equals(transPrice.getDefunctInd())){
            InvsTransferPriceHistory history = new InvsTransferPriceHistory();
            history.setId(transPrice.getId());
            history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_4);
            history.setUpdatedDatetime(transPrice.getUpdatedDatetime());
            history.setUpdatedBy(transPrice.getUpdatedBy());
            histories.add(history);
        }else if ("Y".equals(transPrice.getDefunctInd())) {
            InvsTransferPriceHistory history = new InvsTransferPriceHistory();
            history.setId(transPrice.getId());
            history.setOperateInd(DictConsts.TIH_TAX_OPERATETYPE_TYPE_3);
            history.setUpdatedDatetime(transPrice.getUpdatedDatetime());
            history.setUpdatedBy(transPrice.getUpdatedBy());
            histories.add(history);
        }
        try {
            List<InvsTransferPriceHistory> oldHistories = findTransHistores(transPrice.getId());
            if(oldHistories != null){
                histories.addAll(oldHistories);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return histories;
    }
    
    public boolean isTransAdmin(Long companyId){
        boolean contains = false;
        try {
            List<Long> adminCompanys = findTransAdminCompanys();
            if(adminCompanys ==null){
                contains = false;
            }else{
                contains = adminCompanys.contains(companyId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return contains;
    }
    
    public List<Long> findTransAdminCompanys() throws Exception{
        return findTransCompanysByPositon("TRANSADM");
    }
    
    public List<Long> findTransObserveCompanys() throws Exception{
        return findTransCompanysByPositon("TRANSOBSV");
    }
    
    public List<Long> findTransCompanysByPositon(String positon) throws Exception{
        ResourceBundle rb = ResourceBundle.getBundle("positons");
        String positonCode = rb.getString(positon);
        StringBuffer jpql = new StringBuffer();
        jpql.append("select c.id from Usermstr u,Userpositionorg up,Companymstr c,Positionorg po,Position p");
        jpql.append(" where u.id = up.usermstr.id and up.positionorg.id = po.id and po.oid = c.oid and po.position.id = p.id");
        jpql.append(" and po.defunctInd <> 'Y'");
        jpql.append(" and up.defunctInd <> 'Y'");
        jpql.append(" and p.code = '"+positonCode+"'");
        jpql.append(" and u.adAccount = '"+loginService.getCurrentUserName()+"'");
        return em.createQuery(jpql.toString()).getResultList();
    }
    
    public String getUserName(String str) {
        if (str == null){
            return "";
        }
        UsermstrVo usermstrVo = userservice.getUsermstrVo(str);
        if (usermstrVo == null){
            return str;
        }
        P p = this.userservice.getUsermstrVo(str).getP();
        if (p == null){
            return str;
        }
        return p.getNachn();
    }
    
    public TransTypeVo getTransTypeVo(InvsVerifyTransType type) {
        TransTypeVo typeVo = new TransTypeVo();
        if (type == null){
        	return typeVo;
        }
        typeVo.setId(type.getId());
        typeVo.setVerifyTransType(type);
        typeVo.setTransType(type.getTransType());
        typeVo.setValidationMethod(type.getValidationMethod());
        typeVo.setCompareCompanyMedian(ArithUtil.mul(type.getCompareCompanyMedian(), 100));
        typeVo.setBeforeAdjustRatio(ArithUtil.mul(type.getBeforeAdjustRatio(), 100));
        typeVo.setAfterAdjustRatio(ArithUtil.mul(type.getAfterAdjustRatio(), 100));
        typeVo.setAdjustSpecialReason(type.getAdjustSpecialReason());
        typeVo.setAdjustSpecialReasons(Arrays.asList(type.getAdjustSpecialReason().split("\r\n")));
        return typeVo;
    }
    
}
