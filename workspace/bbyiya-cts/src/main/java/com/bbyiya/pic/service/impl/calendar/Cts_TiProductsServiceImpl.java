package com.bbyiya.pic.service.impl.calendar;



import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.common.vo.ImageInfo;
import com.bbyiya.dao.PStylecoordinateitemMapper;
import com.bbyiya.dao.TiProductsMapper;
import com.bbyiya.dao.TiProductshowtemplateMapper;
import com.bbyiya.dao.TiProductshowtemplateinfoMapper;
import com.bbyiya.dao.TiProductstylesMapper;
import com.bbyiya.dao.TiProductstyleslayersMapper;
import com.bbyiya.dao.TiStylecoordinateMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ProductStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PStylecoordinateitem;
import com.bbyiya.model.TiGroupactivity;
import com.bbyiya.model.TiProducts;
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
import com.sdicons.json.mapper.MapperException;




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
	private TiProductshowtemplateMapper tishwotempMapper;
	@Autowired
	private TiProductshowtemplateinfoMapper tishwotempinfoMapper;
	
	/*-------------------用户信息------------------------------------------------*/
	@Autowired
	private UUsersMapper usersMapper;
	
	/**
	 * 得到台历产品列表
	 */
	public ReturnModel getTiProList(){
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);
		//01产品列表
		List<TiProductResult> proList=productsMapper.findProductResultlist();
		if(proList!=null&&proList.size()>0){
			for (TiProductResult product : proList) {
				//产品展示图集
				if(!ObjectUtil.isEmpty(product.getImgjson())){
					List<ImageInfo> imList= (List<ImageInfo>)JsonUtil.jsonToList(product.getImgjson());
					if(imList!=null&&imList.size()>0){
						product.setImglist(imList); 
					}
				}
				//产品描述图集
				if(!ObjectUtil.isEmpty(product.getDescriptionimgjson())){
					List<ImageInfo> imList= (List<ImageInfo>)JsonUtil.jsonToList(product.getDescriptionimgjson());
					if(imList!=null&&imList.size()>0){
						product.setDescriptionImglist(imList); 
					}
				}
				//产品的款式列表
				List<TiProductstyles> styleList=styleMapper.findStylelistByProductId(product.getProductid());
				product.setStylelist(styleList);  

			}
		}
		rqModel.setBasemodle(proList); 
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("操作成功！");
		return rqModel;
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
	public ReturnModel addOrEditProductPageturn(Long userid,TiProductshowtemplate param) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		TiProductshowtemplate newtempinfo=null;
		boolean isadd=false;
		if(!ObjectUtil.isEmpty(param.getTempid())){
			newtempinfo=tishwotempMapper.selectByPrimaryKey(param.getTempid());
		}
		if(newtempinfo==null){
			newtempinfo=new TiProductshowtemplate();
			isadd=true;
		}
		newtempinfo.setIsdefault(0);
		newtempinfo.setTempname(param.getTempname());
		if(isadd){
			tishwotempMapper.insertReturnId(newtempinfo);
		}else{
			tishwotempMapper.updateByPrimaryKey(newtempinfo);
		}
		
		List<TiProductshowtemplateinfo> infolist=tishwotempinfoMapper.selectByTempId(param.getTempid());
		if(infolist!=null&&infolist.size()>0){
			for (TiProductshowtemplateinfo info : infolist) {
				tishwotempinfoMapper.deleteByPrimaryKey(info.getTempinfoid());
			}
		}
		if(param.getTemplateinfos()!=null&&param.getTemplateinfos().size()>0){
			for (TiProductshowtemplateinfo infoparam : param.getTemplateinfos()) {
				TiProductshowtemplateinfo newinfo=new TiProductshowtemplateinfo();
				newinfo.setImgjson(JsonUtil.objectToJsonStr(infoparam.getImglist()));
				newinfo.setImglist(infoparam.getImglist());
				newinfo.setProductid(infoparam.getProductid());
				newinfo.setTempid(newtempinfo.getTempid());
				tishwotempinfoMapper.insert(newinfo);
			}
		}
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("新增成功！");
		return rqModel;
	}
	
	public ReturnModel getProductShowTempList(Long userid,int index,int size) throws Exception{
		ReturnModel rqModel=new ReturnModel();
		rqModel.setStatu(ReturnStatus.ParamError);	
		PageHelper.startPage(index, size);
		List<TiProductshowtemplate> list=tishwotempMapper.selectByAll(null);
		PageInfo<TiProductshowtemplate> pageresult=new PageInfo<TiProductshowtemplate>(list);	
		rqModel.setBasemodle(pageresult);
		rqModel.setStatu(ReturnStatus.Success); 		
		rqModel.setStatusreson("操作成功！");
		return rqModel;
		
	}
	
}
