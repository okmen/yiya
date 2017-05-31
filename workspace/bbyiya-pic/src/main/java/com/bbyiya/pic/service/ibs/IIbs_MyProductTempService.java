package com.bbyiya.pic.service.ibs;

import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.vo.ReturnModel;

public interface IIbs_MyProductTempService {
	/**
	 * ���ģ��
	 * @param userid
	 * @param title
	 * @param remark
	 * @return
	 */
	ReturnModel addMyProductTemp(Long userid, String title, String remark,Long productid,int needVerifer,String discription);
	/**
	 * ���û����ģ��
	 * @param type
	 * @param tempid
	 * @return
	 */
	ReturnModel editMyProductTempStatus(int type, int tempid);
	/**
	 * ɾ��ģ��
	 * @param tempid
	 * @return
	 */
	ReturnModel deleteMyProductTemp(int tempid);
	/**
	 * ��ѯģ���б�
	 * @param index
	 * @param size
	 * @param userid
	 * @return
	 */
	ReturnModel findMyProductTempList(int index, int size, Long userid);
	/**
	 * ����ģ���ά��ͼƬ
	 * @param url
	 * @return
	 * @throws Exception
	 */
	ReturnModel saveProductTempRQcode(String url) throws Exception;
	/**
	 * �޸�ģ��
	 * @param userid
	 * @param title
	 * @param remark
	 * @param tempid
	 * @return
	 */
	ReturnModel editMyProductTemp(String title, String remark,
			Integer tempid,int needVerifer,String discription);
	/**
	 * ��ȡӰ¥ģ�������û��б�
	 * @param index
	 * @param size
	 * @param userid
	 * @param tempid
	 * @return
	 */
	ReturnModel getMyProductTempApplyCheckList(int index, int size,
			Long userid, int tempid);
	/**
	 * ���Ӱ¥ģ�������û�
	 * @param userid
	 * @param tempapplyid
	 * @param status  0 �����У�1ͨ����2�ܾ�
	 * @return
	 */
	ReturnModel audit_TempApplyUser(Long userid, Long tempapplyid,
			Integer status);
	/**
	 * Ӱ¥Ա������ģ����Ϣ�б�
	 * @param index
	 * @param size
	 * @param branchUserId
	 * @param tempid
	 * @return
	 */
	ReturnModel find_BranchUserOfTemp(int index, int size, Long branchUserId,
			Integer tempid);
	/**
	 * ����Ա��ģ�帺��Ȩ��
	 * @param userId
	 * @param tempid
	 * @param status
	 * @return
	 */
	ReturnModel setUserTempPermission(Long userId, Integer tempid,
			Integer status);
	/**
	 * ���������ibs��̨����û�������� (���÷���)
	 * @param apply
	 * @return
	 */
	ReturnModel doAcceptOrAutoTempApplyOpt(PMyproducttempapply apply);
	/**
	 * ���ģ�������û���Ʒ�Ƿ�ͨ��
	 * @param userId
	 * @param cartid
	 * @param status
	 * @return
	 */
	ReturnModel audit_TempApplyProduct(Long userId, Long cartid, Integer status,String reason);

	
}
