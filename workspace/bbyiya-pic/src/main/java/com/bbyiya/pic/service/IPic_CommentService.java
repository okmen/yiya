package com.bbyiya.pic.service;

import java.util.List;

import com.bbyiya.model.PCommentstemp;
import com.bbyiya.model.PMyproductcomments;
import com.bbyiya.vo.ReturnModel;

public interface IPic_CommentService {

	/**
	 * ����ģ����ʾ��Ϣ
	 * @param productId
	 * @return
	 */
	List<PCommentstemp> findCommentResultList(Long productId);
	
	/**
	 * ��Ʒ����
	 * @param userId ������userid
	 * @param param
	 * @return
	 */
	ReturnModel  addPinglun(Long userId, PMyproductcomments param);
	/**
	 * ��ȡ��Ʒ�����б�
	 * @param userid
	 * @param cartId
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findCommentsList(Long userid, Long cartId,int index,int size);
	/**
	 * �༭ ��ʾ���۷��ࣨ�������޸ģ�����ʾ���ۣ��޸ġ�������
	 * @param userId
	 * @param param
	 * @return
	 */
	ReturnModel modify_Comments(Long userId,PCommentstemp param);
	
	/**
	 * ɾ�� ������ʾ
	 * @param userId
	 * @param tipId
	 * @return
	 */
	ReturnModel delTip(Long userId,Integer tipId);
	/**
	 * ɾ�� ���۷���
	 * @param userId
	 * @param commentClassId
	 * @return
	 */
	ReturnModel delCommentClass(Long userId,Integer commentClassId);
}
