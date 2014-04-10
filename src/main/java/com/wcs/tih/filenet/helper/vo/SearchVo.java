package com.wcs.tih.filenet.helper.vo;

public class SearchVo extends Vo
{
  private String keyword;
  private String pattern;
  private String category;

  public String getKeyword()
  {
    return this.keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public String getPattern() {
    return this.pattern;
  }

  public void setPattern(String pattern) {
    this.pattern = pattern;
  }

  public String getCategory() {
    return this.category;
  }

  public void setCategory(String category) {
    this.category = category;
  }
}