package com.wcs.tih.system.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.wcs.base.service.LoginService;
import com.wcs.common.model.Companymstr;
import com.wcs.common.service.CommonService;
import com.wcs.tih.model.CompanyStockStructure;

@Stateless
public class CompanyStockService {
    @PersistenceContext
    private EntityManager em;
    @EJB
    private LoginService loginservice;
    @EJB
    private CompanyInvestmentService companyInvestmentService;
    @EJB
    private CommonService commonService;

    public List searchStockNew(long companyid, Map<String, Object> searchMap, String date) {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sb = new StringBuilder();
        sb.append("select   c from CompanyStockStructure c where c.companymstr.id = ").append(companyid);
        if (date != null) {
            sb.append(" and c.statisticsDatetime ='").append(date).append(" 00:00:00.0'");
        } else {
            if (searchMap.get("name") != null && !searchMap.get("name").equals("")) {
                sb.append(" and c.shareholder like '%").append(searchMap.get("name")).append("%'");
            }
            if (searchMap.get("defunctInd") != null && !searchMap.get("defunctInd").equals("")) {
                sb.append(" and c.defunctInd = '").append(searchMap.get("defunctInd")).append("'");
            }
            Object startdate = searchMap.get("startdate");
            Object endDate = searchMap.get("endDate");
            if ((startdate != null && !startdate.equals("")) && (endDate != null && !endDate.equals(""))) {
                sb.append(" and c.statisticsDatetime between '").append(sm.format(startdate)).append("' and '").append(sm.format(endDate)).append("'");
            } else if (startdate != null && !startdate.equals("")) {
                sb.append(" and c.statisticsDatetime > '").append(sm.format(startdate)).append("'");
            } else if (endDate != null && !endDate.equals("")) {
                sb.append(" and c.statisticsDatetime < '").append(sm.format(endDate)).append("'");
            }
        }
        sb.append(" order by c.statisticsDatetime");
        
        String sql = "select sum (b.REGISTERED_CAPITAL),b.STATISTICS_DATETIME from COMPANY_STOCK_STRUCTURE as b where COMPANYMSTR_ID= " + companyid + " group by b.STATISTICS_DATETIME ";
        Map<Date, BigDecimal> m = new HashMap<Date, BigDecimal>();
        List list = this.em.createQuery(sb.toString()).getResultList();
        List list2 = this.em.createNativeQuery(sql).getResultList();

        for (int i = 0; i < list2.size(); i++) {
            Object[] result = (Object[]) list2.get(i);
            m.put((Date) result[1], (BigDecimal) result[0]);
        }

        List returnList = new ArrayList();

        for (int i = 0; i < list.size(); i++) {
            CompanyStockStructure c = (CompanyStockStructure) list.get(i);
            returnList.add(c);
        }

        return returnList;
    }

    public Companymstr getComapnyById(long id) {
        return this.companyInvestmentService.getCompanyById(id);
    }

    public CompanyStockStructure getCompanyStockStructureById(long id) {
        return (CompanyStockStructure) this.em.createQuery("select c from CompanyStockStructure c where c.id = " + id).getSingleResult();
    }

    public void update(CompanyStockStructure company) {
        String sql = " select c from  CompanyStockStructure c where c.id=" + company.getId();
        CompanyStockStructure c = (CompanyStockStructure) this.em.createQuery(sql).getSingleResult();
         c.setUpdatedBy(loginservice.getCurrentUsermstr().getAdAccount());
        c.setUpdatedDatetime(new Date());
        c.setType(company.getType());
        c.setCurrency(company.getCurrency());
        c.setRatio(company.getRatio());
        c.setRegisteredCapital(company.getRegisteredCapital());
        c.setShareholder(company.getShareholder());
        c.setDefunctInd(company.getDefunctInd());
        this.em.merge(c);
    }

    public Date getLastStock(long companyid) {
        String sql = " select c.statisticsDatetime from CompanyStockStructure  c where c.companymstr.id = " + companyid + " order by c.statisticsDatetime desc";
        return (Date) this.em.createQuery(sql).getResultList().get(0);
    }

