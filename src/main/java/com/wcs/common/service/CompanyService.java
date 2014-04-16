package com.wcs.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.wcs.base.service.EntityService;
import com.wcs.base.service.LoginService;
import com.wcs.common.controller.vo.CompanyManagerModel;
import com.wcs.common.controller.vo.CompanyVo;
import com.wcs.common.model.Companymstr;
import com.wcs.common.model.O;
import com.wcs.tih.model.Taxauthority;
import com.wcs.tih.util.HanYuUtil;

@Stateless
public class CompanyService {
    @EJB
    private LoginService loginService;
    @EJB
    private EntityService entityService;
    @PersistenceContext
    private EntityManager em;

    public List<CompanyManagerModel> getCompanyManagerModel(CompanyManagerModel queryCondition, String path) {
        String join = "";
        String joinContion = "";
        List<CompanyManagerModel> list = new ArrayList<CompanyManagerModel>();
        String distinct = "";
        StringBuilder sb = new StringBuilder();
        if (path.indexOf("my_company") != -1) {
            distinct = " distinct ";
            join = " inner join POSITIONORG on o.id = POSITIONORG.oid inner join Userpositionorg on POSITIONORG.id = Userpositionorg.POSITIONORG_id inner join Position on Position.id=POSITIONORG.POSITION_ID ";
            joinContion = " and COMPANYMSTR.DEFUNCT_IND <> 'Y' and (POSITIONORG.DEFUNCT_IND <> 'Y' and Userpositionorg.DEFUNCT_IND <> 'Y' and Userpositionorg.usermstr_id = " + this.loginService.getCurrentUsermstr().getId() + ")   and Position.code='"+ResourceBundle.getBundle("positons").getString("CMPADMIN")+"'";
        }
        sb.append("select " + distinct + "o.STEXT,COMPANYMSTR.ADDRESS,COMPANYMSTR.ZIPCODE,COMPANYMSTR.TELPHONE,COMPANYMSTR.DEFUNCT_IND,COMPANYMSTR.id,o.BUKRS,COMPANYMSTR.type,COMPANYMSTR.desc,o.id as oid ,COMPANYMSTR.START_DATETIME,COMPANYMSTR.SETUP_DATETIME,COMPANYMSTR.id,COMPANYMSTR.REGION,COMPANYMSTR.PROVINCE,COMPANYMSTR.CODE,lower(COMPANYMSTR.CODE) from COMPANYMSTR INNER JOIN O on O.id=COMPANYMSTR.oid " + join + " where 1=1").append(joinContion);
       
        
        if (queryCondition != null) {
            if (queryCondition.getStext() != null && !queryCondition.getStext().trim().equals("")) {
                sb.append(" and o.STEXT like '%").append(queryCondition.getStext().trim()).append("%'");
            }
            if (queryCondition.getAddress() != null && !queryCondition.getAddress().trim().equals("")) {
                sb.append(" and COMPANYMSTR.ADDRESS like '%").append(queryCondition.getAddress().trim()).append("%'");
            }
            if (queryCondition.getJgName() != null && !queryCondition.getJgName().trim().equals("")) {
                sb.append(" and TAXAUTHORITY.name like '%").append(queryCondition.getJgName().trim()).append("%'");
            }
            if (queryCondition.getDefuctInt() != null && !queryCondition.getDefuctInt().equals("")) {
                sb.append(" and COMPANYMSTR.DEFUNCT_IND ='").append(queryCondition.getDefuctInt().trim()).append("'");
            }
            if (queryCondition.getRegion() != null && !queryCondition.getRegion().trim().equals("")) {
            	sb.append(" and COMPANYMSTR.REGION ='").append(queryCondition.getRegion().trim()).append("'");
            }
            if (queryCondition.getProvince() != null && !queryCondition.getProvince().trim().equals("")) {
            	sb.append(" and COMPANYMSTR.PROVINCE ='").append(queryCondition.getProvince().trim()).append("'");
            }
            if (queryCondition.getCode() != null && !queryCondition.getCode().trim().equals("")) {
                sb.append(" and COMPANYMSTR.CODE LIKE '%").append(queryCondition.getCode().trim()).append("%'");
            }
        }
        sb.append(" order by lower(COMPANYMSTR.CODE)");
        List li = this.em.createNativeQuery(sb.toString()).getResultList();
        
        for (int i = 0; i < li.size(); i++) {
            Object[] result = (Object[]) li.get(i);
            CompanyManagerModel model = new CompanyManagerModel();
            String stext = result[0] == null ? "" : result[0].toString();
            model.setStext(stext);
            model.setHanYuStext(HanYuUtil.getStringPinYin(stext == null ? "":(""+stext.trim().charAt(0))));
            String address = result[1] == null ? "" : result[1].toString().trim();
            model.setAddress(address);
            model.setHanYuAddress(HanYuUtil.getStringPinYin(address == null ? "":(""+address.trim().charAt(0))));
            model.setZipcode(result[2] == null ? "" : result[2].toString());
            model.setTelphone(result[3] == null ? "" : result[3].toString());
            model.setDefuctInt(result[4] == null ? "" : result[4].toString());
            model.setId(Long.valueOf(result[5].toString()));
            model.setJgCode(result[6] == null ? "" : result[6].toString());
            model.setType(result[7] == null ? "" : result[7].toString());
            model.setDesc(result[8] == null ? "" : result[8].toString());
            model.setOid(result[9].toString());
            model.setStartDatetime(result[10] == null ? null : (Date) result[10]);
            model.setStepDatetime(result[11] == null ? null : (Date) result[11]);
            model.setId(result[12] == null ? null : (Long) result[12]);
            model.setRegion(result[13] == null ? "" : (String) result[13]);
            model.setProvince(result[14] == null ? "" : (String) result[14]);
            model.setCode(result[15] == null ? "" : (String) result[15]);
            model.setLowererCode(result[16] == null ? "" : (String) result[16]);
            list.add(model);
        }
        return list;
    }

