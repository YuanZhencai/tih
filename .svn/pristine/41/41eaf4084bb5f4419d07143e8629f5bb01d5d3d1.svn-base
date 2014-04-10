package com.wcs.tih.filenet.helper.pe;

import filenet.vw.api.VWException;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;
import java.util.List;

public class QueueWorkListService
{
  public static List<VWStepElement> getStepElements(VWSession session, String queueName, String stepName)
    throws VWException
  {
    return QueueHelper.getStepElementsByStepName(session, queueName, stepName);
  }
}