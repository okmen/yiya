package com.bbyiya.pic.web.version_one;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Branch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.YiyeAddressType;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.UBranches;
import com.bbyiya.service.IRegionService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.vo.user.UUserAddressResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/act")
public class YiyeActionController extends SSOController{
	@Autowired
	private PMyproducttempapplyMapper applyMapper;
	@Autowired
	private PMyproducttempMapper tempMapper;
	@Autowired
	private UBranchesMapper branchMapper;
	
	@Resource(name = "regionServiceImpl")
	private IRegionService regionMapper;
	
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
	
	/**
	 * 作品是否门店自提
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/actAddressList")
	public String actAddressList(Long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PMyproducttempapply apply= applyMapper.getMyProducttempApplyByCartId(cartId);
			if(apply!=null){
				PMyproducttemp temp=tempMapper.selectByPrimaryKey(apply.getTempid());
				if(temp!=null&&temp.getIsbranchaddress()!=null&&temp.getIsbranchaddress()==Integer.parseInt(YiyeAddressType.branchList.toString())){
					UBranches branch= branchMapper.selectByPrimaryKey(temp.getBranchuserid());
					if(branch!=null){
						List<UBranches> list= branchMapper.findBranchesByAgentUserId(branch.getAgentuserid());
						if(list!=null&&list.size()>0){
							List<UUserAddressResult> addrlist=new ArrayList<UUserAddressResult>();
							for (UBranches uu : list) {
								if(uu.getStatus()!=null&&uu.getStatus().intValue()==1){
									UUserAddressResult add=new UUserAddressResult();
									add.setProvince(uu.getProvince());
									add.setProvinceName(regionMapper.getProvinceName(uu.getProvince()));
									add.setCity(uu.getCity());
									add.setCityName(regionMapper.getCityName(uu.getCity()));
									add.setArea(uu.getArea());
									add.setAreaName(regionMapper.getAresName(uu.getArea()));
									add.setStreetdetail(uu.getStreetdetail()); 
									add.setUserid(uu.getBranchuserid());
									if(temp.getBranchuserid().longValue()==uu.getBranchuserid().longValue()){
										add.setIsdefault(1); 
									}
									addrlist.add(add);
								}
							}
							rq.setBasemodle(addrlist);
						}
					}
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
