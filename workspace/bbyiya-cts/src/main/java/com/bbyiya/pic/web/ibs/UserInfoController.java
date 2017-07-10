package com.bbyiya.pic.web.ibs;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UUsers;
import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/ibs/user")
public class UserInfoController  extends SSOController{
	@Autowired
	private UUsersMapper usermapper;
	
	
	/**
	 * 我推荐的用户列表
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getSourceUsers")
	public String getSourceUsers(@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "10")int size,String startTime,String endTime) throws Exception {
		ReturnModel rq=new ReturnModel(); 
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
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
			List<UUsers> list=usermapper.findUUsersBySourceUserId(user.getUserId(),startDay,endDay);
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
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
