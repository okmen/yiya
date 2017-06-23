package com.bbyiya.pic.service.cts;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.baseUtils.GenUtils;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.PMyproductactivitycodeMapper;
import com.bbyiya.dao.PMyproductchildinfoMapper;
import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductextMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PMyproductsinvitesMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.UBranchusersMapper;
import com.bbyiya.dao.UChildreninfoMapper;
import com.bbyiya.dao.UUseraddressMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.MyProductTempStatusEnum;
import com.bbyiya.enums.MyProductTempType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.ActivityCodeStatusEnum;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.PMyproductactivitycode;
import com.bbyiya.model.PMyproductchildinfo;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproductext;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.model.UBranchusers;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.ibs.IIbs_ActivityCodeService;
import com.bbyiya.pic.vo.product.ActivityCodeProductVO;
import com.bbyiya.pic.vo.product.MyProductTempAddParam;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.PageInfoUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.MyProductResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public interface IScenseService {
	/**
	 * 添加或修改场影
	 * @param userid
	 * @param myScenseJson
	 * @return
	 */
	ReturnModel addorUpdateScense(Long userid, String myScenseJson);
	/**
	 * 获取场景列表
	 * @param index
	 * @param size
	 * @param keywords
	 * @param productId
	 * @return
	 */
	ReturnModel getScenseList(int index, int size, String keywords,
			String productId);
	
}