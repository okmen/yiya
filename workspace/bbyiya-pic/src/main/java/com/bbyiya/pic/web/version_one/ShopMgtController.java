package com.bbyiya.pic.web.version_one;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.baseUtils.ValidateUtils;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.user.UserIdentityEnums;
import com.bbyiya.model.UBranches;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.vo.agent.BranchShopInfo;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/shop")
public class ShopMgtController extends SSOController{
	@Autowired
	private UBranchesMapper branchMapper;
	@Autowired
	private UUsersMapper userMapper;
	/**
	 * 代理商店铺页 （是否需要展示的信息）
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/info")
	public String shopInfo(long uid) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult users=super.getLoginUser();
		if(users!=null){
			UUsers userUp= userMapper.selectByPrimaryKey(uid);
			if(userUp!=null){
				UBranches branch=null;
				//如果uid 是影楼用户
				if(ValidateUtils.isIdentity(userUp.getIdentity(), UserIdentityEnums.branch)){
					branch= branchMapper.selectByPrimaryKey(uid);
				}
				//如果uid 不是影楼 
				else if (!ObjectUtil.isEmpty(userUp.getSourseuserid())) {
					branch=branchMapper.selectByPrimaryKey(userUp.getSourseuserid());
				}
				
				if(branch!=null){
					BranchShopInfo shopInfo=new BranchShopInfo();
					if(!ObjectUtil.isEmpty(branch.getLogo())){
						shopInfo.setLogo(branch.getLogo());
					}
					if(!ObjectUtil.isEmpty(branch.getPromotionstr())){
						shopInfo.setPromotionStr(branch.getPromotionstr());
					}
					if(!ObjectUtil.isEmpty(users.getMobilePhone())){
						shopInfo.setMobileBind(1); 
					}
					rq.setBasemodle(shopInfo); 
				}
			}
		}
		
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}

	
}
