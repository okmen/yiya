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
	//�û�����ģ��
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
	 * ��ȡӰ¥�Ƽ��˷�չ���û������б�
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
	 * cts��ѯ�����������б�
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
	 * ����������
	 */
	public ReturnModel applyWeiUser(Long userId,UWeiuserapplys applyInfo){
		ReturnModel rq=new ReturnModel();
		UWeiuserapplys apply= weiuserApplyMapper.selectByPrimaryKey(userId); 
		if(apply!=null){
			if(apply.getStatus()!=null&&apply.getStatus().intValue()==Integer.parseInt(weiUserStatusEnum.ok.toString())){
				rq.setStatu(ReturnStatus.SystemError);
				rq.setStatusreson("���Ѿ��Ǵ������ˣ������ύ���룡");
				return rq;
			}
			applyInfo.setUserid(apply.getUserid());
		}
		rq.setStatu(ReturnStatus.SystemError);
		if(applyInfo==null){
			rq.setStatusreson("��������");
			return rq;
		}
		if(ObjectUtil.isEmpty(applyInfo.getName())){
			rq.setStatusreson("���Ʋ���Ϊ��");
			return rq;
		}
		
		if(!ObjectUtil.validSqlStr(applyInfo.getName())||!ObjectUtil.validSqlStr(applyInfo.getRemark())){
			rq.setStatusreson("���ڷǷ��ַ�");
			return rq;
		}
		applyInfo.setUserid(userId);
		applyInfo.setCreatetime(new Date());
		applyInfo.setStatus(Integer.parseInt(weiUserStatusEnum.applying.toString()));
		
		if(apply!=null&&applyInfo.getUserid()!=null&&applyInfo.getUserid()>0){
			weiuserApplyMapper.updateByPrimaryKeySelective(applyInfo);
		}else {
			weiuserApplyMapper.insert(applyInfo);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("�ύ�ɹ����ȴ���ˣ�"); 
		return rq;
	}
	/**
	 * ���������
	 * @param adminId
	 * @param weiUserId
	 * @param status 1���ͨ����2��ͨ��
	 * @return
	 */
	public ReturnModel audit_weiUserApply(Long adminId,Long weiUserId,int status){
		ReturnModel rq=new ReturnModel();
		UWeiuserapplys apply= weiuserApplyMapper.selectByPrimaryKey(weiUserId); 
		if(apply!=null){
			apply.setStatus(status); 
			weiuserApplyMapper.updateByPrimaryKeySelective(apply);
			if(status==Integer.parseInt(weiUserStatusEnum.ok.toString())){//��Ϊ����
				//������ ������Ϣ���Ƶ���ʽ�����
				this.addUweiUserInfo(apply);	
				
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("��˳ɹ�");
			}else{
				rq.setStatu(ReturnStatus.Success);
				rq.setStatusreson("��˲�ͨ��");
			}
			
		}else {
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("�Ҳ�����������");
		} 
		return rq;
	}
	
	/**
	 * ������ͨ����ˣ�¼����������Ϣ����������ݱ�ʶ
	 * @param apply
	 */
	public void addUweiUserInfo(UWeiuserapplys apply){
		if(apply!=null){
			//������¼��
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
			//���´�����ݱ�ʶ
			userBasic.addUserIdentity(apply.getUserid(),UserIdentityEnums.wei); 
		}
	}
	
	/**
	 * ctsɾ����������¼
	 */
	public ReturnModel delete_weiUserApply(Long adminId,Long weiUserId){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		UWeiuserapplys apply=weiuserApplyMapper.selectByPrimaryKey(weiUserId);
		UWeiusers weiuser=weiuserMapper.selectByPrimaryKey(weiUserId);
		if(apply!=null){
			apply.setStatus(Integer.parseInt(weiUserStatusEnum.del.toString()));
			weiuserApplyMapper.updateByPrimaryKey(apply);
		}	
		if(weiuser!=null){
			weiuser.setStatus(Integer.parseInt(weiUserStatusEnum.del.toString()));
			weiuserMapper.updateByPrimaryKey(weiuser);
		}
		//���´�����ݱ�ʶ
		userBasic.removeUserIdentity(weiUserId,UserIdentityEnums.wei); 	
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("ɾ���ɹ�");
		return rq;
	}
	
	/**
	 * cts����������״̬
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
					map.put("msg", "�Ѿ���Ϊ������");
				}else if (apply.getStatus().intValue()==Integer.parseInt(weiUserStatusEnum.applying.toString())) {
					map.put("msg", "������");
				}else if (apply.getStatus().intValue()==Integer.parseInt(weiUserStatusEnum.no.toString())) {
					map.put("msg", "���벻ͨ����");
				}
			}else {
				map.put("msg", "������");
			}
		}else {
			map.put("isApplyed", 0);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(map);
		return rq;
	}
	
	/**
	 * �õ��Ƽ��û��б�
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
			//��ȡ���ڵ�������ʱ��
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
