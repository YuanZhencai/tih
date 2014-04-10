package com.wcs.tih.filenet.helper.vo;

public class UserVo extends Vo
{
  private String name;
  private String password;

  public String getName()
  {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}