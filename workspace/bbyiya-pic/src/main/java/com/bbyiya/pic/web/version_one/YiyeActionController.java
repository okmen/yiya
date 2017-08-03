package com.bbyiya.pic.web.version_one;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/act")
public class YiyeActionController extends SSOController{
	@Autowired
	private PMyproducttempapplyMapper applyMapper;
	
	/**
	 * 完成活动 分享目标
	 * @param phone
	 * @param cartId
	 * @param type 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/complete")
	public String sendInvite(Long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PMyproducttempapply apply= applyMapper.getMyProducttempApplyByCartId(cartId);
			if(apply!=null){
				if(apply.getShared()==null||apply.getShared().intValue()<=0){
					apply.setShared(1);
					applyMapper.updateByPrimaryKeySelective(apply);
				}
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期，请重新登录"); 
			return JsonUtil.objectToJsonStr(rq);
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
}
