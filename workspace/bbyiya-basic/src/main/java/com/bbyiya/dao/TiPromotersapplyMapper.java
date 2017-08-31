package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.TiPromotersapply;
import com.bbyiya.vo.calendar.TiAgentSearchParam;
import com.bbyiya.vo.calendar.TiPromoterApplyVo;

public interface TiPromotersapplyMapper {
    int deleteByPrimaryKey(Long promoteruserid);

    int insert(TiPromotersapply record);

    int insertSelective(TiPromotersapply record);

    TiPromotersapply selectByPrimaryKey(Long promoteruserid);

    int updateByPrimaryKeySelective(TiPromotersapply record);

    int updateByPrimaryKey(TiPromotersapply record);
    
    TiPromoterApplyVo getTiPromoterapplyVOById(Long promoteruserid);
    
    List<TiPromoterApplyVo> findTiPromoterapplyVOList(TiAgentSearchParam param);
}