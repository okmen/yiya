package com.bbyiya.pic.web.version_one;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
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
	 * 保存、更新我的作品
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
			MyProductParam param = getParams(myproductJson);// (MyProductParam) JsonUtil.jsonStrToObject(myproductJson, MyProductParam.class);
			if (param != null) {
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
	 * 保存参数对象转化
	 * @param result
	 * @return
	 */
	private MyProductParam getParams(String result) {
		JSONObject model = JSONObject.fromObject(result);
		if (model != null) {
			MyProductParam param = new MyProductParam();
			param.setProductid(ObjectUtil.parseLong(String.valueOf(model.get("productid"))));
			param.setTitle(String.valueOf(model.get("title")));
			param.setAuthor(String.valueOf(model.get("author")));
			param.setCartid(ObjectUtil.parseLong(String.valueOf(model.get("cartid"))));
			JSONArray details = new JSONArray().fromObject(String.valueOf(model.get("details")));
			if(details!=null&&details.size()>0){
				List<PMyproductdetails> detailsList=new ArrayList<PMyproductdetails>();
				for (int i = 0; i < details.size(); i++) {
					JSONObject dd = details.getJSONObject(i);//
					PMyproductdetails mo=new PMyproductdetails();
					String content=String.valueOf(dd.get("content"));
					if(!ObjectUtil.isEmpty(content)&&!content.equals("null")){
						mo.setContent(content);
					}
					int scenid=ObjectUtil.parseInt(String.valueOf(dd.get("sceneid")));
					if(scenid>0){
						mo.setSceneid(scenid); 
					}
					if(dd.get("imgurl")!=null){
						String url=String.valueOf(dd.get("imgurl"));
						if(!ObjectUtil.isEmpty(url)&&!url.equals("null")){
							mo.setImgurl(url); 
						}	
					}
					int sort=ObjectUtil.parseInt(String.valueOf(dd.get("sort")));
					if(sort>0){
						mo.setSort(sort); 
					}
					long pdid=ObjectUtil.parseLong(String.valueOf(dd.get("pdid")));
					if(pdid>0){
						mo.setPdid(pdid); 
					}
					detailsList.add(mo);
				}
				param.setDetails(detailsList); 
			}
			return param;
		}
		return null;
		
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
	 * 我的作品详情
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
		} else {//非登录分享页
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("未登录"); 
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	/**
	 * 我的作品，分享页
	 * @param cartId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/sharedetails")
	public String sharedetails(@RequestParam(required = false, defaultValue = "0") long cartId) throws Exception {
		ReturnModel rq = new ReturnModel();
		String key = "shareurl0210-cartid-" + cartId;
		rq = (ReturnModel) RedisUtil.getObject(key);
		if (rq == null || !rq.getStatu().equals(ReturnStatus.Success)) {
			rq = proService.getMyProductInfo(cartId);
			if (ReturnStatus.Success.equals(rq.getStatu())) {
				RedisUtil.setObject(key, rq, 60000);
			}
		}

		return JsonUtil.objectToJsonStr(rq);
	}

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

}