    public List<O> getInsertCompanyModel(CompanyManagerModel queryCondition) {
        StringBuilder sb = new StringBuilder();
        sb.append("select o from O o where o.defunctInd = 'N' ");
        if (queryCondition.getZipcode() != null && !queryCondition.getZipcode().equals("")) {
            sb.append(" and o.bukrs like '%").append(queryCondition.getZipcode()).append("%'");
        }
        if (queryCondition.getStext() != null && !queryCondition.getStext().equals("")) {
            sb.append(" and o.stext like '%").append(queryCondition.getStext()).append("%'");
        }
        return this.em.createQuery(sb.toString()).getResultList();
    }

    public void saveOrUpdate(Companymstr companymstr, boolean flag) {
        if (!flag) {
            companymstr.setCreatedDatetime(new Date());
            companymstr.setCreatedBy(loginService.getCurrentUsermstr().getAdAccount());
            companymstr.setUpdatedBy(loginService.getCurrentUsermstr().getAdAccount());
            companymstr.setAddress(companymstr.getAddress().trim());
            this.em.persist(companymstr);
        } else {
            Companymstr cm = (Companymstr) this.em.createQuery("select c from Companymstr c where c.id = " + companymstr.getId()).getSingleResult();
            cm.setAddress(companymstr.getAddress().trim());
            cm.setDefunctInd(companymstr.getDefunctInd());
            cm.setType(companymstr.getType());
            cm.setDesc(companymstr.getDesc());
            cm.setZipcode(companymstr.getZipcode());
            cm.setTelphone(companymstr.getTelphone());
            cm.setRegion(companymstr.getRegion());
            cm.setProvince(companymstr.getProvince());
            cm.setCode(companymstr.getCode());
            cm.setUpdatedBy(loginService.getCurrentUsermstr().getAdAccount());
            cm.setUpdatedDatetime(new Date());
            cm.setStartDatetime(companymstr.getStartDatetime());
            cm.setStepDatetime(companymstr.getStepDatetime());
            this.em.merge(cm);
        }

    }

