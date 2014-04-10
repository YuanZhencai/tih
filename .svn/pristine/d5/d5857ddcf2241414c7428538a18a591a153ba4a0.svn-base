/**
 * DictService.java
 * Created: 2012-2-16 下午01:37:43
 */
package com.wcs.common.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;








import com.wcs.base.service.LoginService;
import com.wcs.common.controller.vo.AnnualVo;
import com.wcs.common.controller.vo.DictVo;
import com.wcs.common.model.Dict;
import com.wcs.scheduler.util.DateUtils;

/**
 * <p>Project: tih</p>
 * <p>Description: </p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:chengchao@wcs-global.com">ChengChao</a>
 * 注解Starup,启动程序就立即加载.
 */

@Startup
@Singleton
public class CommonService {
    private Logger logger = LoggerFactory.getLogger(getClass());
	//查询所有语言类 loadDict()使用
	private List<String> langList; 
	
	//语言分类的list MAP集合,开始程序就就获取到. loadDict()得到
	private Map<String, List<Dict>> langKeyDictMap=new  HashMap<String, List<Dict>>();

	//数据库中所有的数据,根据JPQL查出,总的数据
	private List<Dict> dicts=new ArrayList<Dict>();
	
	//专门用于查询getValueByDictCatKey的Value值的map集合.
	private Map<String,Dict> valueMap=new HashMap<String, Dict>();
	
	@PersistenceContext 
	private EntityManager em;
	
	@EJB
	private LoginService loginService;
	
	/**
	 * <p>Description: 系统加载时查询DICT表，将所有defunct_ind!='Y'的记录放到map,key=CODE_CAT+"."+CODE_KEY,value=CODE+VAL并放到application级别的bean中。</p>
	 * @return
	 */
	@PostConstruct
	public void loadDict() {
		this.queryDict();
	}
	
	/**
	 * <p>Description: 一系列的取值,刷新时以及程序刚运行时调用这个方法</p>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void queryDict(){
		//查询出数据库中一共有几种语言
		String langSQL="SELECT distinct lang FROM Dict";
		langList =em.createNativeQuery(langSQL).getResultList();
		
		//循环查询出每种语言的所有数据,然后装如一个MAP集合中去,以语言类型为key,value为所有的数据是一个list集合.
		for(int i=0;i<langList.size();i++){
			String strJPQL="SELECT d FROM Dict d where d.defunctInd <> 'Y' and d.lang = '"+langList.get(i)+"' order by d.lang,d.codeCat,d.seqNo";
			List<Dict> result=em.createQuery(strJPQL).getResultList();
			langKeyDictMap.put( langList.get(i).toString(),result);
		}
		
		String allJQPL="SELECT d FROM Dict d";
		List<Dict> allResult=em.createQuery(allJQPL).getResultList();
		for(int l=0;l<allResult.size();l++){
			//查询出所有的数据,以catPointKey_lang为key,Value为value.Map形式存储,在取值时效率高.
		    String keyData=(allResult.get(l).getCodeCat().toString()+"."+allResult.get(l).getCodeKey().toString()+"."+allResult.get(l).getLang().toString());
			valueMap.put(keyData, allResult.get(l));
		}
	}
	
	/**
	 * <p>Description: 从application级别的bean获取该值，不要直接从数据库获取
	 * 这里是以d.getCodeCat()+"."+d.getCodeKey()来dictMap中获取相应的value值.
	 * </p>
	 * @param catKey
	 * @return
	 */
	public String getValueByDictCatKey(String catPointKey,String lang) {
		Dict dict = getDictByKey(catPointKey, lang);
		return dict == null ? null:dict.getCodeVal();
	}

