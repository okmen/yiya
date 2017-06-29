package com.bbyiya.pic.web.ibs;


import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.service.IBaseUserAccountService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
import com.sdicons.json.mapper.MapperException;

@Controller
@RequestMapping(value = "/ibs/account")
public class BranchAccountController  extends SSOController{

	
	@Resource(name = "baseUserAccountService")
	private IBaseUserAccountService accountService;
	
	
	/**
	 * �˻���ϸ��¼
	 * @param branchuserid
	 * @param amount
	 * @return
	 * @throws MapperException 
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/accountLog")
	public String findAcountsLogsPageResult(Long userId, Integer type, int index, int size) throws MapperException {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ValidateUtils.isIdentity(user.getIdentity(), UserIdentityEnums.branch)){
				rq= accountService.findAcountsLogsPageResult(userId, type, index, size);
			}else {
				rq.setStatu(ReturnStatus.SystemError_1);
				rq.setStatusreson("Ȩ�޲���");
			} 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("��¼����");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
