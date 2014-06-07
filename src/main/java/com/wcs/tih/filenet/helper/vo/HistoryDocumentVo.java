package com.wcs.tih.filenet.helper.vo;

public class HistoryDocumentVo
{
  private String documentTitle;
  private String versionStatus;
  private String majorVersionNumber;
  private String minorVersionNumber;
  private String lastModifier;
  private String mimeType;
  private String dateLastModified;
  private String id;

  public String getId()
  {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDocumentTitle() {
    return this.documentTitle;
  }

  public void setDocumentTitle(String documentTitle) {
    this.documentTitle = documentTitle;
  }

  public String getVersionStatus() {
    return this.versionStatus;
  }

  public void setVersionStatus(String versionStatus) {
    this.versionStatus = versionStatus;
  }

  public String getMajorVersionNumber() {
    return this.majorVersionNumber;
  }

  public void setMajorVersionNumber(String majorVersionNumber) {
    this.majorVersionNumber = majorVersionNumber;
  }

  public String getMinorVersionNumber() {
    return this.minorVersionNumber;
  }

  public void setMinorVersionNumber(String minorVersionNumber) {
    this.minorVersionNumber = minorVersionNumber;
  }

  public String getLastModifier() {
    return this.lastModifier;
  }

  public void setLastModifier(String lastModifier) {
    this.lastModifier = lastModifier;
  }

  public String getDateLastModified() {
    return this.dateLastModified;
  }

  public void setDateLastModified(String dateLastModified) {
    this.dateLastModified = dateLastModified;
  }

  public String getMimeType() {
    return this.mimeType;
  }

  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }
}