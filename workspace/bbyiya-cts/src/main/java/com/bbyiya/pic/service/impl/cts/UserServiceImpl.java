package com.bbyiya.pic.service.impl.cts;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.PScenesMapper;
import com.bbyiya.dao.UAdminMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.PScenes;
import com.bbyiya.model.UAdmin;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.cts.IScenseService;
import com.bbyiya.pic.service.cts.IUserService;
import com.bbyiya.service.IBaseUserCommonService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("ctsuserService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class UserServiceImpl implements IUserService{
	@Resource(name = "baseUserCommon")
	private IBaseUserCommonService userBasic;
	
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private UAdminMapper adminMapper;
	/**
	 * 添加cts内部账号
	 * */
	public ReturnModel addCtsUser(Long userid,String phone){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		//检测被添加的用户身份（必须绑定手机）
		UUsers member= usersMapper.getUUsersByPhone(phone);
		if(member==null){
			member=usersMapper.getUUsersByUserID(ObjectUtil.parseLong(phone));
		}
		if(member!=null){
			UAdmin admin=adminMapper.selectByPrimaryKey(member.getUserid());
			if(admin!=null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("该用户已是cts内部账号，不能重复添加！");
				return rq;
			}
			UAdmin adminnew=new UAdmin();
			adminnew.setCreatetime(new Date());
			adminnew.setType(2);//普通账号
			adminnew.setUserid(member.getUserid());
			adminnew.setUsername(member.getNickname());
			adminMapper.insert(adminnew);
			
			//被添加的用户的身份标示
			userBasic.addUserIdentity(member.getUserid(),UserIdentityEnums.cts_member); 
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("添加成功！");
		}else{
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("用户不存在（或者该手机未绑定）！");
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	
	public ReturnModel deleteCtsUser(Long userid){
		ReturnModel rq=new ReturnModel();
		
		UAdmin admin=adminMapper.selectByPrimaryKey(userid);
		if(admin!=null&&admin.getType().intValue()!=1){
			adminMapper.deleteByPrimaryKey(userid);
			userBasic.removeUserIdentity(userid,UserIdentityEnums.cts_member);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("删除成功！"); 
		}else{
			rq.setStatu(ReturnStatus.SystemError);
			rq.setStatusreson("管理员账号不能删除！");
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	

	public ReturnModel findCtsMemberlist(String keywords,int index,int size){
		ReturnModel rqModel=new ReturnModel();
		PageHelper.startPage(index, size);
		List<UAdmin> memberlist=adminMapper.findAdminUserList(keywords);
		PageInfo<UAdmin> resultPage = new PageInfo<UAdmin>(memberlist);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setBasemodle(resultPage);
		return rqModel;
		
	}
	
	

	
}
