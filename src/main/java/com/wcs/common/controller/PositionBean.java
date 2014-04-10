package com.wcs.common.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.wcs.base.service.LoginService;
import com.wcs.common.controller.vo.PositionCompanyVO;
import com.wcs.common.model.Position;
import com.wcs.common.model.Positionorg;
import com.wcs.common.service.PositionService;
import com.wcs.tih.util.ValidateUtil;

/**
 * Project: tih
 * Description: position backing bean.
 * Copyright (c) 2012 Wilmar Consultancy Services
 * All Rights Reserved.
 * @author <a href="mailto:guanluyong@wcs-global.com">Mr.Guan</a>
 */
@ManagedBean
@ViewScoped
public class PositionBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @EJB 
    private LoginService loginService;
    
    // 当前操作的 position
    private Position position;
    // position option service
//    @Transient
    @EJB
    private PositionService positionService;
    // query condition
    private Map<String, String> query = new HashMap<String, String>(4);
    // position query list
    private LazyDataModel<Position> positionLazyModel;
    
    // belong's company list
    private LazyDataModel<PositionCompanyVO> companyLazyModel;
    // position company VO
    private PositionCompanyVO[] positionCompany;
    // current position company
    private List<PositionCompanyVO> belongedCompany;
    // 缓存list
    private List<PositionCompanyVO> cacheBelongedCompany = new ArrayList<PositionCompanyVO>();
    
    // not belong this position's company query Map
    private Map<String, String> companyQuery = new HashMap<String, String>(4);
    // other company list
    private List<PositionCompanyVO> notBelongedCompany;
    // not belong this position's company VO(selected)
    private PositionCompanyVO[] notPositionCompany;
    
    // constructor
    public PositionBean() {}
    
    @PostConstruct public void initPosition() {
        initAddPosition();
        search();
    }
    
    public void reset() {
        query.clear();
    }
    
    private class PositionLazyDataModel extends LazyDataModel<PositionCompanyVO> {
        private static final long serialVersionUID = 1L;
        private List<PositionCompanyVO> list;
        
        public PositionLazyDataModel(List<PositionCompanyVO> list) {
            this.list = list;
        }
        
        public List<PositionCompanyVO> load(int arg0, int arg1, String arg2, SortOrder arg3, Map<String, String> arg4) {
            setRowCount(list.size());
            if(getRowCount() > arg1) {
                try {
                    return list.subList(arg0, arg0 + arg1);
                } catch (IndexOutOfBoundsException e) {
                    return list.subList(arg0, arg0 + (getRowCount() % arg1));
                }
            } else {
                return list;
            }
        }

        public PositionCompanyVO getRowData(String rowKey) {
            PositionCompanyVO pvo = null;
            for(PositionCompanyVO p : list) {
                if(rowKey.equals(String.valueOf(p.getId()))) {
                    pvo = p;
                    break;
                }
            }
            return pvo;
        }

        public Object getRowKey(PositionCompanyVO p) {
            return p.getId();
        }
        
    }
    
    /**
     * < p>Description: query positions</p>
     */
    public void search() {
        positionLazyModel = new LazyDataModel<Position>() {
            private static final long serialVersionUID = 1L;

            public List<Position> load(int arg0, int arg1, String arg2, SortOrder arg3, Map<String, String> arg4) {
                List<Position> list = positionService.searchPosition(query);
                setRowCount(list.size());
                if(getRowCount() > arg1) {
                    try {
                        return list.subList(arg0, arg0 + arg1);
                    } catch (IndexOutOfBoundsException e) {
                        return list.subList(arg0, arg0 + (getRowCount() % arg1));
                    }
                } else {
                    return list;
                }
            }
            
        };
    }
    
    public void initAddPosition() {
        position = new Position();
        position.setDefunctInd("N");
        position.setSysInd("N");
    }
    
    /**
     * <p>Description: add an new position</p>
     */
    public void addPosition() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(! (ValidateUtil.validateRequiredAndMax(context, position.getName(), "岗位名称：", 20)
                & ValidateUtil.validateRequiredAndMax(context, position.getCode(), "岗位编码：", 100)
                & ValidateUtil.validateMaxlength(context, position.getDesc(), "岗位描述：", 200))){
        	return;
        }
        
        position.setId(null);
        position.setCreatedBy(loginService.getCurrentUserName()); // set login name
        position.setCreatedDatetime(new Date());
        position.setUpdatedBy(loginService.getCurrentUserName());
        position.setUpdatedDatetime(new Date());
        if(positionService.isNameExists(position)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "添加岗位：", "操作失败，岗位名称重复。"));
            return;
        }
        if(positionService.isCodeExists(position)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "添加岗位：", "操作失败，岗位编码重复。"));
            return;
        }
        positionService.insertPosition(position);
        search();
        RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "添加岗位：", "操作成功."));
    }
    
    /**
     * <p>Description: modify an exists position</p>
     */
    public void updatePosition() {
        FacesContext context = FacesContext.getCurrentInstance();
        if(!(ValidateUtil.validateRequiredAndMax(context, position.getName(), "岗位名称：", 20)
                & ValidateUtil.validateRequiredAndMax(context, position.getCode(), "岗位编码：", 100)
                & ValidateUtil.validateMaxlength(context, position.getDesc(), "岗位描述：", 200))){
        	return;
        }
        
        position.setUpdatedBy(loginService.getCurrentUserName());
        position.setUpdatedDatetime(new Date());
        if(positionService.isNameExists(position)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "编辑岗位：", "操作失败，岗位名称重复。"));
            return;
        }
        if(positionService.isCodeExists(position)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "编辑岗位：", "操作失败，岗位编码重复。"));
            return;
        }
        positionService.updatePosition(position);
        search();
        RequestContext.getCurrentInstance().addCallbackParam("issucc", "yes");
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "编辑岗位：", "操作成功."));
    }
    
    /**
     * <p>Description: selected</p>
     */
    public void onSelectedPosition(SelectEvent event) {
        this.position = (Position) event.getObject();
    }
    
    /**
     * <p>Description: null implements</p>
     */
    public void editBelong() {
        
    }
    
    /**
     * <p>Description: find current position's company which belong this</p>
     */
    public void searchBelongCompany() {
        belongedCompany = positionService.findPositionCompanys(position.getId());
        for(PositionCompanyVO p : belongedCompany) {
            cacheBelongedCompany.add(p);
        }
        
        if(!belongedCompany.isEmpty()) {
            positionCompany = new PositionCompanyVO[belongedCompany.size()];
            int i = 0;
            for(PositionCompanyVO pc : belongedCompany) {
                positionCompany[i ++] = pc;
            }
        }
        companyLazyModel = new PositionLazyDataModel(belongedCompany);
    }
    
    /**
     * <p>Description: find other not belong this position's company</p>
     */
    public void searchOtherCompany() {
        notBelongedCompany = positionService.findCompanys(position, companyQuery);
    }
    
    /**
     * <p>Description: merge other company</p>
     */
    public void mergeVO() {
        if(notPositionCompany == null || notPositionCompany.length == 0) {
            return;
        }
        // add
        if(positionCompany == null) {
            positionCompany = new PositionCompanyVO[0];
        }
        
        // re count notPositionCompany's length
        List<PositionCompanyVO> tempNot = new ArrayList<PositionCompanyVO>();
        for(PositionCompanyVO pc : notPositionCompany) {
            boolean f = false;
            for(PositionCompanyVO p : belongedCompany) {
                if(p.getOid().equals(pc.getOid())) {
                    f = true;
                    break;
                }
            }
            if(!f) {
                belongedCompany.add(pc);
                tempNot.add(pc);
            }
        }
        if(tempNot.isEmpty()) {
            return;
        }
        int i = positionCompany.length;
        positionCompany = 
            Arrays.copyOf(positionCompany, i + tempNot.size());
        
        for(PositionCompanyVO pc : tempNot) {
            positionCompany[i ++] = pc;
        }
        companyLazyModel = new PositionLazyDataModel(belongedCompany);
    }
    /**
     * <p>Description: Before add position and org , clear lazy model</p>
     */
    public void beforeAddPositionorg() {
        companyQuery.clear();
        setNotBelongedCompany(null);
        notPositionCompany = null;
    }
    /**
     * <p>Description: add position company relations</p>
     */
    public void addPositionorg() {
        List<Positionorg> newPositionorg = new ArrayList<Positionorg>();
        Positionorg porg = null;
        for(PositionCompanyVO pc : positionCompany) {
            if(pc.getPositionorgId() == null) {
                porg = new Positionorg();
                porg.setOid(pc.getOid());
                porg.setPosition(position);
                porg.setCreatedBy(loginService.getCurrentUserName()); // login.user
                porg.setCreatedDatetime(new Date());
                porg.setDefunctInd("N");
                porg.setUpdatedBy(loginService.getCurrentUserName());
                porg.setUpdatedDatetime(new Date());
                newPositionorg.add(porg);
            }
            for(PositionCompanyVO p : belongedCompany) {
                if(p.getOid() == pc.getOid()) {
                    belongedCompany.remove(p);
                    break;
                }
            }
        }
        
        for(PositionCompanyVO p : belongedCompany) {
            boolean f = false;
            for(PositionCompanyVO pc : cacheBelongedCompany) {
                if(p.getOid() == pc.getOid()) {
                    f = true;
                    break;
                }
            }
            if(!f) {
                belongedCompany.remove(p);
            }
        }
        positionService.addPostionorg(newPositionorg, belongedCompany);
    }
    
    /**
     * <p>Description: update remote data</p>
     */
    public void synchData() {
        
    }
    
    // Getter & Setter
    public Map<String, String> getQuery() {
        return query;
    }
    
    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public LazyDataModel<Position> getPositionLazyModel() {
        return positionLazyModel;
    }

    public void setPositionLazyModel(LazyDataModel<Position> positionLazyModel) {
        this.positionLazyModel = positionLazyModel;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public LazyDataModel<PositionCompanyVO> getCompanyLazyModel() {
        return companyLazyModel;
    }

    public void setCompanyLazyModel(LazyDataModel<PositionCompanyVO> companyLazyModel) {
        this.companyLazyModel = companyLazyModel;
    }

    public PositionCompanyVO[] getPositionCompany() {
        return positionCompany;
    }

    public void setPositionCompany(PositionCompanyVO[] positionCompany) {
        this.positionCompany = positionCompany;
    }

    public List<PositionCompanyVO> getBelongedCompany() {
        return belongedCompany;
    }

    public void setBelongedCompany(List<PositionCompanyVO> belongedCompany) {
        this.belongedCompany = belongedCompany;
    }

    public Map<String, String> getCompanyQuery() {
        return companyQuery;
    }

    public void setCompanyQuery(Map<String, String> companyQuery) {
        this.companyQuery = companyQuery;
    }

    public PositionCompanyVO[] getNotPositionCompany() {
        return notPositionCompany;
    }

    public void setNotPositionCompany(PositionCompanyVO[] notPositionCompany) {
        this.notPositionCompany = notPositionCompany;
    }

    public List<PositionCompanyVO> getCacheBelongedCompany() {
        return cacheBelongedCompany;
    }

    public void setCacheBelongedCompany(List<PositionCompanyVO> cacheBelongedCompany) {
        this.cacheBelongedCompany = cacheBelongedCompany;
    }

    public List<PositionCompanyVO> getNotBelongedCompany() {
        return notBelongedCompany;
    }

    public void setNotBelongedCompany(List<PositionCompanyVO> notBelongedCompany) {
        this.notBelongedCompany = notBelongedCompany;
    }
    
}
