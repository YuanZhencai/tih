package com.wcs.common.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.common.controller.helper.PageModel;
import com.wcs.common.controller.vo.SynclogSearchVo;
import com.wcs.common.controller.vo.SynclogVo;
import com.wcs.common.service.MDSService;
import com.wcs.common.service.TDSLocal;

/**
 * <p>Project: tih</p>
 * <p>Description: 同步数据</p>
 * <p>Copyright (c) 2012 Wilmar Consultancy Services</p>
 * <p>All Rights Reserved.</p>
 * @author <a href="mailto:yubinfeng@wcs-global.com">喻彬峰</a>
 */
@ManagedBean(name = "dataSynchronousBean")
@ViewScoped
public class DataSynchronousBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    @EJB
    private MDSService mDSService;
    @EJB
    private TDSLocal tDSLocal;
    private LazyDataModel<SynclogVo> lazySynclogVoModel;
    private SynclogSearchVo synclogSearchVo;

    /**
     * <p>Description: 初始化</p>
     */
    @PostConstruct
    public void init() {
        synclogSearchVo =new SynclogSearchVo();
        querySynclog();
    }
    
    public void querySynclog(){
        List<SynclogVo> synclogVos = mDSService.getAllSynclog(synclogSearchVo);
        if (synclogVos != null && synclogVos.size() != 0) {
            lazySynclogVoModel = new PageModel<SynclogVo>(synclogVos, false);
        } else {
            lazySynclogVoModel = null;
        }
    }

    /**
     * <p>Description: MDS到TIH数据一键同步</p>
     */
    public void mdsToTihSynchronous() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            // 同步用户数据
            userSynchronous();
            // 同步组织数据
            organizationSynchronous();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "成功", "一键从MDS同步数据到TIH成功！"));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "成功", "一键从MDS同步数据到TIH失败！"));
        }
        init();
    }
    
    /**
     * <p>Description: TIH到TDS数据一键同步</p>
     */
    public void tihToTdsSynchronous() {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            String msgs=tDSLocal.allSynchronous();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"同步结果", msgs));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"系统异常", "一键从TIH同步数据到TDS失败！"));
        }
        init();
    }

    /**
     * <p>Description: 同步用户数据</p>
     */
    public void userSynchronous() {
        FacesContext context = FacesContext.getCurrentInstance();
        String msgStrs;
        try {
            msgStrs = mDSService.userSynchronous();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"成功", msgStrs));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"失败", e.getMessage()));
        }
    }

    /**
     * <p>Description: 同步组织数据</p>
     */
    public void organizationSynchronous() {
        FacesContext context = FacesContext.getCurrentInstance();
        String msgStrs;
        try {
            msgStrs = mDSService.organizationSynchronous();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"成功", msgStrs));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"失败", e.getMessage()));
        }
    }

    public LazyDataModel<SynclogVo> getLazySynclogVoModel() {
        return lazySynclogVoModel;
    }

    public void setLazySynclogVoModel(LazyDataModel<SynclogVo> lazySynclogVoModel) {
        this.lazySynclogVoModel = lazySynclogVoModel;
    }

    public SynclogSearchVo getSynclogSearchVo() {
        return synclogSearchVo;
    }

    public void setSynclogSearchVo(SynclogSearchVo synclogSearchVo) {
        this.synclogSearchVo = synclogSearchVo;
    }

}