    public boolean isExistedCompanyCode(CompanyManagerModel company){
        boolean isExisted = false;
        String sql = "select c from Companymstr c where c.code = '" + company.getCode().trim() + "' and c.defunctInd <> 'Y'";
        List<Companymstr> companys= this.em.createQuery(sql).getResultList();
        if(companys!=null&&companys.size()>0){
            if(company.getId()==null){
                isExisted = true;
            }else{
                for (Companymstr c : companys) {
                    if(!c.getId().equals(company.getId())){
                        isExisted = true;
                    }
                }
            }
        }
        return isExisted;
    }
    
    public Taxauthority selectTax(String name) {
        if (name == null || name.equals("")){
        	return null;
        }
        return (Taxauthority) this.em.createQuery("select t from Taxauthority t where t.name ='" + name + "'").getSingleResult();
    }

    public boolean exists(O o) {
        if (o != null) {
            String sql = "select c from Companymstr c  where  c.oid = '" + o.getId() + "'";
            List li = this.em.createQuery(sql).getResultList();
            return li.size() > 0;
        }
        return false;
    }
    
    public List<CompanyManagerModel> findMySpecialManageCompanys(CompanyManagerModel queryCondition,boolean isObserver) {
        ResourceBundle rb = ResourceBundle.getBundle("positons");
        String TRANSADM = rb.getString("TRANSADM");
        String TRANSOBSV = rb.getString("TRANSOBSV");
        String join = "";
        String joinContion = "";
        List<CompanyManagerModel> list = new ArrayList<CompanyManagerModel>();
        String distinct = "";
        StringBuilder sb = new StringBuilder();
        distinct = " distinct ";
        join = " inner join POSITIONORG on o.id = POSITIONORG.oid inner join Userpositionorg on POSITIONORG.id = Userpositionorg.POSITIONORG_id inner join Position on Position.id=POSITIONORG.POSITION_ID ";
        String position = " (Position.code='"+TRANSADM+"')";
        if(isObserver){
            position = " (Position.code='"+TRANSADM+"' or Position.code = '"+TRANSOBSV+"')";
        }
        joinContion = " and COMPANYMSTR.DEFUNCT_IND <> 'Y' and (POSITIONORG.DEFUNCT_IND <> 'Y' and Userpositionorg.DEFUNCT_IND <> 'Y' and Userpositionorg.usermstr_id = " + this.loginService.getCurrentUsermstr().getId() + ")   and "+position;
        sb.append("select " + distinct + "o.STEXT,COMPANYMSTR.ADDRESS,COMPANYMSTR.ZIPCODE,COMPANYMSTR.TELPHONE,COMPANYMSTR.DEFUNCT_IND,COMPANYMSTR.id,o.BUKRS,COMPANYMSTR.type,COMPANYMSTR.desc,o.id as oid ,COMPANYMSTR.START_DATETIME,COMPANYMSTR.SETUP_DATETIME,COMPANYMSTR.id,COMPANYMSTR.REGION,COMPANYMSTR.PROVINCE,COMPANYMSTR.CODE,lower(COMPANYMSTR.CODE) from COMPANYMSTR INNER JOIN O on O.id=COMPANYMSTR.oid " + join + " where 1=1").append(joinContion);
        
        if (queryCondition != null) {
            if (queryCondition.getStext() != null && !queryCondition.getStext().trim().equals("")) {
                sb.append(" and o.STEXT like '%").append(queryCondition.getStext().trim()).append("%'");
            }
            if (queryCondition.getAddress() != null && !queryCondition.getAddress().trim().equals("")) {
                sb.append(" and COMPANYMSTR.ADDRESS like '%").append(queryCondition.getAddress().trim()).append("%'");
            }
            if (queryCondition.getJgName() != null && !queryCondition.getJgName().trim().equals("")) {
                sb.append(" and TAXAUTHORITY.name like '%").append(queryCondition.getJgName().trim()).append("%'");
            }
            if (queryCondition.getDefuctInt() != null && !queryCondition.getDefuctInt().equals("")) {
                sb.append(" and COMPANYMSTR.DEFUNCT_IND ='").append(queryCondition.getDefuctInt().trim()).append("'");
            }
            if (queryCondition.getRegion() != null && !queryCondition.getRegion().trim().equals("")) {
                sb.append(" and COMPANYMSTR.REGION ='").append(queryCondition.getRegion().trim()).append("'");
            }
            if (queryCondition.getProvince() != null && !queryCondition.getProvince().trim().equals("")) {
                sb.append(" and COMPANYMSTR.PROVINCE ='").append(queryCondition.getProvince().trim()).append("'");
            }
            if (queryCondition.getCode() != null && !queryCondition.getCode().trim().equals("")) {
                sb.append(" and COMPANYMSTR.CODE LIKE '%").append(queryCondition.getCode().trim()).append("%'");
            }
        }
        sb.append(" order by lower(COMPANYMSTR.CODE)");
        List li = this.em.createNativeQuery(sb.toString()).getResultList();
        for (int i = 0; i < li.size(); i++) {
            Object[] result = (Object[]) li.get(i);
            CompanyManagerModel model = new CompanyManagerModel();
            String stext = result[0] == null ? "" : result[0].toString();
            model.setStext(stext);
            model.setHanYuStext(HanYuUtil.getStringPinYin(stext == null ? "":(""+stext.trim().charAt(0))));
            String address = result[1] == null ? "" : result[1].toString().trim();
            model.setAddress(address);
            model.setHanYuAddress(HanYuUtil.getStringPinYin(address == null ? "":(""+address.trim().charAt(0))));
            model.setZipcode(result[2] == null ? "" : result[2].toString());
            model.setTelphone(result[3] == null ? "" : result[3].toString());
            model.setDefuctInt(result[4] == null ? "" : result[4].toString());
            model.setId(Long.valueOf(result[5].toString()));
            model.setJgCode(result[6] == null ? "" : result[6].toString());
            model.setType(result[7] == null ? "" : result[7].toString());
            model.setDesc(result[8] == null ? "" : result[8].toString());
            model.setOid(result[9].toString());
            model.setStartDatetime(result[10] == null ? null : (Date) result[10]);
            model.setStepDatetime(result[11] == null ? null : (Date) result[11]);
            model.setId(result[12] == null ? null : (Long) result[12]);
            model.setRegion(result[13] == null ? "" : (String) result[13]);
            model.setProvince(result[14] == null ? "" : (String) result[14]);
            model.setCode(result[15] == null ? "" : (String) result[15]);
            model.setLowererCode(result[16] == null ? "" : (String) result[16]);
            list.add(model);
        }
        return list;
    }
    
