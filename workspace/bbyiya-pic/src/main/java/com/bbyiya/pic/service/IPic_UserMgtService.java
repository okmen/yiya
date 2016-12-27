package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.OtherLoginParam;

public interface IPic_UserMgtService {
	
	/**
	 * µÚÈý·½µÇÂ¼¡¢×¢²á
	 * @param param
	 * @return
	 */
	ReturnModel otherLogin(OtherLoginParam param);
}
