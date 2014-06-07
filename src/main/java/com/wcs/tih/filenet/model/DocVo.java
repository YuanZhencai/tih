package com.wcs.tih.filenet.model;

import com.filenet.api.core.Document;
import com.wcs.tih.filenet.helper.vo.DocumentVo;

public class DocVo extends DocumentVo
{
  public DocVo(Document doc)
  {
    fromDocument(doc);
  }

  protected void fromCustom(Document paramDocument)
  {
  }
}