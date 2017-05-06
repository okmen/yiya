package com.bbyiya.pic.web.cts;



import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.cts.IMessageAndResponseMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/msg")
public class SysMessageController extends SSOController {
	@Resource(name = "messageAndResponseMgtServiceImpl")
	private IMessageAndResponseMgtService messageAndResponseService;

	
	/**
	 * 发送系统通知
	 * @param agentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sendSysMessage")
	public String sendSysMessage(String title,String content) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=messageAndResponseService.addSysMessage(title, content);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	} 
	
	/**
	 * 获取意见反馈列表
	 * @param branchUserId
	 * @param content
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getUserResponseList")
	public String getUserResponseList(int index,int size,String startTimeStr,String endTimeStr)throws Exception{	
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=messageAndResponseService.getUserResponseList(index, size, startTimeStr, endTimeStr);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}		
		return JsonUtil.objectToJsonStr(rq);	
	}
	
}
