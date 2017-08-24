package com.bbyiya.pic.service.impl.ibs;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.UBranchesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UBranches;
import com.bbyiya.pic.service.ibs.IIbs_BranchMgtService;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;

@Service("ibs_BranchMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Ibs_BranchMgtServiceImpl implements IIbs_BranchMgtService{
	
	@Autowired
	private UBranchesMapper branchesMapper;
	
	/**
	 * 修改代理商店铺页信息
	 * @param branchUserId
	 * @return
	 */
	public ReturnModel editBranchShopInfo(Long branchUserId,String logo,String promotionstr){	
		ReturnModel rqModel=new ReturnModel();
		UBranches branch=branchesMapper.selectByPrimaryKey(branchUserId);
		if(branch!=null){
			branch.setLogo(logo);
			branch.setPromotionstr(promotionstr);
			branchesMapper.updateByPrimaryKeySelective(branch);
			
			rqModel.setStatu(ReturnStatus.Success);
			rqModel.setStatusreson("修改店铺页信息成功！");
		}else{
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("不是影楼身份，不能上传logo！");
		}
		
		return rqModel;		
	}
	
}
