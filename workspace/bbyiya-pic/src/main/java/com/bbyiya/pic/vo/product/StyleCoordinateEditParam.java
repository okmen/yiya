package com.bbyiya.pic.vo.product;

import java.io.Serializable;

import com.bbyiya.model.PStylecoordinateitem;

public class StyleCoordinateEditParam implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long coId; 
	private Long styleId;
	/**
	 * 1��ʾ��ͼ��2����ͼ
	 */
	private int type;
	private String typeName;
	/**
	 * ��ӡ������
	 */
	private PStylecoordinateitem numberMod;
	/**
	 * ͼƬ����
	 */
	private PStylecoordinateitem picMod;
	/**
	 * ������������
	 */
	private PStylecoordinateitem contentMod;
	
	public PStylecoordinateitem getNumberMod() {
		return numberMod;
	}
	public void setNumberMod(PStylecoordinateitem numberMod) {
		this.numberMod = numberMod;
	}
	public PStylecoordinateitem getPicMod() {
		return picMod;
	}
	public void setPicMod(PStylecoordinateitem picMod) {
		this.picMod = picMod;
	}
	public PStylecoordinateitem getContentMod() {
		return contentMod;
	}
	public void setContentMod(PStylecoordinateitem contentMod) {
		this.contentMod = contentMod;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Long getCoId() {
		return coId;
	}
	public void setCoId(Long coId) {
		this.coId = coId;
	}
	public Long getStyleId() {
		return styleId;
	}
	public void setStyleId(Long styleId) {
		this.styleId = styleId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	

}