    public List<CompanyVo> findCompanysBy(Map<String, Object> filter, int first, int pageSize) {
    	StringBuilder jpql = new StringBuilder();
    	//xsql
    	jpql.append(" select new com.wcs.common.controller.vo.CompanyVo(c,o)");
    	jpql.append(" from Companymstr c ,O o");
    	jpql.append(" where 1=1");
    	jpql.append(" and c.oid = o.id");
    	jpql.append(" and c.defunctInd = 'N'");
    	jpql.append(" and o.defunctInd = 'N'");
    	
    	// filter ...
    	Object companyIds = null;
    	if(filter != null) {
			companyIds = filter.get("companyIds");
    		if(companyIds != null){
    			jpql.append(" and c.id in ?1");
    		}
    	}
    	jpql.append( " order by c.code");
		Query query = entityService.createQuery(jpql.toString(), companyIds);
    	if(first > 0){
			query = query.setFirstResult(first);
		}
		if(pageSize > 0){
			query = query.setMaxResults(pageSize);
		}
		List<CompanyVo> companyVos = query.getResultList();
		return companyVos;
	}
    
    private List<CompanyVo> getCompanyVosBy(List<Object[]> rows){
    	List<CompanyVo> companyVos = new ArrayList<CompanyVo>();
    	CompanyVo companyVo = null;
    	for (Object[] coloums : rows) {
    		companyVo = new CompanyVo();
    		companyVo.setO(coloums[0] == null ? null : (O)coloums[0]);
    		companyVo.setCompany(coloums[1] == null ? null : (Companymstr)coloums[1]);
    		companyVos.add(companyVo);
		}
		return companyVos;
    }
}
