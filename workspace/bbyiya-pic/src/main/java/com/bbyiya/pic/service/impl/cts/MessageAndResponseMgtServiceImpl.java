package com.bbyiya.pic.service.impl.cts;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.SysMessageMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UUserresponsesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.SysMessageTypeEnum;
import com.bbyiya.model.SysMessage;
import com.bbyiya.model.UUserresponses;
import com.bbyiya.pic.service.cts.IMessageAndResponseMgtService;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("messageAndResponseMgtServiceImpl")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class MessageAndResponseMgtServiceImpl implements IMessageAndResponseMgtService{

	@Autowired
	private UBranchesMapper branchesMapper;
	
	@Autowired
	private UUserresponsesMapper userresponseMapper;//用户反馈
	
	@Autowired
	private SysMessageMapper sysMessageMapper;//系统消息
	
	
	/**
	 * 添加系统通知
	 * @param branchUserId
	 * @param content
	 * @return
	 */
	public ReturnModel addSysMessage(String title,String content){	
		ReturnModel rqModel=new ReturnModel();
		SysMessage message=new SysMessage();
		message.setContent(content);
		message.setCreatetime(new Date());
		message.setTitle(title);
		message.setType(Integer.parseInt(SysMessageTypeEnum.all.toString()));
		sysMessageMapper.insertSelective(message);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("添加系统通知成功！");
		return rqModel;		
	}
	
	/**
	 * 获取意见反馈列表
	 * @param branchUserId
	 * @param content
	 * @return
	 */
	public ReturnModel getUserResponseList(int index,int size,String startTimeStr,String endTimeStr){	
		ReturnModel rqModel=new ReturnModel();
		PageHelper.startPage(index, size);
		List<UUserresponses> list=userresponseMapper.findUserResponse(startTimeStr, endTimeStr);
		PageInfo<UUserresponses> reuslt=new PageInfo<UUserresponses>(list); 
		rqModel.setBasemodle(reuslt);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("获取意见反馈列表成功！");
		return rqModel;		
	}
	
	
}
