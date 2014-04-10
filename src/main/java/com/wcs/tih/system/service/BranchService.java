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
import com.wcs.tih.model.CompanyBranch;
import com.wcs.tih.system.controller.vo.BranchVo;

/**
 * <p>Project: tih</p>
 * <p>Description: 分支机构Service</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@Stateless
public class BranchService {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginService;

    /**
     * <p>Description: 查询分支机构</p>
     * @param organizationName 组织机构名称
     * @param effective 有效
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<BranchVo> queryBranch(long companymstrId,String organizationName, String effective) {
        StringBuffer sb = new StringBuffer();
        sb.append("select cb from CompanyBranch cb where cb.companymstr.id = "+companymstrId);
        if (null != organizationName && !"".equals(organizationName)) {
            sb.append(" and cb.name like '%" + organizationName + "%'");
        }
        if (null != effective && !"".equals(effective)) {
            sb.append(" and cb.defunctInd = '" + effective + "'");
        }
        sb.append(" order by cb.setupDatetime");
        List<CompanyBranch> companyBranchs = this.em.createQuery(sb.toString()).getResultList();
        List<BranchVo> branchVos = new ArrayList<BranchVo>();
        if (null != companyBranchs && companyBranchs.size() != 0) {
            BranchVo bv;
            for (CompanyBranch cb : companyBranchs) {
                bv = new BranchVo();
                bv.setId(cb.getId());
                bv.setOrganizationName(cb.getName());
                bv.setSetUpDatetime(cb.getSetupDatetime());
                bv.setLocation(cb.getLocation());
                bv.setBusinessScope(cb.getBusinessScope());
                bv.setRemakrs(cb.getRemakrs());
                bv.setEffective(cb.getDefunctInd());
                branchVos.add(bv);
            }
        }
        return branchVos;
    }

    /**
     * <p>Description: 判断是否存在分支机构</p>
     * @param organizationName 组织机构名称
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean isExistBranch(long companymstrId,BranchVo branchVo) {
    	if("Y".equals(branchVo.getEffective())){
    		return false;
    	}
        String sql = "select cb from CompanyBranch cb where cb.defunctInd = 'N' and cb.name = '" + branchVo.getOrganizationName().trim() + "' and cb.companymstr.id = "+companymstrId;
        if(branchVo.getId() != null){
        	sql = sql + " and cb.id <> " + branchVo.getId();
        }
        List<CompanyBranch> companyBranchs = this.em.createQuery(sql).getResultList();
        return companyBranchs.size() > 0;
    }

    /**
     * <p>Description: 保存分支机构</p>
     * @param companymstrId 公司Id
     * @param branchVo 分支机构Vo
     * @throws Exception
     */
    public void saveBranch(long companymstrId, BranchVo branchVo) throws Exception {
        CompanyBranch cb = new CompanyBranch();
        cb.setCompanymstr(this.em.find(Companymstr.class, companymstrId));
        cb.setName(branchVo.getOrganizationName().trim());
        cb.setSetupDatetime(branchVo.getSetUpDatetime());
        cb.setLocation(branchVo.getLocation().trim());
        cb.setBusinessScope(branchVo.getBusinessScope().trim());
        cb.setRemakrs(null != branchVo.getRemakrs() ? branchVo.getRemakrs().trim() : "");
        cb.setStatisticDatetime(new Date());
        cb.setDefunctInd(branchVo.getEffective());
        cb.setCreatedBy(this.loginService.getCurrentUserName());
        cb.setCreatedDatetime(new Date());
        cb.setUpdatedBy(this.loginService.getCurrentUserName());
        cb.setUpdatedDatetime(new Date());
        try {
            this.em.persist(cb);
        } catch (Exception e) {
            throw new Exception("系统出现异常，保存机构名称：" + cb.getName() + "的分支机构信息失败");
        }
    }

    /**
     * <p>Description: 更新分支机构</p>
     * @param branchVo 分支机构Vo
     * @throws Exception
     */
    public void updateBranch(BranchVo branchVo) throws Exception {
        CompanyBranch cb = new CompanyBranch();
        cb.setId(branchVo.getId());
        cb.setName(branchVo.getOrganizationName().trim());
        cb.setSetupDatetime(branchVo.getSetUpDatetime());
        cb.setLocation(branchVo.getLocation().trim());
        cb.setBusinessScope(branchVo.getBusinessScope().trim());
        cb.setRemakrs(null != branchVo.getRemakrs() ? branchVo.getRemakrs().trim() : "");
        cb.setDefunctInd(branchVo.getEffective());
        cb.setUpdatedBy(this.loginService.getCurrentUserName());
        cb.setUpdatedDatetime(new Date());
        try {
            this.em.merge(cb);
        } catch (Exception e) {
            throw new Exception("系统出现异常，编辑机构名称：" + cb.getName() + "的分支机构信息失败");
        }
    }
}
