package com.bbyiya.cts.service;

import com.bbyiya.model.SMusics;
import com.bbyiya.vo.ReturnModel;

public interface ICtsMusicService {
	/**
	 * ������ֵ��ֿ�
	 * @param model
	 * @return
	 */
	ReturnModel addOrEdit_SMusics(SMusics model);
}
