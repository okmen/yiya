package com.bbyiya.pic.web.calendar;



import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.calendar.IIbs_TiAdvertimgsService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/ti_advert")
public class TiAdvertimgsController extends SSOController {
	
	@Resource(name = "ibs_TiAdvertimgsService")
	private IIbs_TiAdvertimgsService advertimgsService;
	@Autowired
	private UUsersMapper userMapper;
	
	/**
	 * 添加影楼广告位
	 * @param agentUserId
	 * @param status
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addorEditAdvertimgs")
	public String addorEditAdvertimgs(Long productid,String advertImgJson,String advertContent) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=advertimgsService.addOrEditAdvertimgs(user.getUserId(),productid,advertImgJson,advertContent);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 得到产品的广告位信息
	 * @param memberJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getAdvertimgsByIds")
	public String getAdvertimgsByIds(Long productid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=advertimgsService.getAdvertimgsByIds(user.getUserId(), productid);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
}