	/**
	 * <p>Description: 根据cat值获得所有的Dict列表</p>
	 * @param codeCat
	 * @return
	 */
	public List<Dict> getDictByCat(String cat, String lang) {
		// 根据浏览器语言环境选取langKeyMap中的 List<Dict>集合.
		dicts = langKeyDictMap.get(lang);
		List<Dict> listByCat = new ArrayList<Dict>();
		if (dicts != null && !dicts.isEmpty()) {
			for (int i = 0; i < dicts.size(); i++) {
				if (dicts.get(i).getCodeCat().equals(cat)) {
					listByCat.add(dicts.get(i));
				}
			}
		}
		return listByCat;
	}
	
	/**
	 * <p>Description: 根据cat值获得所有的Dict列表</p>
	 * @param codeCat
	 * @return
	 */
	public Map<String ,String> getDictMapByCat(String cat, String lang) {
	    // 根据浏览器语言环境选取langKeyMap中的 List<Dict>集合.
	    dicts = langKeyDictMap.get(lang);
	    HashMap<String ,String> dictMap = new HashMap<String, String>();
	    if (dicts != null && !dicts.isEmpty()) {
	        for (int i = 0; i < dicts.size(); i++) {
	            Dict dict = dicts.get(i);
	            if (dict.getCodeCat().equals(cat)) {
	                dictMap.put(dict.getCodeCat() + "." + dict.getCodeKey(), dict.getCodeVal());
	            }
	        }
	    }
	    return dictMap;
	}
	
	public Dict getDictByKey(String catPointKey, String lang) {
	    return valueMap.get(catPointKey+"."+lang);
	}
	
	public DictVo getDictVoByKey(String key){
	    DictVo dictVo = new DictVo();
	    String codeVal = this.getValueByDictCatKey(key, "zh_CN");
	    dictVo.setCodeKey(key);
	    dictVo.setCodeVal(codeVal);
        return dictVo;
	}
	
	public List<DictVo> getDictByCatKey(String catKey){
	    List<DictVo> dictVos = new ArrayList<DictVo>();
	    DictVo dictVo = null;
	    List<Dict> dicts = this.getDictByCat(catKey, "zh_CN");
	    for (Dict dict : dicts) {
	        dictVo = new DictVo();
	        dictVo.setCodeCat(dict.getCodeCat());
	        dictVo.setCodeKey(dict.getCodeKey());
	        dictVo.setCodeVal(dict.getCodeVal());
	        dictVos.add(dictVo);
        }
        return dictVos;
	}
	
