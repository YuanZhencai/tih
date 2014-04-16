package com.wcs.common.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.StreamedContent;

import com.wcs.common.service.DownloadService;

@ManagedBean(name = "downloadBean")
@ApplicationScoped
public class DownloadBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@EJB
	private DownloadService downloadService;

	public StreamedContent download(String fileId) {
		return downloadService.download(fileId);
	}
}