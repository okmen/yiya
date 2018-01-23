package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.TiActivityworks;
import com.bbyiya.vo.calendar.TiActivitysWorkVo;

public interface TiActivityworksMapper {
    int deleteByPrimaryKey(Long workid);

    int insert(TiActivityworks record);

    int insertSelective(TiActivityworks record);

    TiActivityworks selectByPrimaryKey(Long workid);

    int updateByPrimaryKeySelective(TiActivityworks record);

    int updateByPrimaryKey(TiActivityworks record);
    
    Integer getCountByActStatus(@Param("actid") Integer actid,@Param("status") Integer status);
    /**
     * 查询活动参与数
     * @param actid
     * @param statusArrs
     * @return
     */
    int countByActIdAndStatus(@Param("actid") Integer actid,@Param("statusArrs") List<Integer> statusArrs);
    /**
     * 活动名额满了，置未拿到名额的作品 活动失败
     * @param actid
     * @param toStatus
     * @param statusArrs
     * @return
     */
    int failActivityWorkByStatus(@Param("actId") Integer actid,@Param("toStatus") Integer toStatus,@Param("statusArrs") List<Integer> statusArrs);
    /**
     * 获取需要分享、公开活动、但是为完成分享
     * @return
     */
    List<TiActivityworks> getActWorkListNeedShared();
    /**
     * 根据活动ID得到活动制作情况
     * @param actid
     * @param status
     * @param keywords
     * @return
     */
    List<TiActivitysWorkVo>findActWorkListByActId(@Param("actid") Integer actid,@Param("status") Integer status,@Param("keywords") String keywords);
    /**
     * 获取用户参与的活动
     * @param actid
     * @param userId
     * @return
     */
    TiActivityworks getActWorkListByActIdAndUserId(@Param("actid") Integer actid,@Param("userId") Long userId);
    /**
     * 获取已经到期的活动作品
     * @return
     */
    List<TiActivityworks> findActworklistExpired();
}