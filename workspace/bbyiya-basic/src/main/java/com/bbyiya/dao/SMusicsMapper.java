package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.SMusics;
import com.bbyiya.vo.music.MusicResult;

public interface SMusicsMapper {
    int deleteByPrimaryKey(Integer musicid);

    int insert(SMusics record);

    int insertSelective(SMusics record);

    SMusics selectByPrimaryKey(Integer musicid);

    int updateByPrimaryKeySelective(SMusics record);

    int updateByPrimaryKey(SMusics record);
    
    /**
     * 根据音乐类型获取音乐列表（from the music storage）
     * @param typeId
     * @return
     */
    List<MusicResult>  findMusiclistByTypeId(@Param("typeId")Integer typeId);
}