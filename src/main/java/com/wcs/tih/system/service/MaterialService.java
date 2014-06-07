package com.wcs.tih.system.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.Dict;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.CompanyMaterial;
import com.wcs.tih.system.controller.vo.MaterialVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 原料及工艺Service</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class MaterialService {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;
    @EJB
    private CommonService commonService;

    /**
     * <p>Description: 查询原料及工艺信息</p>
     * @param mainMaterial 主要原料
     * @param mainProduct 主要差评
     * @param effective 有效
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<MaterialVo> queryMaterial(long companymstrId,String mainMaterial, String mainProduct, String effective) {
        StringBuffer sb = new StringBuffer();
        sb.append("select cm from CompanyMaterial cm where cm.companymstr.id = "+companymstrId);
        if (mainMaterial != null && !"".equals(mainMaterial)) {
            sb.append(" and cm.mainMaterial like '%" + mainMaterial + "%'");
        }
        if (mainProduct != null && !"".equals(mainProduct)) {
            sb.append(" and cm.mainProduct like '%" + mainProduct + "%'");
        }
        if (effective != null && !"".equals(effective)) {
            sb.append(" and cm.defunctInd = '" + effective + "'");
        }
        sb.append(" order by cm.mainMaterial");
        List<CompanyMaterial> cms = this.em.createQuery(sb.toString()).getResultList();
        List<MaterialVo> mvs = new ArrayList<MaterialVo>();
        if (null != cms && cms.size() != 0) {
            MaterialVo mv;
            for (CompanyMaterial cm : cms) {
                mv = new MaterialVo();
                mv.setId(cm.getId());
                mv.setMainMaterial(cm.getMainMaterial());
                mv.setMainProduct(cm.getMainProduct());
                mv.setProcessing(cm.getProcessing());
                mv.setAbility(cm.getAbility());
                mv.setUnit(cm.getUnit());
                mv.setEffective(cm.getDefunctInd());
                mvs.add(mv);
            }
        }
        return mvs;
    }

    /**
     * <p>Description: 判断是否存在原料及工艺信息</p>
     * @param mainMaterial 主要原料
     * @param mainProduct 主要产品
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean isExistMaterial(long companymstrId,MaterialVo mv,String excuteMethod){
        List<CompanyMaterial> cms = null;
        boolean b=false;
        if("Y".equals(mv.getEffective())){
        	return b;
        }
        StringBuilder jpql = new StringBuilder();
        jpql.append(" select cm from CompanyMaterial cm");
        jpql.append(" where cm.defunctInd = 'N'");
        jpql.append(" and cm.companymstr.id = ").append(companymstrId);
        jpql.append(" and cm.processing = '" + mv.getProcessing().trim() + "'");
        jpql.append(" and cm.mainMaterial = '" + mv.getMainMaterial().trim() + "'");
        if("update".equals(excuteMethod)) { 
        	jpql.append(" and cm.id <> ").append(mv.getId());
        }
        cms = this.em.createQuery(jpql.toString()).getResultList();
        if (null != cms && cms.size() != 0) {
        	b = true;	
        }
        return b;
    }

    /**
     * <p>Description: 保存原料及工艺信息</p>
     * @param companymstrId 公司Id
     * @param mv 原料及工艺Vo
     * @throws Exception
     */
    public void saveMaterial(long companymstrId, MaterialVo mv) throws Exception {
        CompanyMaterial cm = new CompanyMaterial();
        cm.setCompanymstr(this.em.find(Companymstr.class, companymstrId));
        cm.setMainMaterial(mv.getMainMaterial().trim());
        cm.setMainProduct(mv.getMainProduct().trim());
        cm.setProcessing(mv.getProcessing().trim());
        cm.setAbility(mv.getAbility());
        cm.setUnit(mv.getUnit());
        cm.setDefunctInd(mv.getEffective());
        cm.setCreatedBy(this.loginService.getCurrentUserName());
        cm.setCreatedDatetime(new Date());
        cm.setUpdatedBy(this.loginService.getCurrentUserName());
        cm.setUpdatedDatetime(new Date());
        try {
            this.em.persist(cm);
        } catch (Exception e) {
            throw new Exception("系统出现异常，保存产品原料：" + mv.getMainMaterial() + "，主要产品：" + mv.getMainProduct() + "的原料及工艺信息失败");
        }
    }

    /**
     * <p>Description: 更新原料及工艺信息</p>
     * @param mv 原料及工艺Vo
     * @throws Exception
     */
    public void updateMaterial(MaterialVo mv) throws Exception {
        CompanyMaterial cm = new CompanyMaterial();
        cm.setId(mv.getId());
        cm.setMainMaterial(mv.getMainMaterial().trim());
        cm.setMainProduct(mv.getMainProduct().trim());
        cm.setProcessing(mv.getProcessing().trim());
        cm.setAbility(mv.getAbility());
        cm.setUnit(mv.getUnit());
        cm.setDefunctInd(mv.getEffective());
        cm.setUpdatedBy(this.loginService.getCurrentUserName());
        cm.setUpdatedDatetime(new Date());
        try {
            this.em.merge(cm);
        } catch (Exception e) {
            throw new Exception("系统出现异常，编辑产品原料：" + mv.getMainMaterial() + "，主要产品：" + mv.getMainProduct() + "的原料及工艺信息失败");
        }
    }
    
    /**
     * <p>Description: 根据cat值获得所有的Dict列表</p>
     * @param codeCat
     * @return
     */
    public List<Dict> getDictByCat(String cat, String lang) {
        return commonService.getDictByCat(cat, lang);
    }

}
