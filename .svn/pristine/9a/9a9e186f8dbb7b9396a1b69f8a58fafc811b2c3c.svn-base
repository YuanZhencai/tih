package com.wcs.tihm.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import com.wcs.common.consts.DictConsts;
import com.wcs.common.controller.vo.DictVo;
import com.wcs.common.service.CommonService;
import com.wcs.tih.interaction.controller.vo.SmartmstrVo;
import com.wcs.tih.interaction.service.WizardAnswerService;
import com.wcs.tih.model.Smartmstr;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2013 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yuanzhencai@wcs-global.com">Yuan</a>
 */
@Stateless
public class TihmQuestionService {

    @EJB
    private CommonService commonService;
    @EJB
    private WizardAnswerService wizardAnswerService;
    
    
    public List<SmartmstrVo> getQuestionsTops() {
        return wizardAnswerService.getQuestionsTopVos();
    }
    
    public List<DictVo> getTaxTypes(){
        return commonService.getDictByCatKey(DictConsts.TIH_TAX_TYPE);
    }

    public List<SmartmstrVo> searchQuestionsByTaxType(String taxType){
        Smartmstr st = new Smartmstr();
        st.setTaxType(taxType);
        return wizardAnswerService.getQuestionVo(st);
    }
    
    public List<SmartmstrVo> searchQuestionsByTitle(String title){
        Smartmstr st = new Smartmstr();
        st.setName(title);
        return wizardAnswerService.getQuestionVo(st);
    }
}
