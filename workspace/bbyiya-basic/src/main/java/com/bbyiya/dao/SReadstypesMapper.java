package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.SReadstypes;

public interface SReadstypesMapper {
	int deleteByPrimaryKey(Integer readtypeid);

	int insert(SReadstypes record);

	int insertSelective(SReadstypes record);

	SReadstypes selectByPrimaryKey(Integer readtypeid);

	int updateByPrimaryKeySelective(SReadstypes record);

	int updateByPrimaryKey(SReadstypes record);

	/**
	 * 获取读物的分类列表
	 * 
	 * @return
	 */
	List<SReadstypes> findReadsTypelist();
}