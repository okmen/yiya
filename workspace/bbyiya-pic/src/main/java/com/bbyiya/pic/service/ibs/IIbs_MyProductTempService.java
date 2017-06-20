package com.bbyiya.pic.service.ibs;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.vo.ReturnModel;

public interface IIbs_MyProductTempService {
	/**
	 * 添加模板
	 * @param userid
	 * @param title
	 * @param remark
	 * @return
	 */
	ReturnModel addMyProductTemp(Long userid,MyProductTempAddParam param);
	/**
	 * 启用或禁用模板
	 * @param type
	 * @param tempid
	 * @return
	 */
	ReturnModel editMyProductTempStatus(int type, int tempid);
	/**
	 * 删除模板
	 * @param tempid
	 * @return
	 */
	ReturnModel deleteMyProductTemp(int tempid);
	/**
	 * 查询模板列表
	 * @param index
	 * @param size
	 * @param userid
	 * @return
	 */
	ReturnModel findMyProductTempList(int index, int size, Long userid,Integer status,String keywords,Integer type);
	/**
	 * 保存模板二维码图片
	 * @param url
	 * @return
	 * @throws Exception
	 */
	ReturnModel saveProductTempRQcode(String url) throws Exception;
	/**
	 * 修改模板
	 * @param userid
	 * @param title
	 * @param remark
	 * @param tempid
	 * @return
	 */
	ReturnModel editMyProductTemp(MyProductTempAddParam param);
	/**
	 * 获取影楼模板待审核用户列表
	 * @param index
	 * @param size
	 * @param userid
	 * @param tempid
	 * @return
	 */
	ReturnModel getMyProductTempApplyCheckList(int index, int size,
			Long userid, int tempid);
	/**
	 * 审核影楼模板申请用户
	 * @param userid
	 * @param tempapplyid
	 * @param status  0 申请中，1通过，2拒绝
	 * @return
	 */
	ReturnModel audit_TempApplyUser(Long userid, Long tempapplyid,
			Integer status);
	/**
	 * 影楼员工负责模板信息列表
	 * @param index
	 * @param size
	 * @param branchUserId
	 * @param tempid
	 * @return
	 */
	ReturnModel find_BranchUserOfTemp(int index, int size, Long branchUserId,
			Integer tempid);
	/**
	 * 设置员工模板负责权限
	 * @param userId
	 * @param tempid
	 * @param status
	 * @return
	 */
	ReturnModel setUserTempPermission(Long userId, Integer tempid,
			Integer status);
	/**
	 * 接受邀请或ibs后台审核用户申请操作 (公用方法)
	 * @param apply
	 * @return
	 */
	ReturnModel doAcceptOrAutoTempApplyOpt(PMyproducttempapply apply);
	/**
	 * 审核模板申请用户作品是否通过
	 * @param userId
	 * @param cartid
	 * @param status
	 * @return
	 */
	ReturnModel audit_TempApplyProduct(Long userId, Long cartid, Integer status,String reason);
	/**
	 * 修改模板二维码信息
	 * @param tempid
	 * @param codeurl
	 * @param codesm
	 * @return
	 */
	ReturnModel editTempCodeUrl(Integer tempid, String codeurl, String codesm,String discription);
	/**
	 * 设置员工模板审核负责权限
	 * @param userId
	 * @param tempid
	 * @param status
	 * @return
	 */
	ReturnModel setUserTempVerfiyPermission(Long userId, Integer tempid,
			Integer status);
	/**
	 * 设置活动最大报名人数
	 * @param userId
	 * @param tempid
	 * @param maxApplyCount
	 * @return
	 */
	ReturnModel setTempMaxApplyCount(Long userId, Integer tempid,
			Integer maxApplyCount);
	/**
	 * 设置活动完成目标
	 * @param userId
	 * @param tempid
	 * @param blessCount
	 * @param maxCompleteCount
	 * @return
	 */
	ReturnModel setTempCompletecondition(Long userId, Integer tempid,
			Integer blessCount, Integer maxCompleteCount);

	
}
