package com.bbyiya.pic.service.impl.cts;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.OPayorderextMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.dao.UWeiuserapplysMapper;
import com.bbyiya.dao.UWeiusersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.AgentStatusEnum;
import com.bbyiya.enums.pic.weiUserStatusEnum;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.OPayorderext;
import com.bbyiya.model.UAgentapply;
import com.bbyiya.model.UUsers;
import com.bbyiya.model.UWeiuserapplys;
import com.bbyiya.model.UWeiusers;
import com.bbyiya.pic.service.cts.ICts_UWeiUserManageService;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.UWeiUserSearchParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("cts_UWeiuserService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Cts_UWeiUserServiceImpl implements ICts_UWeiUserManageService{
	//用户公共模块
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	@Autowired
	private UUsersMapper usermapper;
	@Autowired
	private OPayorderextMapper payextendMapper;
	@Autowired
	private UWeiusersMapper weiuserMapper;	
	@Autowired
	private UWeiuserapplysMapper weiuserApplyMapper;
	
	/**
	 * 获取影楼推荐人发展的用户订单列表
	 */
	public ReturnModel find_payorderExtByBranchUpUserid(Long branchuserId,String startTimeStr,String endTimeStr,Integer status, int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<OPayorderext> list= payextendMapper.find_payorderExtByBranchUpUserid(branchuserId, status, startTimeStr, endTimeStr);
		PageInfo<OPayorderext> resultPage=new PageInfo<OPayorderext>(list); 
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	public ReturnModel findWeiUserVoList(UWeiUserSearchParam param,int index, int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		List<UWeiusers> list=weiuserMapper.findUWeiusersList(param);
		PageInfo<UWeiusers> result=new PageInfo<UWeiusers>(list); 
		rq.setBasemodle(result);
		return rq;
	}
	/**
	 * cts查询流量主申请列表
	 */
	public ReturnModel findWeiUserApplylist(UWeiUserSearchParam param,int index, int size){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		PageHelper.startPage(index, size);
		List<UWeiuserapplys> list=weiuserApplyMapper.findWeiUserApplylist(param);
		PageInfo<UWeiuserapplys> result=new PageInfo<UWeiuserapplys>(list); 
		rq.setBasemodle(result);
		return rq;
	}
	
	/**
	 * 流量主申请
	 */
	public ReturnModel applyWeiUser(UWeiuserapplys applyInfo){
		ReturnModel rq=new ReturnModel();
		if(applyInfo==null){
			rq.setStatusreson("参数有误");
			return rq;
		}
		
		UWeiuserapplys apply= weiuserApplyMapper.selectByPrimaryKey(applyInfo.getUserid()); 
		if(apply!=null){
			if(apply.getStatus()!=null&&apply.getStatus().intValue()==Integer.parseInt(weiUserStatusEnum.ok.toString())){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("您已经是代理商了，不能提交申请！");
				return rq;
			}
			applyInfo.setUserid(apply.getUserid());
		}
		rq.setStatu(ReturnStatus.SystemError);
		
		if(ObjectUtil.isEmpty(applyInfo.getName())){
			rq.setStatusreson("名称不能为空");
			return rq;
		}
		
		if(!ObjectUtil.validSqlStr(applyInfo.getName())||(!ObjectUtil.isEmpty(applyInfo.getRemark()))&&!ObjectUtil.validSqlStr(applyInfo.getRemark())){
			rq.setStatusreson("存在非法字符");
			return rq;
		}
		applyInfo.setUserid(applyInfo.getUserid());
		applyInfo.setCreatetime(new Date());
		applyInfo.setStatus(Integer.parseInt(weiUserStatusEnum.applying.toString()));
		
		if(apply!=null&&applyInfo.getUserid()!=null&&applyInfo.getUserid()>0){
			weiuserApplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			weiuserApplyMapper.insert(applyInfo);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("提交成功，等待审核！"); 
		return rq;
	}
	/**
	 * 流量主审核
	 * @param adminId
	 * @param weiUserId
	 * @param status 1审核通过，2不通过
	 * @return
	 */
	public ReturnModel audit_weiUserApply(Long adminId,Long weiUserId,int status){
		ReturnModel rq=new ReturnModel();
		UWeiuserapplys apply= weiuserApplyMapper.selectByPrimaryKey(weiUserId); 
		if(apply!=null){
			apply.setStatus(status); 
			weiuserApplyMapper.updateByPrimaryKeySelective(apply);
			if(status==Integer.parseInt(weiUserStatusEnum.ok.toString())){//成为代理
				//代理商 申请信息复制到正式代理表
				this.addUweiUserInfo(apply);	
				
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("审核成功");
			}else{
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("审核不通过");
			}
			
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("找不到申请资料");
		} 
		return rq;
	}
	
	/**
	 * 流量主通过审核，录入流量主信息、流量主身份标识
	 * @param apply
	 */
	public void addUweiUserInfo(UWeiuserapplys apply){
		if(apply!=null){
			//流量主录入
			UWeiusers weiuserModel=weiuserMapper.selectByPrimaryKey(apply.getUserid());
			boolean isEdit=true;
			if(weiuserModel==null){
				weiuserModel=new UWeiusers();
				isEdit=false;
			}
			weiuserModel.setUserid(apply.getUserid());
			weiuserModel.setCreatetime(new Date());
			weiuserModel.setMobilephone(apply.getMobilephone());
			weiuserModel.setName(apply.getName());
			weiuserModel.setRemark(apply.getRemark());
			weiuserModel.setStatus(Integer.parseInt(weiUserStatusEnum.ok.toString()));
			if(isEdit){
				weiuserMapper.updateByPrimaryKeySelective(weiuserModel);
			}else{
				weiuserMapper.insertSelective(weiuserModel);
			}		
			//更新代理身份标识
			userBasic.addUserIdentity(apply.getUserid(),UserIdentityEnums.wei); 
		}
	}
	
	/**
	 * cts删除流量主记录
	 */
	public ReturnModel delete_weiUserApply(Long adminId,Long weiUserId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		UWeiuserapplys apply=weiuserApplyMapper.selectByPrimaryKey(weiUserId);
		UWeiusers weiuser=weiuserMapper.selectByPrimaryKey(weiUserId);
		if(weiuser.getStatus()==Integer.parseInt(weiUserStatusEnum.ok.toString())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("已审核通过的流量主不能删除！");
		}
		if(apply!=null){
			apply.setStatus(Integer.parseInt(weiUserStatusEnum.del.toString()));
			weiuserApplyMapper.updateByPrimaryKey(apply);
		}	
		if(weiuser!=null){
			weiuser.setStatus(Integer.parseInt(weiUserStatusEnum.del.toString()));
			weiuserMapper.updateByPrimaryKey(weiuser);
		}
		//更新代理身份标识
		userBasic.removeUserIdentity(weiUserId,UserIdentityEnums.wei); 	
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("删除成功");
		return rq;
	}
	
	/**
	 * cts流量主申请状态
	 */
	public ReturnModel getWeiUserApplyStatus(Long weiUserId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		Map<String, Object> map=new HashMap<String, Object>();
		UWeiuserapplys apply=weiuserApplyMapper.selectByPrimaryKey(weiUserId);
		if(apply!=null){
			map.put("isApplyed", 1);
			map.put("status", apply.getStatus());
			map.put("applyInfo", apply);
			if(apply.getStatus()!=null){
				if(apply.getStatus().intValue()==Integer.parseInt(weiUserStatusEnum.ok.toString())){
					map.put("msg", "已经成为流量主");
				}else if (apply.getStatus().intValue()==Integer.parseInt(weiUserStatusEnum.applying.toString())) {
					map.put("msg", "申请中");
				}else if (apply.getStatus().intValue()==Integer.parseInt(weiUserStatusEnum.no.toString())) {
					map.put("msg", "申请不通过。");
				}
			}else {
				map.put("msg", "申请中");
			}
		}else {
			map.put("isApplyed", 0);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(map);
		return rq;
	}
	
	/**
	 * 得到推荐用户列表
	 * @param userId
	 * @param startTime
	 * @param endTime
	 * @param index
	 * @param size
	 * @return
	 */
	public ReturnModel getRecommendUser(Long userId,String startTime,String endTime,int index,int size){
		ReturnModel rq=new ReturnModel();
		Date startDay=null,endDay=null;
		if(!ObjectUtil.isEmpty(startTime)){
			startDay=DateUtil.getDateByString("yyyy-MM-dd", startTime);
		}
		if(!ObjectUtil.isEmpty(endTime)){
			//获取日期的最后结束时间
			endTime=DateUtil.getEndTime(endTime);
			endDay=DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", endTime);
		}
		PageHelper.startPage(index, size);
		List<UUsers> list=usermapper.findUUsersByUpUserid(userId,startDay,endDay);
		PageInfo<UUsers> resultPage=new PageInfo<UUsers>(list); 
		for (UUsers uu : resultPage.getList()) {
			uu.setPassword(""); 
			uu.setCreatetimestr(DateUtil.getTimeStr(uu.getCreatetime(), "yyyy-MM-dd HH:mm:ss")); 
			if(ObjectUtil.isEmpty(uu.getNickname())){
				uu.setNickname("yiya"+uu.getUserid());
			}
			if(ObjectUtil.isEmpty(uu.getUserimg())){
				uu.setUserimg(ConfigUtil.getSingleValue("default-headimg")); 
			}
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(resultPage);
		return rq;
	}
	
	
}