	public Dict findDictByKey(String catPointKey, String lang){
	    Dict dict = new Dict();
	    try {
	        if(catPointKey.contains(".")){
	            int lastIndexOf = catPointKey.lastIndexOf('.');
	            String codeCat = catPointKey.substring(0, lastIndexOf);
	            String codeKey = catPointKey.substring(lastIndexOf + 1, catPointKey.length());
	            Dict findDict = findDict(codeCat, codeKey, lang);
	            if(findDict == null){
	                dict.setCodeVal(catPointKey);
	                dict.setCodeCat(codeCat);
	                dict.setCodeKey(codeKey);
	                dict.setSeqNo(Long.parseLong(codeKey));
	                dict.setLang(lang);
	            }else{
	                dict = findDict;
	            }
	        }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return dict;
	}
	
    public Dict findDict(String cat, String key, String lang){
        Dict dict = null;
        String jpql = "select d from Dict d where d.codeCat = '" + cat + "' and d.codeKey = '" + key + "' and d.lang = '"+ lang +"' order by d.defunctInd";
        try {
            List<Dict> resultList = em.createQuery(jpql).getResultList();
            if(resultList != null && resultList.size() > 0){
                dict = resultList.get(0);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return dict;
    }
	
	public Dict insertDict(Dict dict){
	    try {
	        List<Dict> resultList = em.createQuery("select d from Dict d where d.codeCat = '" + dict.getCodeCat() + "' and d.codeVal = '"+ dict.getCodeVal() +"' and d.lang = '" + dict.getLang() + "' and d.defunctInd <> 'Y'").getResultList();
	        if(resultList != null && resultList.size() > 0){
	            dict = resultList.get(0);
	        }else{
	            Long count = (Long) em.createQuery("select count(d) from Dict d where d.codeCat = '" + dict.getCodeCat() +"' and d.lang = '" + dict.getLang()+ "'").getSingleResult();
	            
	            dict.setCodeKey(String.valueOf(count + 1));
	            dict.setSeqNo(count + 1);
	            
	            dict.setSysInd("N");
	            dict.setDefunctInd("N");
	            dict.setRemarks("user");
	            String currentUserName = loginService.getCurrentUserName();
	            dict.setCreatedBy(currentUserName);
	            dict.setCreatedDatetime(new Date());
	            dict.setUpdatedBy(currentUserName);
	            dict.setUpdatedDatetime(new Date());
	            em.persist(dict);
	            this.queryDict();
	        }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
	    return dict;
	}

    public Dict saveDict(Dict dict) {
        try {
            String valSlashCatPointKey = dict.getCodeKey();
            if(valSlashCatPointKey != null){
                if(valSlashCatPointKey.contains("/")){
                    String[] split = valSlashCatPointKey.split("/");
                    String codeVal = split[0];
                    String catPointKey = split[1];
                    if (catPointKey.contains(".")) {
                        int lastIndexOf = catPointKey.lastIndexOf('.');
                        String codeCat = catPointKey.substring(0, lastIndexOf);
                        String codeKey = catPointKey.substring(lastIndexOf + 1, catPointKey.length());
                        Dict findDict = findDict(codeCat, codeKey, dict.getLang());
                        if(findDict == null || !"N".equals(findDict.getDefunctInd())){
                            dict.setCodeVal(codeVal);
                            dict.setCodeCat(codeCat);
                            dict.setSeqNo(Long.parseLong(codeKey));
                            dict = insertDict(dict);
                        }else{
                            dict = findDict;
                        }
                    }
                        
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return dict;
    }
    
    public List<Dict> completeByCat(String queryCodeVal, String cat, String lang){
        List<Dict> suggestions = new ArrayList<Dict>();
        try {
            List<Dict> catDicts= getDictByCat(cat,lang);
            queryCodeVal = queryCodeVal.trim();
            Dict dict = new Dict();
            if(!"".equals(queryCodeVal)){
                dict.setCodeVal(queryCodeVal);
                dict.setCodeCat(cat);
                Long random = random();
                dict.setCodeKey(String.valueOf(random));
                dict.setSeqNo(random);
                dict.setLang(lang);
                dict.setDefunctInd("Y");
                suggestions.add(dict);
            }

            for(Dict d : catDicts) {
                String codeVal = d.getCodeVal().trim();
                if(queryCodeVal.equals(codeVal)){
                	suggestions.remove(dict);
                }
                
                if(codeVal.startsWith(queryCodeVal) || codeVal.endsWith(queryCodeVal)){
                    suggestions.add(d);  
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return suggestions;  
    }
    
    public long random() {
        Random r = new Random(System.nanoTime());
        return r.nextLong();
    }
    
    public List<AnnualVo> getAnnualsBy(Date selectedDate) throws ParseException{
    	String selectedAnnaul = null;
    	if(selectedDate == null || "".equals(selectedDate)){
    		selectedAnnaul = DateUtils.getYear();
    	} else {
    		selectedAnnaul = DateUtils.format(selectedDate, "yyyy");
    	}
    	List<AnnualVo> annualVos= new ArrayList<AnnualVo>();
    	Long annual = Long.valueOf(selectedAnnaul) - 10;
    	AnnualVo anualVo = null;
    	for (int i = 0; i < 20; i++) {
    		String itemLabel = (annual + i) + "";
    		anualVo = new AnnualVo();
			anualVo.setItemLabel(itemLabel);
			anualVo.setItemValue(DateUtils.parse(itemLabel, "yyyy"));
			annualVos.add(anualVo);
		}
		return annualVos;
    }
	   
}
