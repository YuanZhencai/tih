package com.wcs.tih.interaction.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.wcs.common.service.CommonService;
import com.wcs.tih.interaction.controller.vo.SmartmstrVo;
import com.wcs.tih.model.SmartStatisticsmstr;
import com.wcs.tih.model.Smartmstr;
import com.wcs.tih.util.DateUtil;
/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yidongjun@wcs-global.com">yidongjun</a>
 */
@Stateless
public class WizardAnswerService {
	@PersistenceContext
	private EntityManager em;
	@EJB
	private CommonService commonService;
	
	
	public List getQuestion(Smartmstr st, Date beginTime, Date endTime) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select s from Smartmstr s where s.defunctInd = 'N' ");
		if (st != null) {
			if (st.getQuestion()!=null&&!st.getQuestion().trim().equals("")) {
				sb.append(" and s.question like '%").append(st.getQuestion())
						.append("%'");
			}
			if (st.getAnswer()!=null&&!st.getAnswer().trim().equals("")) {
				sb.append(" and s.answer like '%").append(st.getAnswer())
						.append("%'");
			}
			if(st.getTaxType()!=null&&!st.getTaxType().equals("")){
				sb.append(" and s.taxType = '").append(st.getTaxType()).append("'");
			}
			if(st.getRegion()!=null&&!st.getRegion().equals("")){
				sb.append(" and s.region ='").append(st.getRegion()).append("'");
			}
			if(st.getName()!=null&&!st.getName().trim().equals("")){
				sb.append(" and s.name like '%").append(st.getName()).append("%'");
			}
		}
		//赵谦修改东俊的代码.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (beginTime != null) {
            sb.append(" and s.createdDatetime >= '" + sdf.format(beginTime) + " 00:00:00'");
        }
        if (endTime != null) {
            sb.append(" and s.createdDatetime <= '" + sdf.format(DateUtil.getNextDate(endTime)) + " 00:00:00'");
        }
        
		List li =em.createQuery(sb.toString()).getResultList();

		return li;
	}

	public Smartmstr getAnswer(long id){
		
		return id==0? new Smartmstr():(Smartmstr)this.em.createQuery("select s from Smartmstr s where s.id="+id).getResultList().get(0);
	}
	public List  getAttachement(long id,String dictConsts){
		return this.em.createQuery("select s from SmartAttachmentmstr s where s.smartmstr.id = "+id+" and s.type = '"+dictConsts+"' and s.defunctInd = 'N' ").getResultList();
	}
	public List<Smartmstr> getQuestionsTops(){
		Query q=this.em.createQuery("select s from Smartmstr s,SmartStatisticsmstr c where  s.defunctInd = 'N'  and c.smartmstr.id = s.id order by c.clickCount desc");
		q.setFirstResult(0).setMaxResults(10);
		return q.getResultList();
	}
	public void saveOrUpdateCount(long id){
		SmartStatisticsmstr st=new SmartStatisticsmstr();
		String sql="select s from SmartStatisticsmstr s where s.smartmstr.id="+id;
		List li=this.em.createQuery(sql).getResultList();
		if(li.size()==0){
		//	SMART_STATISTICSMSTR
			Smartmstr s=new Smartmstr();
			s.setId(id);
			st.setSmartmstr(s);
			st.setDefunctInd("N");
			st.setUpdatedBy("0");
			st.setUpdatedDatetime(new Date());
			st.setCreatedBy("0");
			st.setClickCount(1);
			st.setCreatedDatetime(new Date());
			this.em.persist(st);
		}else{
			st=(SmartStatisticsmstr)li.get(0);
			st.setClickCount(st.getClickCount()+1);
			this.em.merge(st);
		}
	}
	public List<SmartmstrVo> getQuestionsTopVos(){
	    SmartmstrVo sv = null;
	    List<SmartmstrVo> svs = new ArrayList<SmartmstrVo>();
	    List<Smartmstr> questionsTops = this.getQuestionsTops();
	    for (Smartmstr s : questionsTops) {
	        sv = new SmartmstrVo();
            sv.setSmartmstr(s);
            sv.setTaxType(commonService.getDictVoByKey(s.getTaxType()));
            sv.setRegion(commonService.getDictVoByKey(s.getRegion()));
            svs.add(sv);
        }
        return svs;
	}
	
	public List<SmartmstrVo> getQuestionVo(Smartmstr st){
	    SmartmstrVo sv = new SmartmstrVo();
        List<SmartmstrVo> svs = new ArrayList<SmartmstrVo>();
	    List<Smartmstr> questions = this.getQuestion(st, null, null);
	    for (Smartmstr s : questions) {
	        sv = new SmartmstrVo();
            sv.setSmartmstr(s);
            sv.setTaxType(commonService.getDictVoByKey(s.getTaxType()));
            sv.setRegion(commonService.getDictVoByKey(s.getRegion()));
            svs.add(sv);
        }
        return svs;
	}
	
}
