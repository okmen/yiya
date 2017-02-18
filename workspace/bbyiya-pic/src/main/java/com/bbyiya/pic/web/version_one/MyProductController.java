package com.bbyiya.pic.web.version_one;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/myProduct")
public class MyProductController extends SSOController {
	@Resource(name = "pic_productService")
	private IPic_ProductService proService;
	@Autowired
	private PMyproductdetailsMapper detaiMapper;

	/**
	 * P08 保存我的作品(新增/修改) 每次修改都是先清除原有的，再新增
	 * 
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/saveMyproduct")
	public String saveMyproduct(String myproductJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			MyProductParam param = Json2Objects.getParam_MyProductParam(myproductJson);
			if (param != null) {
				if (param.getDetails() != null && param.getDetails().size() > 12) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("不能超过12条记录");
					return JsonUtil.objectToJsonStr(rq);
				}
				rq = proService.saveOrEdit_MyProducts(user.getUserId(), param);
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * P08-1 修改我的作品） 只做修改操作（根据主键Id修改）
	 * 
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editMyproduct")
	public String editMyproduct(String myproductJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			MyProductParam param = Json2Objects.getParam_MyProductParam(myproductJson);
			if (param != null) {
				if (param.getDetails() != null && param.getDetails().size() > 12) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("不能超过12条记录");
					return JsonUtil.objectToJsonStr(rq);
				}
				rq = proService.Edit_MyProducts(user.getUserId(), param);
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 我的作品列表
	 * 
	 * @param myproductJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/mylist")
	public String myProlist() throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = proService.findMyProlist(user.getUserId());
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * P10 作品详情（用户管理、编辑页）
	 * 
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/details")
	public String details(@RequestParam(required = false, defaultValue = "0") long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = proService.getMyProductInfo(user.getUserId(), cartId);
		} else {// 非登录分享页
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("未登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * P14 作品详情（通过产品Id获取）
	 * 
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/getDetailsByProductId")
	public String getDetailsByProductId(@RequestParam(required = false, defaultValue = "0") long productId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = proService.getMyProductByProductId(user.getUserId(), productId);
		} else {// 非登录分享页
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("未登录");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 我的作品，分享页
	 * 
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sharedetails")
	public String sharedetails(@RequestParam(required = false, defaultValue = "0") long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		String key = "shareurl02142-cartid-" + cartId;
		rq = (ReturnModel) RedisUtil.getObject(key);
		if (rq == null || !rq.getStatu().equals(ReturnStatus.Success)) {
			rq = proService.getMyProductInfo(cartId);
			if (ReturnStatus.Success.equals(rq.getStatu())) {
				RedisUtil.setObject(key, rq, 3600);
			}
		}
		//浏览量
		myproductCount(cartId);
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * P11 我的作品-删除单个场景照片
	 * 
	 * @param pdid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/dele")
	public String dele(@RequestParam(required = false, defaultValue = "0") long pdid) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = proService.del_myProductDetail(user.getUserId(), pdid);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * P09-01 删除我的作品
	 * 
	 * @param pdid
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/deleMyWorks")
	public String deleMyWorks(@RequestParam(required = false, defaultValue = "0") long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq = proService.deleMyProduct(user.getUserId(), cartId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 记录作品的浏览量
	 * @param cartId
	 */
	public void myproductCount(Long cartId) {
		String keyName = "yiya_myproduct21070217";
		Map<Long, Integer> map = (Map<Long, Integer>) RedisUtil.getObject(keyName);
		if (map == null) {
			map = new HashMap<Long, Integer>();
		}
		Integer count=0;
		if (map.containsKey(cartId)) {
			count= map.get(cartId);
			if (count != null && count > 0){
				count += 1;
			}
			else {
				count = 1;
			}
		}else {
			count=1;
		}
		map.put(cartId, count);
		RedisUtil.setObject(keyName, map); 
	}
}
