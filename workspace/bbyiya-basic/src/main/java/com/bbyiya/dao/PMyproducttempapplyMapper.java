package com.bbyiya.dao;

import com.bbyiya.model.PMyproducttempapply;

public interface PMyproducttempapplyMapper {
    int deleteByPrimaryKey(Long tempapplyid);

    int insert(PMyproducttempapply record);

    int insertSelective(PMyproducttempapply record);

    PMyproducttempapply selectByPrimaryKey(Long tempapplyid);

    int updateByPrimaryKeySelective(PMyproducttempapply record);

    int updateByPrimaryKey(PMyproducttempapply record);
}