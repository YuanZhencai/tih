package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWFieldType;
import filenet.vw.api.VWModeType;
import filenet.vw.api.VWParameterDefinition;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.wcs.tih.filenet.helper.pe.util.ToStringUtil;

public class ParameterDefinitionHelper
{
  public static String vwString(VWParameterDefinition pd)
  {
    try
    {
      ToStringBuilder b = ToStringUtil.createBuilder(pd);
      b.append("name", pd.getName());
      b.append("type", VWFieldType.getLocalizedString(pd.getDataType()));
      b.append("mode", VWModeType.getLocalizedString(pd.getMode()));
      b.append("description", pd.getDescription());
      b.append("value", pd.getValue());
      return b.toString();
    } catch (VWException vwe) {
      throw new P8BpmException(vwe);
    }
  }
}