package com.bbyiya.dao;

import com.bbyiya.model.RAreaplansagentprice;

public interface RAreaplansagentpriceMapper {
    int deleteByPrimaryKey(Integer areaid);

    int insert(RAreaplansagentprice record);
    /**
     * 新增代理区域返回代理单元Id
     * @param record
     * @return
     */
    int insertResultId(RAreaplansagentprice record);

    int insertSelective(RAreaplansagentprice record);

    RAreaplansagentprice selectByPrimaryKey(Integer areaid);

    int updateByPrimaryKeySelective(RAreaplansagentprice record);

    int updateByPrimaryKey(RAreaplansagentprice record);
}