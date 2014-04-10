package com.wcs.tih.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "RSS")
public class Rss extends com.wcs.base.model.IdEntity implements Serializable {

	private Long newschannelmstrId;
	private String link;
	private String title;
	private Date publishedDate;

	public Long getNewschannelmstrId() {
		return newschannelmstrId;
	}

	public void setNewschannelmstrId(Long newschannelmstrId) {
		this.newschannelmstrId = newschannelmstrId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

}
