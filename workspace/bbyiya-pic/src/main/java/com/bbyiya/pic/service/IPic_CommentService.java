package com.bbyiya.pic.service;

import java.util.List;

import com.bbyiya.model.PCommentstemp;
import com.bbyiya.model.PMyproductcomments;
import com.bbyiya.vo.ReturnModel;

public interface IPic_CommentService {

	/**
	 * 评论模板提示信息
	 * @param productId
	 * @return
	 */
	List<PCommentstemp> findCommentResultList(Long productId);
	
	/**
	 * 作品评论
	 * @param userId 评论者userid
	 * @param param
	 * @return
	 */
	ReturnModel  addPinglun(Long userId, PMyproductcomments param);
	/**
	 * 获取作品评论列表
	 * @param userid
	 * @param cartId
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findCommentsList(Long userid, Long cartId,int index,int size);
	/**
	 * 获取作品评论者 头像列表
	 * @param cartId
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findCommentsHeadImgList(Long cartId,int index,int size);
	/**
	 * 编辑 提示评论分类（新增、修改）；提示评论（修改、新增）
	 * @param userId
	 * @param param
	 * @return
	 */
	ReturnModel modify_Comments(Long userId,PCommentstemp param);
	
	/**
	 * 删除 评论提示
	 * @param userId
	 * @param tipId
	 * @return
	 */
	ReturnModel delTip(Long userId,Integer tipId);
	/**
	 * 删除 评论分类
	 * @param userId
	 * @param commentClassId
	 * @return
	 */
	ReturnModel delCommentClass(Long userId,Integer commentClassId);
}
