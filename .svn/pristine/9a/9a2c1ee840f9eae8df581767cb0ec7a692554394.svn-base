package com.wcs.tih.system.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.tih.model.CompanyLandDetail;
import com.wcs.tih.system.controller.vo.LandDetailVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 土地明细Service</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class LandDetailService {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;

    /**
     * <p>Description: 查询土地明细信息</p>
     * @param landCertificateNo 土地证编号
     * @param landName 宗地名称
     * @param effective 有效
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<LandDetailVo> queryLandDetail(long companymstrId,String landCertificateNo, String landName, String effective) {
        StringBuffer sb = new StringBuffer();
        sb.append("select cld from CompanyLandDetail cld where cld.companymstr.id = "+companymstrId);
        if (null != landCertificateNo && !"".equals(landCertificateNo)) {
            sb.append(" and cld.landCertificateNo like '%" + landCertificateNo + "%'");
        }
        if (null != landName && !"".equals(landName)) {
            sb.append(" and cld.landName like '%" + landName + "%'");
        }
        if (null != effective && !"".equals(effective)) {
            sb.append(" and cld.defunctInd = '" + effective + "'");
        }
        sb.append(" order by cld.landCertificateNo");
        List<CompanyLandDetail> clds = this.em.createQuery(sb.toString()).getResultList();
        List<LandDetailVo> ldvs = new ArrayList<LandDetailVo>();
        if (null != clds && clds.size() != 0) {
            LandDetailVo ldv;
            for (CompanyLandDetail cld : clds) {
                ldv = new LandDetailVo();
                ldv.setId(cld.getId());
                ldv.setLandCertificateNo(cld.getLandCertificateNo());
                ldv.setLandName(cld.getLandName());
                ldv.setLandArea(cld.getLandArea());
                ldv.setLandUsage(cld.getLandUsage());
                ldv.setLandKind(cld.getLandKind());
                ldv.setDevDegree(cld.getDevDegree());
                ldv.setCertificateRightBy(cld.getCertificateRightBy());
                ldv.setLandVolumeRate(cld.getLandVolumeRate());
                ldv.setLandGetDatetime(cld.getLandGetDatetime());
                ldv.setLandOverDatetime(cld.getLandOverDatetime());
                ldv.setLandCost(new BigDecimal(null != cld.getLandCost() ? cld.getLandCost().doubleValue() / 10000 : 0));
                ldv.setUnitLandCost(new BigDecimal((null != cld.getLandCost() && cld.getLandArea() != 0) ? cld.getLandCost().doubleValue() / cld.getLandArea() : 0));
                ldv.setTaxAccroding(cld.getTaxAccroding());
                ldv.setAnnualPay(cld.getAnnualPay());
                ldv.setLandAddress(cld.getLandAddress());
                ldv.setEffective(cld.getDefunctInd());
                ldvs.add(ldv);
            }
        }
        return ldvs;
    }

    /**
     * <p>Description: 判断是否存在土地明细信息</p>
     * @param landName 宗地名称
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean isExistLandDetail(long companymstrId,LandDetailVo landDetailVo) {
    	if("Y".equals(landDetailVo.getEffective())){
    		return false;
    	}
        String sql = "select cld from CompanyLandDetail cld where cld.defunctInd = 'N' and cld.landName = '" + landDetailVo.getLandName().trim() + "' and cld.companymstr.id = "+companymstrId;
        if(landDetailVo.getId() != null) {
        	sql = sql + " and cld.id <> " + landDetailVo.getId();
        }
        List<CompanyLandDetail> clds = this.em.createQuery(sql).getResultList();
        return clds.size() > 0;
    }

    /**
     * <p>Description: 保存土地明细信息</p>
     * @param companymstrId 公司Id
     * @param ldv 土地明细Vo
     * @throws Exception
     */
    public void saveLandDetail(long companymstrId, LandDetailVo ldv) throws Exception {
        CompanyLandDetail cld = new CompanyLandDetail();
        cld.setCompanymstr(this.em.find(Companymstr.class, companymstrId));
        cld.setLandCertificateNo(ldv.getLandCertificateNo().trim());
        cld.setLandName(ldv.getLandName().trim());
        cld.setLandArea(ldv.getLandArea());
        cld.setLandUsage(ldv.getLandUsage());
        cld.setLandKind(ldv.getLandKind());
        cld.setDevDegree(ldv.getDevDegree());
        cld.setCertificateRightBy(ldv.getCertificateRightBy());
        if(ldv.getLandVolumeRate() != null){
        	cld.setLandVolumeRate(ldv.getLandVolumeRate());
        }
        cld.setLandGetDatetime(ldv.getLandGetDatetime());
        cld.setLandOverDatetime(ldv.getLandOverDatetime());
        cld.setLandCost(new BigDecimal(ldv.getLandCost().doubleValue()*10000));
        cld.setTaxAccroding(ldv.getTaxAccroding());
        cld.setAnnualPay(ldv.getAnnualPay());
        cld.setLandAddress(ldv.getLandAddress().trim());
        cld.setDefunctInd(ldv.getEffective());
        cld.setCreatedBy(this.loginService.getCurrentUserName());
        cld.setCreatedDatetime(new Date());
        cld.setUpdatedBy(this.loginService.getCurrentUserName());
        cld.setUpdatedDatetime(new Date());
        try {
            this.em.persist(cld);
        } catch (Exception e) {
            throw new Exception("系统出现异常，保存土地证编号：" + ldv.getLandCertificateNo() + ",宗地名称：" + ldv.getLandName() + "的土地明细信息失败");
        }
    }

    /**
     * <p>Description: 更新土地明细信息</p>
     * @param ldv 土地明细Vo
     * @throws Exception
     */
    public void updateLandDetail(LandDetailVo ldv) throws Exception {
        CompanyLandDetail cld = new CompanyLandDetail();
        cld.setId(ldv.getId());
        cld.setLandCertificateNo(ldv.getLandCertificateNo().trim());
        cld.setLandName(ldv.getLandName().trim());
        cld.setLandArea(ldv.getLandArea());
        cld.setLandUsage(ldv.getLandUsage());
        cld.setLandKind(ldv.getLandKind());
        cld.setDevDegree(ldv.getDevDegree());
        cld.setCertificateRightBy(ldv.getCertificateRightBy());
        cld.setLandVolumeRate(ldv.getLandVolumeRate());
        cld.setLandGetDatetime(ldv.getLandGetDatetime());
        cld.setLandOverDatetime(ldv.getLandOverDatetime());
        cld.setLandCost(new BigDecimal(ldv.getLandCost().doubleValue()*10000));
        cld.setTaxAccroding(ldv.getTaxAccroding());
        cld.setAnnualPay(ldv.getAnnualPay());
        cld.setLandAddress(ldv.getLandAddress().trim());
        cld.setDefunctInd(ldv.getEffective());
        cld.setUpdatedBy(this.loginService.getCurrentUserName());
        cld.setUpdatedDatetime(new Date());
        try {
            this.em.merge(cld);
        } catch (Exception e) {
            throw new Exception("系统出现异常，编辑土地证编号：" + ldv.getLandCertificateNo() + ",宗地名称：" + ldv.getLandName() + "的土地明细信息失败");
        }
    }
}
