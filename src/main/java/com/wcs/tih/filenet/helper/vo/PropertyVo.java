package com.wcs.tih.filenet.helper.vo;

public class PropertyVo extends Vo
{
  private String name;
  private String value;
  private String dataType;
  private String symbolicName;

  public String getSymbolicName()
  {
    return this.symbolicName;
  }

  public void setSymbolicName(String symbolicName) {
    this.symbolicName = symbolicName;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getDataType() {
    return this.dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }
}