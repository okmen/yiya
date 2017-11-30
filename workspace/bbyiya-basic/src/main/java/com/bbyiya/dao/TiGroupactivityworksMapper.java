package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiGroupactivityworks;
import com.bbyiya.vo.calendar.TiGroupActivitysWorksVo;

public interface TiGroupactivityworksMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiGroupactivityworks record);

    int insertSelective(TiGroupactivityworks record);

    TiGroupactivityworks selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiGroupactivityworks record);

    int updateByPrimaryKey(TiGroupactivityworks record);
    /**
     * 获取用户参与 某团购的信息
     * @param userId
     * @param gactId 团购Id
     * @return
     */
    TiGroupactivityworks getTiGroupactivityworksByActIdAndUserId(@Param("userId")Long userId,@Param("gactId")Integer gactId);
    /**
     * 获取用户参与 某团购的信息
     * @param userId
     * @param gactId
     * @return
     */
    List<TiGroupactivityworks> findTiGroupactivityworksByActIdAndUserId(@Param("userId")Long userId,@Param("gactId")Integer gactId);
    
    /**
     * 根据活动ID及状态得到各状态的总数
     * @param gactid
     * @param status
     * @return
     */
    Integer getCountByGActStatus(@Param("gactid") Integer gactid,@Param("status") Integer status);
    
    Double getSumPostAgeByGactid(@Param("gactid") Integer gactid,@Param("addresstype") Integer addresstype);
    /**
     * 根据活动ID得到活动参与情况列表
     * @param gactid
     * @param addresstype
     * @param keywords
     * @return
     */
    List<TiGroupActivitysWorksVo>findGroupActWorkListByActId(@Param("gactid") Integer gactid,@Param("addresstype") Integer addresstype,@Param("keywords") String keywords);
    
    /**
     * 得到可自动下单的分销作品
     * @param ordertime
     * @return
     */
    List<TiGroupactivityworks> findCanOrderGroupActWork(@Param("ordertime") Integer ordertime);
    
    /**
     * 得到活动赠送总数
     * @param gactid
     * @param actcpraisecount
     * @return
     */
    Integer getCompltePraiseCountByGactid(@Param("gactid") Integer gactid,@Param("actcpraisecount") Integer actcpraisecount);
    
    /**
     * 得到有效点赞总数
     * @param gactid
     * @return
     */
    Integer getSumPraiseCountByGactid(@Param("gactid") Integer gactid);

}