package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiPromoteradvertinfo;

public interface TiPromoteradvertinfoMapper {
    int deleteByPrimaryKey(Integer advertid);

    int insert(TiPromoteradvertinfo record);

    int insertSelective(TiPromoteradvertinfo record);

    TiPromoteradvertinfo selectByPrimaryKey(Integer advertid);

    int updateByPrimaryKeySelective(TiPromoteradvertinfo record);

    int updateByPrimaryKey(TiPromoteradvertinfo record);
    /**
     * 新增并返回主键Id
     * @param record
     * @return
     */
    int insertReturnId(TiPromoteradvertinfo record);
    
    /**
     * 重置默认广告
     * @param promoteruserid
     * @return
     */
    int setDefaultByPromoterUserId(@Param("promoteruserid")Long promoteruserid,@Param("advertid")Integer advertid,@Param("isdefault")Integer isdefault);
    
    /**
     * 得到广告列表
     * @param promoteruserid
     * @param keywords
     * @return
     */
    List<TiPromoteradvertinfo> selectListByPromoterUserId(@Param("promoteruserid")Long promoteruserid,@Param("keywords")String keywords);
    /**
     * 获取 广告信息
     * @param userId
     * @return
     */
    TiPromoteradvertinfo getModelByPromoterUserId(@Param("userId")Long userId);
    
    TiPromoteradvertinfo getAdvertByPromoterUserId(@Param("userId")Long userId);

}