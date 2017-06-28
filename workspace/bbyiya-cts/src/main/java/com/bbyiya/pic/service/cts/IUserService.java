package com.bbyiya.pic.service.cts;

import com.bbyiya.vo.ReturnModel;


public interface IUserService {
	/**
	 * ���cts�ڲ��˺�
	 * @param userid
	 * @param phone
	 * @return
	 */
	ReturnModel addCtsUser(Long userid, String phone);
	/**
	 * ɾ��cts�ڲ��˺�
	 * */
	ReturnModel deleteCtsUser(Long userid);
	/**
	 * �ڲ�Ա���˺��б�
	 * @param keywords
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findCtsMemberlist(String keywords, int index, int size);
	
	
}