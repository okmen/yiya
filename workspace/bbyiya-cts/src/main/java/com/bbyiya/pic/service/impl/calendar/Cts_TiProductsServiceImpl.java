package com.bbyiya.pic.service.impl.calendar;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductshowproductsMapper;
import com.bbyiya.dao.TiProductshowstylesMapper;
import com.bbyiya.dao.TiProductshowtemplateMapper;
import com.bbyiya.dao.TiProductshowtemplateinfoMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiProductstyleslayersMapper;
import com.bbyiya.dao.TiStylecoordinateMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ProductStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.TiProducts;
import com.bbyiya.model.TiProductshowproducts;
import com.bbyiya.model.TiProductshowstyles;
import com.bbyiya.model.TiProductshowtemplate;
import com.bbyiya.model.TiProductshowtemplateinfo;
import com.bbyiya.model.TiProductstyles;
import com.bbyiya.model.TiProductstyleslayers;
import com.bbyiya.model.TiStylecoordinate;
import com.bbyiya.pic.service.calendar.ICts_TiProductsService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.calendar.product.TiProductResult;
import com.bbyiya.vo.calendar.product.TiStyleLayerResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("cts_TiProductsService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Cts_TiProductsServiceImpl implements ICts_TiProductsService{
	
	@Autowired
	private TiProductsMapper productsMapper;
	@Autowired
	private TiProductstylesMapper styleMapper;
	@Autowired
	private TiProductstyleslayersMapper styleslayersMapper;
	@Autowired
	private TiStylecoordinateMapper stylecoordMapper;
	@Autowired
	private PStylecoordinateitemMapper pstylecoorditemMapper;
	
	@Autowired
	private TiProductshowtemplateMapper tishowtempMapper;
	@Autowired
	private TiProductshowtemplateinfoMapper tishowtempinfoMapper;
	@Autowired
	private TiProductshowstylesMapper tishowstyleMapper;
	
	@Autowired
	private TiProductshowproductsMapper tishowproductMapper;
	
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	
	
	public ReturnModel getTiProListAll(){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		//01产品列表
		List<TiProductResult> proList=productsMapper.findProductResultlist();
		if(proList!=null&&proList.size()>0){
			for (TiProductResult product : proList) {
				product=getProductResult(product);
			}
		}
		rqModel.setBasemodle(proList); 
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	/**
	 * 得到台历产品列表
	 */
	public ReturnModel getTiProList(){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		//01产品列表
		List<TiProductResult> proList=productsMapper.findProductResultlist();
		if(proList!=null&&proList.size()>0){
			List<TiProductResult> productListResult=new ArrayList<TiProductResult>();
			for (TiProductResult product : proList) {
				if(product.getCateid()!=null&&product.getCateid()!=5){
					productListResult.add(getProductResult(product));
				}
			}
			rqModel.setBasemodle(productListResult); 
		}
		
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	
	private TiProductResult getProductResult(TiProductResult product) {
		// 产品展示图集
		if (!ObjectUtil.isEmpty(product.getImgjson())) {
			List<ImageInfo> imList = (List<ImageInfo>) JsonUtil.jsonToList(product.getImgjson());
			if (imList != null && imList.size() > 0) {
				product.setImglist(imList);
			}
		}
		// 产品描述图集
		if (!ObjectUtil.isEmpty(product.getDescriptionimgjson())) {
			List<ImageInfo> imList = (List<ImageInfo>) JsonUtil.jsonToList(product.getDescriptionimgjson());
			if (imList != null && imList.size() > 0) {
				product.setDescriptionImglist(imList);
			}
		}
		// 产品的款式列表
		List<TiProductstyles> styleList = styleMapper.findStylelistByProductId(product.getProductid());
		product.setStylelist(styleList);

		return product;
	}
	

	public ReturnModel editTiproduct(TiProductResult param) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		
		TiProducts pro=productsMapper.selectByPrimaryKey(param.getProductid());
		if(pro!=null){
			pro.setTitle(param.getTitle());
			pro.setDefaultimg(param.getDefaultimg());
			pro.setPrice(param.getPrice());
			pro.setDescription(param.getDescription());
			pro.setCateid(param.getCateid());
			pro.setImgjson(JsonUtil.objectToJsonStr(param.getImglist()));
			productsMapper.updateByPrimaryKey(pro);
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	
	/**
	 * 修改详情图集
	 * @param productid
	 * @param imglist
	 * @return
	 * @throws Exception
	 */
	public ReturnModel editDescriptionImglist(Long productid,List<ImageInfo> imglist) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		
		TiProducts pro=productsMapper.selectByPrimaryKey(productid);
		if(pro!=null){
			pro.setDescriptionimgjson(JsonUtil.objectToJsonStr(imglist));
			productsMapper.updateByPrimaryKey(pro);
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	

	public ReturnModel getproductStyleList(Long productid){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		List<TiProductstyles> styleList=styleMapper.findAllStylelistByProductId(productid);
		rqModel.setBasemodle(styleList);
		rqModel.setStatu(ReturnStatus.Success); 		
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	
	public ReturnModel editTiStyle(TiProductstyles param) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiProductstyles style=styleMapper.selectByPrimaryKey(param.getStyleid());
		if(style!=null){
			style.setDescription(param.getDescription());
			style.setDefaultimg(param.getDefaultimg());
			style.setPrice(param.getPrice());
			style.setPromoterprice(param.getPromoterprice());
			style.setImgcount(param.getImgcount());
			style.setWidth(param.getWidth());
			style.setHight(param.getHight());
			styleMapper.updateByPrimaryKey(style);
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("编辑操作成功！");
		return rqModel;
	}

	public ReturnModel addTiStyle(TiProductstyles param) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiProductstyles style=new TiProductstyles();
		Long styleid=styleMapper.getMaxStyleIdByProductId(param.getProductid());
		if(styleid==null){
			styleid=param.getProductid();
		}else{
			styleid=styleid.longValue()+1;
		}
		style.setStyleid(styleid);
		style.setProductid(param.getProductid());
		style.setDescription(param.getDescription());
		style.setDefaultimg(param.getDefaultimg());
		style.setPrice(param.getPrice());
		style.setPromoterprice(param.getPromoterprice());
		style.setImgcount(param.getImgcount());
		style.setWidth(param.getWidth());
		style.setHight(param.getHight());
		//产品类型默认为2
		style.setProducttype(2);
		//状态默认为零
		style.setStatus(Integer.parseInt(ProductStatusEnum.drafts.toString()));
		styleMapper.insert(style);
		
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("新增操作成功！");
		return rqModel;
	}
	
	/**
	 * 款式背景图列表
	 */
	public ReturnModel getTiStylesLayersList(Long styleid){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		HashMap<String, Object> mapresult=new HashMap<String, Object>();
		//得到坐标
		TiStylecoordinate stylecoord=stylecoordMapper.selectByPrimaryKey(styleid);
		if(stylecoord!=null){
			PStylecoordinateitem imgcoorddata=pstylecoorditemMapper.selectByPrimaryKey(stylecoord.getImgcoordid().longValue());
			PStylecoordinateitem printcoorddata=pstylecoorditemMapper.selectByPrimaryKey(stylecoord.getPrintcoordid().longValue());
			PStylecoordinateitem fontimgcoorddata=pstylecoorditemMapper.selectByPrimaryKey(stylecoord.getFrontimgcoordid().longValue());
			mapresult.put("imgcoorddata", imgcoorddata);
			mapresult.put("printcoorddata", printcoorddata);
			mapresult.put("fontimgcoorddata", fontimgcoorddata);
		}
		List<TiStyleLayerResult> styleList=styleslayersMapper.findLayerlistByStyleId(styleid);
		mapresult.put("styleList", styleList);
		rqModel.setBasemodle(mapresult);
		rqModel.setStatu(ReturnStatus.Success); 		
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	

	public ReturnModel addTiStylesLayers(TiProductstyleslayers param) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiProductstyleslayers styleslayers=new TiProductstyleslayers();
		styleslayers.setStyleid(param.getStyleid());
		styleslayers.setIsround(param.getIsround());
		styleslayers.setLayerimg(param.getLayerimg());
		styleslayers.setLayerimgpreview(param.getLayerimgpreview());
		styleslayers.setSort(param.getSort());
		styleslayers.setCreatetime(new Date());
		styleslayersMapper.insert(styleslayers);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("新增操作成功！");
		return rqModel;
	}
	
	public ReturnModel editTiStylesLayers(TiProductstyleslayers param) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiProductstyleslayers styleslayers=styleslayersMapper.selectByPrimaryKey(param.getId());
		if(styleslayers==null){
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("背景id不存在！");
			return rqModel;
		}
		styleslayers.setIsround(param.getIsround());
		styleslayers.setLayerimg(param.getLayerimg());
		styleslayers.setLayerimgpreview(param.getLayerimgpreview());
		styleslayers.setSort(param.getSort());
		styleslayersMapper.updateByPrimaryKey(styleslayers);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("修改操作成功！");
		return rqModel;
	}

	public ReturnModel delTiStylesLayers(Long layersid) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiProductstyleslayers styleslayers=styleslayersMapper.selectByPrimaryKey(layersid);
		if(styleslayers==null){
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("layersid不存在！");
			return rqModel;
		}
		styleslayersMapper.deleteByPrimaryKey(layersid);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("删除操作成功！");
		return rqModel;
	}
	

	public ReturnModel setStyleCoordinate(Long styleid,PStylecoordinateitem imgcoordparam,PStylecoordinateitem printcoordparam,PStylecoordinateitem fontimgcoordparam) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiStylecoordinate stylecoord=stylecoordMapper.selectByPrimaryKey(styleid);
		if(stylecoord==null){
			stylecoord=new TiStylecoordinate();
			pstylecoorditemMapper.insertReturnId(imgcoordparam);
			pstylecoorditemMapper.insertReturnId(printcoordparam);
			pstylecoorditemMapper.insertReturnId(fontimgcoordparam);
			stylecoord.setStyleid(styleid);
			stylecoord.setImgcoordid(imgcoordparam.getCoordid().intValue());
			stylecoord.setPrintcoordid(printcoordparam.getCoordid().intValue());
			stylecoord.setFrontimgcoordid(fontimgcoordparam.getCoordid().intValue());
			stylecoordMapper.insert(stylecoord);
		}else{
			PStylecoordinateitem imgcoorddata=pstylecoorditemMapper.selectByPrimaryKey(stylecoord.getImgcoordid().longValue());
			if(imgcoorddata!=null){
				imgcoorddata.setPointhight(imgcoordparam.getPointhight());
				imgcoorddata.setPointleft(imgcoordparam.getPointleft());
				imgcoorddata.setPointtop(imgcoordparam.getPointtop());
				imgcoorddata.setPointwidth(imgcoordparam.getPointwidth());
				pstylecoorditemMapper.updateByPrimaryKey(imgcoorddata);
			}
			PStylecoordinateitem printcoorddata=pstylecoorditemMapper.selectByPrimaryKey(stylecoord.getPrintcoordid().longValue());
			if(printcoorddata!=null){
				printcoorddata.setPointhight(printcoordparam.getPointhight());
				printcoorddata.setPointleft(printcoordparam.getPointleft());
				printcoorddata.setPointtop(printcoordparam.getPointtop());
				printcoorddata.setPointwidth(printcoordparam.getPointwidth());
				pstylecoorditemMapper.updateByPrimaryKey(printcoorddata);
			}
			PStylecoordinateitem fontimgcoorddata=pstylecoorditemMapper.selectByPrimaryKey(stylecoord.getFrontimgcoordid().longValue());
			if(fontimgcoorddata!=null){
				fontimgcoorddata.setPointhight(fontimgcoordparam.getPointhight());
				fontimgcoorddata.setPointleft(fontimgcoordparam.getPointleft());
				fontimgcoorddata.setPointtop(fontimgcoordparam.getPointtop());
				fontimgcoorddata.setPointwidth(fontimgcoordparam.getPointwidth());
				pstylecoorditemMapper.updateByPrimaryKey(fontimgcoorddata);
			}
		}
		
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("设置成功！");
		return rqModel;
	}

	public ReturnModel setStyleStatus(Long styleid,Integer status) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiProductstyles style=styleMapper.selectByPrimaryKey(styleid);
		if(style!=null){
			if(status!=null&&status.intValue()==Integer.parseInt(ProductStatusEnum.ok.toString())){
				style.setStatus(status);
			}else{
				style.setStatus(0);
			}
			styleMapper.updateByPrimaryKey(style);
		}
		return rqModel;
	}
	
	/**
	 * 新增或修改分页效果
	 * @param userid
	 * @param tempinfo
	 * @return
	 * @throws Exception
	 */
	public ReturnModel addOrEditProductPageturn(Long userid,TiProductshowtemplateinfo param) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiProductshowtemplate newtempinfo=null;
		//是否是新增模板
		boolean isadd=false ;
		if(!ObjectUtil.isEmpty(param.getTempid())){
			newtempinfo=tishowtempMapper.selectByPrimaryKey(param.getTempid());
		}
		if(newtempinfo==null){
			newtempinfo=new TiProductshowtemplate();
			isadd=true;
		}
		//默认为通用模板
		if(param.getTempname()!=null&&param.getTempname().equals("通用模板")){
			newtempinfo.setIsdefault(1);
		}else{
			newtempinfo.setIsdefault(0);
		}
		if(!ObjectUtil.isEmpty(param.getTempname())){
			newtempinfo.setTempname(param.getTempname());
		}
		if(isadd){
			tishowtempMapper.insertReturnId(newtempinfo);
		}else{
			tishowtempMapper.updateByPrimaryKey(newtempinfo);
		}
		//是否是新增款式图集
		boolean isNew=true;
		List<TiProductshowtemplateinfo> infolist=tishowtempinfoMapper.selectByTempId(param.getTempid());
		if(infolist!=null&&infolist.size()>0){
			for (TiProductshowtemplateinfo info : infolist) {
				if(info.getCateid().intValue()==param.getCateid().intValue()&& info.getShowstyleid().intValue()==param.getShowstyleid().intValue()){
					if(!ObjectUtil.isEmpty(param.getImglist())){
						info.setImgjson(JsonUtil.objectToJsonStr(param.getImglist()));
						tishowtempinfoMapper.updateByPrimaryKeySelective(info);
						isNew=false;
					}
				}
			}
		}
		if(isNew){
			if(ObjectUtil.isEmpty(param.getShowstyleid())||ObjectUtil.isEmpty(param.getTempid())||ObjectUtil.isEmpty(param.getImglist())){
				rqModel.setStatu(ReturnStatus.ParamError);
				rqModel.setStatusreson("参数不全！(款式/模板Id/图片是否为空)");
				return rqModel;
			}
			TiProductshowtemplateinfo newinfo = new TiProductshowtemplateinfo();
			newinfo.setImgjson(JsonUtil.objectToJsonStr(param.getImglist()));
			newinfo.setImglist(param.getImglist());
			newinfo.setCateid(param.getCateid());
			newinfo.setShowstyleid(param.getShowstyleid());
			newinfo.setTempid(newtempinfo.getTempid());
			tishowtempinfoMapper.insert(newinfo);
			rqModel.setStatusreson("新增成功！");
		}else {
			rqModel.setStatusreson("修改成功！");
		}
		rqModel.setStatu(ReturnStatus.Success);
		return rqModel;
	}
	
	public ReturnModel getProductShowTempList(Long userid,int index,int size) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		PageHelper.startPage(index, size);
		List<TiProductshowtemplate> list=tishowtempMapper.selectByAll(null);
		PageInfo<TiProductshowtemplate> pageresult=new PageInfo<TiProductshowtemplate>(list);
		if(pageresult!=null&&pageresult.getList()!=null&&pageresult.getList().size()>0){
			for (TiProductshowtemplate temp : pageresult.getList()) {
				List<TiProductshowtemplateinfo> infolist=tishowtempinfoMapper.selectByTempId(temp.getTempid());
				for (TiProductshowtemplateinfo infotemp : infolist) {
					infotemp.setImglist((List<ImageInfo>)JsonUtil.jsonToList(infotemp.getImgjson()));
				}
				temp.setTemplateinfos(infolist);
			}
		}
		rqModel.setBasemodle(pageresult);
		rqModel.setStatu(ReturnStatus.Success); 		
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	

	public ReturnModel getproductshowstyles(){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		List<TiProductshowstyles> styleList=tishowstyleMapper.selectByAll();
		rqModel.setBasemodle(styleList);
		rqModel.setStatu(ReturnStatus.Success); 		
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
	

	public ReturnModel getproductshowproducts(){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		List<TiProductshowproducts> productList=tishowproductMapper.selectByAll();
		rqModel.setBasemodle(productList);
		rqModel.setStatu(ReturnStatus.Success); 		
		rqModel.setStatusreson("操作成功！");
		return rqModel;
	}
}
