package com.bbyiya.pic.web.calendar;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.TiProductshowtemplateMapper;
import com.bbyiya.dao.TiProductshowtemplateinfoMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.TiProductshowtemplate;
import com.bbyiya.model.TiProductshowtemplateinfo;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiProductstyleslayers;
import com.bbyiya.pic.service.calendar.ICts_TiProductsService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.product.TiProductResult;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/cts/ti_product")
public class TiProductsController extends SSOController {
	
	
	@Resource(name = "cts_TiProductsService")
	private ICts_TiProductsService productservice;
	
	@Autowired
	private UUsersMapper userMapper;
	@Autowired
	private TiProductshowtemplateinfoMapper tishowtempinfoMapper;
	@Autowired
	private TiProductshowtemplateMapper tishowtempMapper;
	
	/**
	 * 产品列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/tiprolist")
	public String getTiproductlist(@RequestParam(required=false,defaultValue="0")int isAll) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(isAll>0){
				rq=productservice.getTiProListAll();
			}else{
				rq=productservice.getTiProList();
			}
			
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/editTiproduct")
	public String editTiproduct(String productJson,String imgJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiProductResult product=(TiProductResult) JsonUtil.jsonStrToObject(productJson, TiProductResult.class);
			if(product==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数productJson有误!");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(product.getProductid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误，productid为空");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(product.getTitle())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("产品名称不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(product.getDefaultimg())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("产品默认图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			List<ImageInfo> imglist=Json2Objects.getParam_ImageInfo(imgJson);
			if(imglist==null||imglist.size()<1){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("产品轮播图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(product!=null){
				product.setImglist(imglist);
			}
			
			rq=productservice.editTiproduct(product);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/editDescriptionImglist")
	public String editDescriptionImglist(String productid,String imgJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ObjectUtil.isEmpty(productid)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误，productid为空");
				return JsonUtil.objectToJsonStr(rq);
			}
			List<ImageInfo> imglist=Json2Objects.getParam_ImageInfo(imgJson);
			if(imglist==null||imglist.size()<1){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("产品轮播图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			
			rq=productservice.editDescriptionImglist(ObjectUtil.parseLong(productid),imglist);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 款式列表
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/tiStyleList")
	public String tiProductStyleList(long productid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=productservice.getproductStyleList(productid);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/addTiStyle")
	public String addTiStyle(String styleJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiProductstyles style=(TiProductstyles) JsonUtil.jsonStrToObject(styleJson, TiProductstyles.class);
			if(style==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数styleJson有误!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			if(ObjectUtil.isEmpty(style.getProductid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误，productid不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("款式名称不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getImgcount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("图片张数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getDefaultimg())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("款式默认图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getPrice())||style.getPrice().doubleValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("价格不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getPromoterprice())||style.getPromoterprice().doubleValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("推广价不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getHight())||style.getHight().doubleValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("纸高不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=productservice.addTiStyle(style);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	@ResponseBody
	@RequestMapping(value = "/editTiStyle")
	public String editTiStyle(String styleJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiProductstyles style=(TiProductstyles) JsonUtil.jsonStrToObject(styleJson, TiProductstyles.class);
			if(style==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数styleJson有误!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			if(ObjectUtil.isEmpty(style.getStyleid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误，styleid为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getDescription())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("款式名称不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getImgcount())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("图片张数不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getDefaultimg())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("款式默认图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getPrice())||style.getPrice().doubleValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("价格不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getPromoterprice())||style.getPromoterprice().doubleValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("推广价不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getWidth())||style.getWidth().doubleValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("纸宽不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(style.getHight())||style.getHight().doubleValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("纸高不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=productservice.editTiStyle(style);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	/**
	 * 款式背景图列表
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/tiStylesLayersList")
	public String tiStylesLayersList(long styleid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=productservice.getTiStylesLayersList(styleid);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/addTiStylesLayers")
	public String addTiStylesLayers(String styleJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiProductstyleslayers styleslayers=(TiProductstyleslayers) JsonUtil.jsonStrToObject(styleJson, TiProductstyleslayers.class);
			if(styleslayers==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数styleJson有误!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			if(ObjectUtil.isEmpty(styleslayers.getStyleid())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误，styleid不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(styleslayers.getLayerimg())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("背景图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(styleslayers.getLayerimgpreview())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("背景预览图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(styleslayers.getSort())||styleslayers.getSort().intValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("序号不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=productservice.addTiStylesLayers(styleslayers);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/editTiStylesLayers")
	public String editTiStylesLayers(String styleJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiProductstyleslayers styleslayers=(TiProductstyleslayers) JsonUtil.jsonStrToObject(styleJson, TiProductstyleslayers.class);
			if(styleslayers==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数styleJson有误!");
				return JsonUtil.objectToJsonStr(rq);
			}			
			if(ObjectUtil.isEmpty(styleslayers.getId())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误，id不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(styleslayers.getLayerimg())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("背景图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(styleslayers.getLayerimgpreview())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("背景预览图不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(styleslayers.getSort())||styleslayers.getSort().intValue()<0){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("序号不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=productservice.editTiStylesLayers(styleslayers);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delTiStylesLayers")
	public String delTiStylesLayers(String layerid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
					
			if(ObjectUtil.isEmpty(layerid)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误，layerid不能为空！");
				return JsonUtil.objectToJsonStr(rq);
			}		
			rq=productservice.delTiStylesLayers(ObjectUtil.parseLong(layerid));
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	@ResponseBody
	@RequestMapping(value = "/setStyleCoordinate")
	public String setStyleCoordinate(String styleid,String imgcoordJson,String printcoordJson,String fontimgcoordJson) throws Exception{
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			PStylecoordinateitem imgcoord=(PStylecoordinateitem) JsonUtil.jsonStrToObject(imgcoordJson, PStylecoordinateitem.class);
			PStylecoordinateitem printcoord=(PStylecoordinateitem) JsonUtil.jsonStrToObject(printcoordJson, PStylecoordinateitem.class);
			PStylecoordinateitem fontimgcoord=(PStylecoordinateitem) JsonUtil.jsonStrToObject(fontimgcoordJson, PStylecoordinateitem.class);
			if(ObjectUtil.isEmpty(styleid)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误:styleid不能为空");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(imgcoord==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误:imgcoordJson");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(printcoord==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误:printcoordJson");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(fontimgcoord==null){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误:fontimgcoordJson");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=productservice.setStyleCoordinate(ObjectUtil.parseLong(styleid),imgcoord,printcoord,fontimgcoord);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/setStyleStatus")
	public String setStyleStatus(String styleid,Integer status) throws Exception{
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(ObjectUtil.isEmpty(styleid)){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数有误:styleid不能为空");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=productservice.setStyleStatus(ObjectUtil.parseLong(styleid),status);
		}else { 
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * 新增翻页效果
	 * @param myproductTempJson
	 * @param productstyleJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrEditProductPageturn")
	public String addOrEditProductPageturn(String productJson) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			TiProductshowtemplateinfo workparam = (TiProductshowtemplateinfo)JsonUtil.jsonStrToObject(productJson,TiProductshowtemplateinfo.class);
			
			if (workparam == null||ObjectUtil.isEmpty(workparam.getCateid())||ObjectUtil.isEmpty(workparam.getShowstyleid())) { 
				rq.setStatu(ReturnStatus.ParamError_1);
				rq.setStatusreson("参数不全");
				return JsonUtil.objectToJsonStr(rq);
			}
			if(ObjectUtil.isEmpty(workparam.getTempname())){
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("模板名称不能为空!");
				return JsonUtil.objectToJsonStr(rq);
			}
			//图集
			List<ImageInfo> imglist = new ArrayList<ImageInfo>();
			if (!ObjectUtil.isEmpty(workparam.getImglist())) {
				for (ImageInfo img : workparam.getImglist()) {
					if (!(ObjectUtil.isEmpty(img) || ObjectUtil.isEmpty(img.getUrl()))) {
						imglist.add(img);
					}
				}
			}
			if (imglist != null && imglist.size() > 0) {
				workparam.setImglist(imglist);
				workparam.setImgjson(JsonUtil.objectToJsonStr(imglist));
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请上传图片!");
				return JsonUtil.objectToJsonStr(rq);
			}
			rq=productservice.addOrEditProductPageturn(user.getUserId(),workparam);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}


	@ResponseBody
	@RequestMapping(value = "/productShowInfo")
	public String productShowInfo(@RequestParam(required = false, defaultValue = "0")int  tempid) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			if(tempid>0){
				TiProductshowtemplate newtempinfo= tishowtempMapper.selectByPrimaryKey(tempid);
				if(newtempinfo!=null){
					List<TiProductshowtemplateinfo> infolist=tishowtempinfoMapper.selectByTempId(tempid);
					for (TiProductshowtemplateinfo infotemp : infolist) {
						infotemp.setImglist((List<ImageInfo>)JsonUtil.jsonToList(infotemp.getImgjson()));
					}
					newtempinfo.setTemplateinfos(infolist); 
				}
				rq.setBasemodle(newtempinfo); 
			}
			rq.setStatu(ReturnStatus.Success); 
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/getProductShowTempList")
	public String getProductShowTempList(int index,int size) throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=productservice.getProductShowTempList(user.getUserId(),index,size);
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	


	@ResponseBody
	@RequestMapping(value = "/getproductshowstyles")
	public String getproductshowstyles() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=productservice.getproductshowstyles();
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/getproductshowproducts")
	public String getproductshowproducts() throws Exception {
		ReturnModel rq=new ReturnModel();
		LoginSuccessResult user= super.getLoginUser();
		if(user!=null){
			rq=productservice.getproductshowproducts();
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
			return JsonUtil.objectToJsonStr(rq);
		}
		return JsonUtil.objectToJsonStr(rq);
	}
}
