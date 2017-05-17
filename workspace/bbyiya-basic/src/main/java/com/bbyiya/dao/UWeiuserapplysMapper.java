package com.bbyiya.dao;

import java.util.List;

import com.bbyiya.model.UWeiuserapplys;
import com.bbyiya.vo.user.UWeiUserSearchParam;

public interface UWeiuserapplysMapper {
    int deleteByPrimaryKey(Long userid);

    int insert(UWeiuserapplys record);

    int insertSelective(UWeiuserapplys record);

    UWeiuserapplys selectByPrimaryKey(Long userid);

    int updateByPrimaryKeySelective(UWeiuserapplys record);

    int updateByPrimaryKey(UWeiuserapplys record);
    /**
     * 查询流量主申请列表
     * @param param
     * @return
     */
    List<UWeiuserapplys> findWeiUserApplylist(UWeiUserSearchParam param);
}