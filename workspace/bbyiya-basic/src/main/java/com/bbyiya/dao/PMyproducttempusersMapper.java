package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproducttempusers;

public interface PMyproducttempusersMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PMyproducttempusers record);

    int insertSelective(PMyproducttempusers record);

    PMyproducttempusers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PMyproducttempusers record);

    int updateByPrimaryKey(PMyproducttempusers record);
    
    PMyproducttempusers selectByUserIdAndTempId(@Param("userId") Long userId,@Param("tempId")int tempId);
    /**
     * 获取影楼工作人员异业合作 模板列表
     * @param sunUserId
     * @param status
     * @return
     */
    List<PMyproducttempusers> findTemplistBySunUserId(@Param("userId")Long sunUserId,@Param("status")Integer status);
}