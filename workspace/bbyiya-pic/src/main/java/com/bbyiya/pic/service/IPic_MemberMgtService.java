package com.bbyiya.pic.service;

import com.bbyiya.model.UBranchusers;
import com.bbyiya.vo.ReturnModel;

public interface IPic_MemberMgtService {
	/**
	 * ��ȡӰ¥���ڲ�Ա���б�
	 * @param branchUserId
	 * @return
	 */
	ReturnModel findBranchUserslistByBranchUserId(Long branchUserId);
	/**
	 * ���Ӱ¥�� ������Ա
	 * @param branchUserId
	 * @param param
	 * @return
	 */
	ReturnModel addBranchUser(Long branchUserId,UBranchusers param);
	
	ReturnModel delBranchUser(Long branchUserId,Long userId);
}
