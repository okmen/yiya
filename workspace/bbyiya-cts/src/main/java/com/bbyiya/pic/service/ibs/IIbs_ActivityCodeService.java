package com.bbyiya.pic.service.ibs;
import java.util.List;

import com.bbyiya.model.PMyproducttempext;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.vo.ReturnModel;

public interface IIbs_ActivityCodeService {
	/**
	 * 添加活动码
	 * @param userid
	 * @param title
	 * @param remark
	 * @return
	 */
	ReturnModel addActivityCode(Long userid,MyProductTempAddParam param,List<PMyproducttempext> arealist);
	/**
	 * 得到tempId下的活动码列表
	 * @param branchUserId
	 * @param tempid
	 * @param activeStatus
	 * @param keywords
	 * @param index
	 * @param size
	 * @return
	 */
	ReturnModel findMyProductslistForActivityCode(Long branchUserId,
			Integer tempid, Integer activeStatus, String keywords, int index,
			int size);
	/**
	 * 删除活动码
	 * @param codeno
	 * @return
	 */
	ReturnModel deleteActivityCode(String codeno);
	/**
	 * 重置活动下的已报名的序号
	 * @return
	 */
	ReturnModel resetAllTempApplySort(Integer tempid);
	/**
	 * 活动码详情
	 * @param tempid
	 * @return
	 */
	ReturnModel getActivityCodeDetail(Integer tempid);
	/**
	 * 修改活动备注
	 * @param tempid
	 * @param remark
	 * @return
	 */
	ReturnModel editTempRemark(Integer tempid, String remark);
	

	
}
