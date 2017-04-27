package com.bbyiya.pic.web.cts;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PPostmodel;
import com.bbyiya.service.pic.IBasePostMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;
@Controller
@RequestMapping(value = "/cts/post")
public class PPostmodelController extends SSOController {
	
	@Resource(name = "basePostMgtServiceImpl")
	private IBasePostMgtService postService;	
	@Autowired
	private EErrorsMapper logger;
	/**
	 * C-P01 邮费模版列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findPPostModelList")
	public String findPPostModelList() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			List<PPostmodel> postlist=postService.find_postlist(0);
			rq.setBasemodle(postlist);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("获取列表成功");
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * C-P02 新增 邮费模版
	 * @param name 快递方式名称
	 * @param Amount 默认价格
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addPPostModel")
	public String addPPostModel(String name,Double amount) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=postService.addPostmodel(user.getUserId(), name, amount);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * C-P03 修改邮费模版
	 * @param name 快递方式名称
	 * @param Amount 默认价格
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePPostModel")
	public String updatePPostModel(int postModelId, String name,Double amount) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=postService.editPostmodel(postModelId, name, amount);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * C-P04 邮费模板特殊区域列表
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findPostmodelareasList")
	public String findPostmodelareasList(@RequestParam(required = false)Integer areacode,
			@RequestParam(required = false,defaultValue="")String areaname,
			@RequestParam(required = false, defaultValue = "1") int index,@RequestParam(required = false, defaultValue = "10") int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=postService.find_PostModelAreaslist(index, size, areacode, areaname);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * C-P05 新增邮费模板特殊区域
	 * @param postModelJson 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addPostmodelareas")
	public String addPostmodelareas(String postModelJson ) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=postService.addPostModelAreas(postModelJson);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * C-P06 修改邮费模板特殊区域
	 * @param postModelJson 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/editPostmodelareas")
	public String editPostmodelareas(String postModelJson ) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=postService.editPostModelAreas(postModelJson);			
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * C-P07 删除邮费模板特殊区域
	 * @param postModelJson 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/delPostmodelareas")
	public String delPostmodelareas(Integer postId ) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=postService.delPostModelAreas(postId);	
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	
	
	
	
}
