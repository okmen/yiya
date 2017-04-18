package com.bbyiya.pic.web.user;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.CookieUtils;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UUsers;
import com.bbyiya.service.IUserInfoMgtService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UUserInfoParam;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/user")
public class UserInfoController  extends SSOController{
	@Resource(name = "userInfoMgtService")
	private IUserInfoMgtService userInfoMgtService;
	@Resource(name = "userInfoMgtService")
	private IUserInfoMgtService userMgtService;
	@Autowired
	private UUsersMapper usermapper;
	
	@ResponseBody
	@RequestMapping(value = "/info/edit")
	public String getAccountInfo(String userInfoJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			UUserInfoParam param=(UUserInfoParam)JsonUtil.jsonStrToObject(userInfoJson, UUserInfoParam.class);
			if(param!=null){
				rq= userInfoMgtService.editUUsers(user.getUserId(), param);
				if(ReturnStatus.Success.equals(rq.getStatu()) ){
					LoginSuccessResult loginUser= userInfoMgtService.getLoginSuccessResult(user.getUserId());
					if(loginUser!=null){
						String ticket=super.getTicket();
						if(ObjectUtil.isEmpty(ticket)){
							ticket=CookieUtils.getCookieBySessionId(request);
						}
						RedisUtil.setObject(ticket, loginUser, 86400); 
					}
				}
			}
			rq.setStatu(ReturnStatus.Success);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 重置密码
	 * @param phone
	 * @param vcode
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/info/updatePwd")
	public String updatePwd(String phone, String vcode, String pwd) throws Exception {
		return JsonUtil.objectToJsonStr(userMgtService.updatePWD(phone, vcode, pwd));
	}

	
	
	/**
	 * 我推荐的用户列表
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/ibs/getusers")
	public String getusers(@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "10")int size,String startTime,String endTime) throws Exception {
		ReturnModel rq=new ReturnModel(); 
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			Date startDay=null,endDay=null;
			if(!ObjectUtil.isEmpty(startTime)){
				startDay=DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", startTime);
			}
			
			if(!ObjectUtil.isEmpty(endTime)){
				endDay=DateUtil.getDateByString("yyyy-MM-dd HH:mm:ss", endTime);
			}
			
			PageHelper.startPage(index, size);
			List<UUsers> list=usermapper.findUUsersByUpUserid(user.getUserId(),startDay,endDay);
			PageInfo<UUsers> resultPage=new PageInfo<UUsers>(list); 
			for (UUsers uu : resultPage.getList()) {
				uu.setPassword(""); 
			}
			rq.setStatu(ReturnStatus.Success);
			rq.setBasemodle(resultPage);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
