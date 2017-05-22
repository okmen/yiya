package com.bbyiya.pic.service.ibs;

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

	
}
