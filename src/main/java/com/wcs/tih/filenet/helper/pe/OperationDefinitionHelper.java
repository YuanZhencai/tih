package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWOperationDefinition;
import filenet.vw.api.VWParameterDefinition;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.wcs.tih.filenet.helper.pe.util.ToStringUtil;

public class OperationDefinitionHelper
{
  public static String vwString(VWOperationDefinition opd)
  {
    try
    {
      ToStringBuilder b = ToStringUtil.createBuilder(opd);
      b.append("name", opd.getName());
      b.append("description", opd.getDescription());
      b.append("== parameters ==");
      VWParameterDefinition[] pds = opd.getParameterDefinitions();
      for (int i = 0; i < pds.length; ++i) {
        b.append(ParameterDefinitionHelper.vwString(pds[i]));
      }
      return b.toString();
    } catch (VWException vwe) {
      throw new P8BpmException(vwe);
    }
  }
}