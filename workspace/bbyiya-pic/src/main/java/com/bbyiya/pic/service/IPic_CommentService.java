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
}
