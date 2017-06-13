package com.bbyiya.cts.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.cts.service.ICtsMusicService;
import com.bbyiya.cts.vo.MusicAddParam;
import com.bbyiya.dao.SMusicsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.SMusics;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.Page;

@Service("ctsMusicService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class CtsMusicServiceImpl implements ICtsMusicService{
	@Autowired
	private SMusicsMapper musicsMapper;
	
	public ReturnModel addOrEdit_SMusics(SMusics model){
		ReturnModel rqModel=new ReturnModel();
		try {
			if(model.getMusicid()!=null&&model.getMusicid()>0){
				musicsMapper.updateByPrimaryKeySelective(model);
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setStatusreson("修改成功");  
			}else {
				musicsMapper.insert(model);
				rqModel.setStatu(ReturnStatus.Success);
				rqModel.setStatusreson("添加成功"); 
			}
		} catch (Exception e) {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson(e.getMessage());
		}
		return rqModel;
	}
	
	public Page<SMusics> find_SMusicsResult(MusicAddParam param,int pageIndex,int pageSize){
		
		return null;	
	}
}
