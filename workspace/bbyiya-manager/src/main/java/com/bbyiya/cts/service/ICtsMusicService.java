package com.bbyiya.cts.service;

import com.bbyiya.cts.vo.MusicAddParam;
import com.bbyiya.model.SMusics;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.Page;

public interface ICtsMusicService {
	/**
	 * ������ֵ��ֿ�
	 * @param model
	 * @return
	 */
	ReturnModel addOrEdit_SMusics(SMusics model);
	/**
	 * ��ȡ�ֿ������б�����ҳ��
	 * @param param ��ѯ����
	 * @param pageIndex ��ǰҳ
	 * @param pageSize ÿҳ����
	 * @return 
	 */
	Page<SMusics> find_SMusicsResult(MusicAddParam param,int pageIndex,int pageSize);
	
}
