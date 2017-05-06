package com.bbyiya.dao;

import java.util.List;
import java.util.Map;

//import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PScenebacks;

public interface PScenebacksMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PScenebacks record);

    int insertSelective(PScenebacks record);

    PScenebacks selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PScenebacks record);

    int updateByPrimaryKey(PScenebacks record);
    
//    List<PScenebacks> findScenelistByStyleId(@Param("styleId")Long styleid,@Param("array")String[]ids);
    
    List<PScenebacks> findScenelistByMap(Map<String, Object> map);
}