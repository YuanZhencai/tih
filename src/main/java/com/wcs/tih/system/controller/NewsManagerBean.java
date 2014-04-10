package com.wcs.tih.system.controller;

import java.util.Date;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wcs.base.service.LoginService;
import com.wcs.common.util.PageModel;
import com.wcs.tih.homepage.service.RssService;
import com.wcs.tih.model.Newschannelmstr;
import com.wcs.tih.system.service.NewsManagerService;

@ManagedBean
@ViewScoped
public class NewsManagerBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
	@EJB 
	private NewsManagerService newsManagerService;
	@EJB 
	private LoginService loginService;
	@EJB 
	private RssService rssService;
	private LazyDataModel<Newschannelmstr> lazyModel;
	
	private Newschannelmstr updateNews=new Newschannelmstr();
	private boolean flag; 
	
	public NewsManagerBean(){
	}

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public Newschannelmstr getUpdateNews() {
		return updateNews;
	}
	public void setUpdateNews(Newschannelmstr updateNews) {
		this.updateNews = updateNews;
	}
	public NewsManagerService getNewsManagerService() {
		return newsManagerService;
	}
	public void setNewsManagerService(NewsManagerService newsManagerService) {
		this.newsManagerService = newsManagerService;
	}
	public LazyDataModel<Newschannelmstr> getLazyModel() {
		return lazyModel;
	}
	public void setLazyModel(LazyDataModel<Newschannelmstr> lazyModel) {
		this.lazyModel = lazyModel;
	}
	private Newschannelmstr searchContion=new Newschannelmstr();
	public Newschannelmstr getSearchContion() {
		return searchContion;
	}
	public void setSearchContion(Newschannelmstr searchContion) {
		this.searchContion = searchContion;
	}
	@PostConstruct
	public void searchNews(){
	    
	    if(this.getSearchContion().getName()!=null&&this.getSearchContion().getName().length()>50){
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "连接名称不能超过50个字符t", ""));
	        return;
	    }
	    if(this.getSearchContion().getRss()!=null&&this.getSearchContion().getRss().length()>50){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "RSS地址不能超过50个字符t", ""));
            return;
        }
	    
		this.lazyModel=new PageModel(this.newsManagerService.searchNews(this.searchContion), false);
	}
	public void resetForm(){
		this.searchContion=new Newschannelmstr();
	}
	public void selectSingle(){
		flag=true;
	}
	public void saveOrUpdateNews(ActionEvent actionEvent){
		ResourceBundle regexrb = ResourceBundle.getBundle("regex");
		RequestContext context = RequestContext.getCurrentInstance();
		String message = "no";
		FacesMessage msg = null;
		boolean flag = true;
		if (this.updateNews.getName() == null
				|| this.updateNews.getName().trim().equals("")) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "频道名称：", "不能为空");
			flag = false;
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else if(this.updateNews.getName().length()>20){
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "频道名称：", "长度不能超过20");
			flag = false;
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (this.updateNews.getRss() == null
				|| this.updateNews.getRss().equals("")) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "RSS地址：",
					"不能为空");
			flag = false;
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else if(this.updateNews.getRss().length()>100){
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "RSS地址：", "长度不能超过100");
			flag = false;
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}else if(!this.updateNews.getRss().matches(regexrb.getString("ONLYHTTP"))){
			logger.info("NewsManagerBean.saveOrUpdateNews()");
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "RSS地址：", "不合法");
			flag = false;
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (this.updateNews.getPageCount() <= 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "每页大小：",
					"不能小于0");
			flag = false;
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (this.updateNews.getPriority() <= 0) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "优先级：", "不能为空");
			flag = false;
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if (this.newsManagerService.countVidate(this.updateNews) >= 10) {
			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"对不起，最多只能维护10条新闻频道记录！", "");
			flag = false;
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		if(this.updateNews.getKeywords()!=null&&!"".equals(this.updateNews.getKeywords().trim())){
			if(this.updateNews.getKeywords().length()>50){
				msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "过滤字：", "长度不能超过50");
				flag = false;
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		}
		if (flag) {
			growl = true;
			message = "yes";
			this.updateNews.setUpdatedBy(loginService.getCurrentUsermstr().getAdAccount());
			this.updateNews.setUpdatedDatetime(new Date());
			this.newsManagerService.saveOrUpdateNews(this.updateNews, this.flag, loginService.getCurrentUsermstr().getAdAccount());

			msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "保存成功，请查询确认", "");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		searchNews();
		context.addCallbackParam("addInfo", message); 
	}
	private boolean growl=false;
	public boolean isGrowl() {
		return growl;
	}

	public void setGrowl(boolean growl) {
		this.growl = growl;
	}

	public void insertDiaLog(){
		this.flag=false;
		this.updateNews.setPageCount(5);
		this.updateNews.setUpdatedDatetime(new Date());
		this.updateNews.setUpdatedBy("1");
		this.updateNews=new Newschannelmstr();
		this.updateNews.setDefunctInd("N");
	}
	
    public void testLink() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean b = rssService.testRssSource(updateNews.getRss());
        if (b) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "测试连接成功", "该RSS地址可用，请应用刷新！"));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "测试连接失败", "由于网络原因测试连接失败，请多次尝试测试，如果多次尝试失败，那么该RSS地址不可用，请输入重新输入新的正确的RSS地址！"));
        }
    }
	
    public void userRefresh() {
        FacesContext context = FacesContext.getCurrentInstance();
        boolean b = rssService.refreshRssByChannel(updateNews);
        if (b) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "应用/刷新成功", "请查看首页新闻频道对应的数据，并确认！"));
        } else {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "应用/刷新失败", "由于网络原因，请多次尝试！"));
        }
    }
    
    public void refreshAllRss() {
        rssService.refreshAllRss();
    }
    
}