    public void insert(CompanyStockStructure company) {
        CompanyStockStructure company2 = new CompanyStockStructure();
        company2.setRatio(company.getRatio());
         company2.setCreatedBy(loginservice.getCurrentUsermstr().getAdAccount());
         company2.setUpdatedBy(loginservice.getCurrentUsermstr().getAdAccount());
        company2.setCreatedDatetime(new Date());
        company2.setUpdatedDatetime(new Date());
        company2.setCompanymstr(company.getCompanymstr());
        company2.setRegisteredCapital(company.getRegisteredCapital());
        company2.setShareholder(company.getShareholder());
        company2.setType(company.getType());
        company2.setCurrency(company.getCurrency());
        company2.setDefunctInd(company.getDefunctInd());
        company2.setStatisticsDatetime(company.getStatisticsDatetime());
        this.em.persist(company2);
    }
    
    public void insertOrUpdate(long companyid,List<CompanyStockStructure> company){
        this.deleteStock(companyid,company.get(0).getStatisticsDatetime());
        for(int i=0;i<company.size();i++){
            CompanyStockStructure c = new CompanyStockStructure();
            c.setCompanymstr(company.get(i).getCompanymstr());
            c.setCreatedBy(company.get(i).getCreatedBy());
            c.setCreatedDatetime(company.get(i).getCreatedDatetime());
            c.setCurrency(company.get(i).getCurrency());
            c.setDefunctInd(company.get(i).getDefunctInd());
            c.setRatio(company.get(i).getRatio());
            c.setRegisteredCapital(company.get(i).getRegisteredCapital());
            c.setShareholder(company.get(i).getShareholder());
            c.setStatisticsDatetime(company.get(i).getStatisticsDatetime());
            c.setType(company.get(i).getType());
            c.setUpdatedBy(company.get(i).getUpdatedBy());
            c.setUpdatedDatetime(company.get(i).getUpdatedDatetime());
            this.em.persist(c);
        }
    }
    public void deleteStock(long companyid,Date date){
        this.em.createNativeQuery("delete from Company_Stock_Structure where COMPANYMSTR_ID ="+companyid+" and Company_Stock_Structure.STATISTICS_DATETIME ='"+new SimpleDateFormat("yyyy-MM-dd").format(date)+" 00:00:00.0'").executeUpdate();
    }
    
   
    public String getCompanyStockStructureByDate(Date date, long companyid) {
        String sql = "select sum(ss.registeredCapital),ss.currency from CompanyStockStructure ss where ss.statisticsDatetime = '"+ new SimpleDateFormat("yyyy-MM-dd").format( date)+" 00:00:00.0' and ss.companymstr.id = "+companyid+" and ss.defunctInd <> 'Y' and ss.companymstr.defunctInd <> 'Y' group by ss.currency";
        List list = this.em.createQuery(sql).getResultList();
        StringBuffer sb = new StringBuffer();
        sb.append("总注册资本：");
        for (int i = 0; i < list.size(); i++) {
            Object[] result = (Object[]) list.get(i);
            sb.append(((BigDecimal) result[0]).intValue()).append("   ");
            sb.append("万"+commonService.getValueByDictCatKey((String) result[1], "zh_CN")).append("+");
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    public List searchStockByDate(String date) {
        String sql = "select * from COMPANY_STOCK_STRUCTURE where COMPANY_STOCK_STRUCTURE.STATISTICS_DATETIME= '" + date + "'";
        List list = this.em.createNativeQuery(sql).getResultList();
        List returnList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            Object[] result = (Object[]) list.get(i);
            CompanyStockStructure company = new CompanyStockStructure();
            company.setId((Long) result[0]);
            company.setShareholder((String) result[2]);
            company.setRegisteredCapital((BigDecimal) result[4]);
            company.setCurrency((String) result[5]);
        }
        return returnList;
    }

    public boolean isExistedStock(CompanyStockStructure stock){
    	if("Y".equals(stock.getDefunctInd())){
    		return false;
    	}
    	StringBuilder jpql = new StringBuilder();
    	jpql.append(" select s from CompanyStockStructure s");
    	jpql.append(" where s.defunctInd = 'N'");
    	jpql.append(" and s.shareholder = '" + stock.getShareholder() + "'");
    	jpql.append(" and s.statisticsDatetime = ?1");
    	jpql.append(" and s.id　<> " + stock.getId());
    	List<CompanyStockStructure> stocks = em.createQuery(jpql.toString()).setParameter(1, stock.getStatisticsDatetime()).getResultList();
		return stocks.size() > 0;
    	
    }

}
