package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.UCashlogs;
import com.bbyiya.vo.user.UCashlogResult;

public interface UCashlogsMapper {
    int deleteByPrimaryKey(Long cashlogid);

    int insert(UCashlogs record);

    int insertSelective(UCashlogs record);

    UCashlogs selectByPrimaryKey(Long cashlogid);

    int updateByPrimaryKeySelective(UCashlogs record);

    int updateByPrimaryKey(UCashlogs record);
    
    /**
     * 获取用户 预存款 流水
     * @param userId
     * @param type
     * @return
     */
    List<UCashlogs> findCashlogsByUserId(@Param("userId")Long userId,@Param("type")Integer type);
    /**
     *  获取用户 预存款 流水
     * @param userId
     * @param type
     * @return
     */
    List<UCashlogResult> findCashlogResultByUserId(@Param("userId")Long userId,@Param("type")Integer type);
}