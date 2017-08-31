package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.TiProducersapply;
import com.bbyiya.vo.calendar.TiAgentSearchParam;
import com.bbyiya.vo.calendar.TiProducersApplyVo;

public interface TiProducersapplyMapper {
    int deleteByPrimaryKey(Long produceruserid);

    int insert(TiProducersapply record);

    int insertSelective(TiProducersapply record);

    TiProducersapply selectByPrimaryKey(Long produceruserid);

    int updateByPrimaryKeySelective(TiProducersapply record);

    int updateByPrimaryKey(TiProducersapply record);
    
   
    TiProducersApplyVo getTiProducersapplyVOById(Long produceruserid);
    
    /**
     * 条件查询得到生产商申请列表
     * @param param
     * @return
     */
    List<TiProducersApplyVo>findTiProducersapplyVOList(TiAgentSearchParam param);
}