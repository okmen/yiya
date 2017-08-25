package com.bbyiya.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bbyiya.model.PMyproducttempapply;

public interface PMyproducttempapplyMapper {
    int deleteByPrimaryKey(Long tempapplyid);

    int insert(PMyproducttempapply record);

    int insertSelective(PMyproducttempapply record);

    PMyproducttempapply selectByPrimaryKey(Long tempapplyid);

    int updateByPrimaryKeySelective(PMyproducttempapply record);

    int updateByPrimaryKey(PMyproducttempapply record);
    /**
     * 得到模板待审核的报名人数
     * @param tempid
     * @return
     */
    Integer getNeedCheckApllyCountByTemp(@Param("tempid") Integer tempid,@Param("status") Integer status);
    /**
     * 根据模板ID及状态得到待审核用户列表
     * @param tempid
     * @return
     */
    List<PMyproducttempapply> findMyProducttempApplyList(@Param("tempid") Integer tempid,@Param("status") Integer status);
    
    /**
     * 得到已参与中的用户列表
     * @param tempid
     * @return
     */
    List<PMyproducttempapply> findMyProducttempApplyInList(@Param("tempid") Integer tempid, @Param("statuslist")List<Integer> statuslist);
    /**
     * 根据tempid 、userid获取用户申请情况
     * @param tempid
     * @param userId
     * @return
     */
    PMyproducttempapply getMyProducttempApplyByUserId(@Param("tempid")Integer tempid,@Param("userid")Long userId);
    /**
     * 根据cartId获取活动申请记录
     * @param cartid
     * @return
     */
    PMyproducttempapply getMyProducttempApplyByCartId(@Param("cartId")Long cartid);
    
    /**
     * 获取用户参与活动的模板申请列表
     * @param userId
     * @return
     */
    List<PMyproducttempapply> findMyProducttempApplyByUserId(@Param("userid")Long userId);
    /**
     * 我参与的活动未读消息数
     * @param userId
     * @return
     */
    int countMyProducttempApplyByUserIdNews(@Param("userid")Long userId);
    
    /**
     * 得到模板待审核的报名人数
     * @param tempid
     * @return
     */
    Integer getMaxSortByTempId(@Param("tempid") Integer tempid);
    
    /**
     * 根据活动ID得到活动每天的报名人数
     * @param tempid
     * @return
     */
    Integer countTempApplyByDay(@Param("tempid") Integer tempid,@Param("startTimeStr") String startTimeStr,@Param("endTimeStr") String endTimeStr);
   
    /**
     * 根据活动ID得到活动每天的完成人数
     * @param tempid
     * @return
     */
    Integer countTempCompleteByDay(@Param("tempid") Integer tempid,@Param("startTimeStr") String startTimeStr,
    		@Param("endTimeStr") String endTimeStr);
}