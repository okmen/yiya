package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.SReads;
import com.bbyiya.vo.reads.ReadsResult;

public interface SReadsMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(SReads record);

	int insertSelective(SReads record);

	SReads selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SReads record);

	int updateByPrimaryKeyWithBLOBs(SReads record);

	int updateByPrimaryKey(SReads record);

	/**
	 * 根据读物分类id获取读物列表
	 * 
	 * @param typeid
	 * @return
	 */
	List<ReadsResult> findSReadsByTypeId(@Param("typeid") Integer typeid);
}