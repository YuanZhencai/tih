package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWTransferResult;
import filenet.vw.api.VWWorkflowDefinition;

public class WorkflowHelper
{
  public static boolean isTransfered(String wfDefName, VWSession vwSession)
    throws VWException
  {
    String[] workClassNames = vwSession.fetchWorkClassNames(false);
    for (int i = 0; i < workClassNames.length; ++i)
    {
      if (wfDefName.equals(workClassNames[i]))
      {
        return true;
      }
    }
    return false;
  }

  public static String transferDefinition(VWWorkflowDefinition wfDef, VWSession vwSession)
    throws VWException
  {
    VWTransferResult transferResult = null;
    transferResult = vwSession.transfer(wfDef, null, true, false);
    if (transferResult.success())
    {
      return transferResult.getVersion();
    }

    throw new P8BpmException("The following transfer errors occured:", 
      transferResult.getErrors());
  }
}