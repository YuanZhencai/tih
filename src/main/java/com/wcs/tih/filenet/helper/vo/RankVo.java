package com.wcs.tih.filenet.helper.vo;

import com.filenet.api.core.Document;

public class RankVo
{
  private Document document;
  private double rank;

  public Document getDocument()
  {
    return this.document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public double getRank() {
    return this.rank;
  }

  public void setRank(double rank) {
    this.rank = rank;
  }
}