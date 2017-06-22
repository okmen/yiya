package com.bbyiya.pic.service.ibs;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.vo.ReturnModel;

public interface IIbs_ActivityCodeService {
	/**
	 * ��ӻ��
	 * @param userid
	 * @param title
	 * @param remark
	 * @return
	 */
	ReturnModel addActivityCode(Long userid,MyProductTempAddParam param);
	/**
	 * �õ�tempId�µĻ���б�
	 * @param branchUserId
	 * @param tempid
	 * @param activeStatus
	 * @param keywords
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findMyProductslistForActivityCode(Long branchUserId,
			Integer tempid, Integer activeStatus, String keywords, int index,
			int size);
	/**
	 * ɾ�����
	 * @param codeno
	 * @return
	 */
	ReturnModel deleteActivityCode(String codeno);
	/**
	 * ���û�µ��ѱ��������
	 * @return
	 */
	ReturnModel resetAllTempApplySort(Integer tempid);
	/**
	 * �������
	 * @param tempid
	 * @return
	 */
	ReturnModel getActivityCodeDetail(Integer tempid);
	

	
}
