package com.wcs.tih.filenet.helper.vo;

public class PaginationVo extends Vo
{
  private int currentPage;
  private int docsPerPage;
  private int totalPages;
  private int totalDocs;

  public int getCurrentPage()
  {
    return this.currentPage;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public int getDocsPerPage() {
    return this.docsPerPage;
  }

  public void setDocsPerPage(int docsPerPage) {
    this.docsPerPage = docsPerPage;
  }

  public int getTotalPages() {
    return this.totalPages;
  }

  public void setTotalPages(int totalPages) {
    this.totalPages = totalPages;
  }

  public int getTotalDocs() {
    return this.totalDocs;
  }

  public void setTotalDocs(int totalDocs) {
    this.totalDocs = totalDocs;
  }
}