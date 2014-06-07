package com.wcs.tih.feedback.controller.vo;


public class DictPictureVO {


	private String name;
	
	private String code;
	
	private String photo;

	public DictPictureVO() {
		super();
	}

	public DictPictureVO(String name, String code, String photo) {
		super();
		this.name = name;
		this.code = code;
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof  DictPictureVO){
			DictPictureVO vo= (DictPictureVO)obj;
			return vo.getCode() != null && vo.getCode().equals(this.code);
		}else{
			return false;
		}
	}



}
