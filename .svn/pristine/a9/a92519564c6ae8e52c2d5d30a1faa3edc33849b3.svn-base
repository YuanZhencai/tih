package com.wcs.tih.system.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.vo.UsermstrVo;
import com.wcs.common.model.CasUsrP;
import com.wcs.common.model.P;
import com.wcs.common.model.Usermstr;
import com.wcs.tih.model.WfSupervisor;
import com.wcs.tih.system.controller.vo.WfSupervisorVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 组织层级Service接口实现类</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class OrganizationLevelService implements OrganizationLevelInterface {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext
    private EntityManager em;

    /**
     * @see com.wcs.tih.system.service.OrganizationLevelInterface#getUsermstrVo(java.lang.String, java.lang.String)
     */
    public UsermstrVo getUsermstrVo(String adAccount, String requestFormType, Long companyId) {
        StringBuffer sb = new StringBuffer();
        sb.append("select wfs from WfSupervisor wfs where wfs.chargedBy = '" + adAccount + "' and wfs.value='" + requestFormType + "' and wfs.companymstrId = " + companyId  + " ");
        if (this.em.createQuery(sb.toString()).getResultList().isEmpty()) { 
        	return null; 
        }
        WfSupervisor wfs = (WfSupervisor) this.em.createQuery(sb.toString()).getResultList().get(0);
        Usermstr u = getUsermstr(wfs.getSupervisor());
        UsermstrVo uv = new UsermstrVo();
        if (null != u) {
            uv.setUsermstr(u);
            String sql = "select cup from CasUsrP cup where cup.id = '" + u.getAdAccount() + "'";
            List<CasUsrP> casUsrPs = this.em.createQuery(sql).getResultList();
            if (null != casUsrPs && casUsrPs.size() == 1) {
                CasUsrP cup = casUsrPs.get(0);
                uv.setCup(cup);
                if (null != cup.getPernr() && !"".equals(cup.getPernr())) {
                    try {
                        uv.setP(this.em.find(P.class, cup.getPernr()));
                    } catch (Exception e) {
                        uv.setP(new P());
                        uv.getP().setNachn(u.getAdAccount());
                    }
                } else {
                    uv.setP(new P());
                    uv.getP().setNachn(u.getAdAccount());
                }
            }
        }
        return uv;
    }

    /**
     * @see com.wcs.tih.system.service.OrganizationLevelInterface#getAllWfSupervisorVo(java.lang.String)
     */
    public List<WfSupervisorVo> getAllWfSupervisorVo(String adAccount) {
        StringBuffer sb = new StringBuffer();
        sb.append("select wfs from WfSupervisor wfs where 1=1");
        if (adAccount != null && !"".equals(adAccount)) {
            sb.append(" and wfs.chargedBy = '" + adAccount + "'");
        }
        sb.append(" order by wfs.chargedBy,wfs.value");
        String sql = sb.toString();
        List<WfSupervisor> list = this.em.createQuery(sql).getResultList();
        List<WfSupervisorVo> wfSupervisorVos = new ArrayList<WfSupervisorVo>();
        if (list != null && list.size() != 0) {
            WfSupervisorVo wfsv = null;
            for (WfSupervisor wfs : list) {
                wfsv = new WfSupervisorVo();
                wfsv.setAdAccount(wfs.getChargedBy());
                if (wfs.getSupervisor() != null && !"".equals(wfs.getSupervisor())) {
                    wfsv.setSupervisor(wfs.getSupervisor());
                    Usermstr u=getUsermstr(wfs.getSupervisor());
                    if(u!=null){
                        wfsv.setDefunctInd(u.getDefunctInd());
                        String sqlCasUsrP = "select cup from CasUsrP cup where cup.id = '" + wfs.getSupervisor() + "'";
                        List<CasUsrP> casUsrPs = this.em.createQuery(sqlCasUsrP).getResultList();
                        if (null != casUsrPs && casUsrPs.size() == 1) {
                            CasUsrP cup = casUsrPs.get(0);
                            if (null!= cup.getPernr()&&!"".equals(cup.getPernr())) {
                                P p= this.em.find(P.class, cup.getPernr());
                                if(p!=null){
                                    wfsv.setSupervisorName(p.getNachn());
                                }else{
                                    wfsv.setSupervisorName(wfs.getSupervisor());
                                }
                            } else {
                                wfsv.setSupervisorName(wfs.getSupervisor());
                            }
                        }else{
                            wfsv.setSupervisorName(wfs.getSupervisor());
                        }
                    }else{
                        wfsv.setSupervisorName(wfs.getSupervisor());
                    }
                }
                wfsv.setRequestFormType(wfs.getValue());
                wfsv.setCompanymstrId(wfs.getCompanymstrId());
                wfsv.setCompanyName(getCompanyName(wfs.getCompanymstrId()));
                wfSupervisorVos.add(wfsv);
            }
        }
        return wfSupervisorVos;
    }
    
    public String getCompanyName(Long id){
        String sql = "select o.STEXT from COMPANYMSTR c,O o where c.OID = o.ID and c.ID = " + id + " ";
        List list = this.em.createNativeQuery(sql).getResultList();
        if(list.size() == 1){
            return list.get(0).toString();
        }
        return null;
    }

    /**
     * @see com.wcs.tih.system.service.OrganizationLevelInterface#saveOrganizationLevel(java.lang.String, java.util.List)
     */
    public boolean saveOrganizationLevel(String selectedAdAccount, List<WfSupervisorVo> wfSupervisorVoList) {
        boolean b = deleteOrganizationLevel(selectedAdAccount);
        if (b) {
            if (wfSupervisorVoList != null && wfSupervisorVoList.size() != 0) {
                WfSupervisor wfSupervisor = null;
                for (WfSupervisorVo wfSupervisorVo : wfSupervisorVoList) {
                    wfSupervisor = new WfSupervisor();
                    wfSupervisor.setChargedBy(wfSupervisorVo.getAdAccount());
                    wfSupervisor.setSupervisor(wfSupervisorVo.getSupervisor());
                    wfSupervisor.setType("");
                    wfSupervisor.setValue(wfSupervisorVo.getRequestFormType());
                    wfSupervisor.setCompanymstrId(wfSupervisorVo.getCompanymstrId());
                    this.em.persist(wfSupervisor);
                }
            }
        }
        return b;
    }

    private boolean deleteOrganizationLevel(String adAccount) {
        StringBuffer sb = new StringBuffer();
        sb.append("delete from WfSupervisor wfs where 1=1");
        if (adAccount != null && !"".equals(adAccount)) {
            sb.append(" and wfs.chargedBy = '" + adAccount + "'");
        }
        boolean b = false;
        try {
            String sql = sb.toString();
            this.em.createQuery(sql).executeUpdate();
            b = true;
        } catch (Exception e) {
            b = false;
            logger.error(e.getMessage(), e);
        }
        return b;
    }

    private Usermstr getUsermstr(String adAccount) {
        String sql = "select u from Usermstr u where u.adAccount = :adAccount";
        Query q = em.createQuery(sql);
        q.setParameter("adAccount", adAccount.trim());
        return (Usermstr) q.getSingleResult();
    }
}
