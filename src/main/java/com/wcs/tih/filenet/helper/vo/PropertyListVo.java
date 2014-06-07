package com.wcs.tih.filenet.helper.vo;

import java.util.List;

public class PropertyListVo extends Vo
{
  private List<PropertyVo> systemList;
  private List<PropertyVo> definitionList;

  public List<PropertyVo> getSystemList()
  {
    return this.systemList;
  }

  public void setSystemList(List<PropertyVo> systemList) {
    this.systemList = systemList;
  }

  public List<PropertyVo> getDefinitionList() {
    return this.definitionList;
  }

  public void setDefinitionList(List<PropertyVo> definitionList) {
    this.definitionList = definitionList;
  }
}