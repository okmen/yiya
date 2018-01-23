package com.bbyiya.pic.service.impl.cts;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.dto.PProductsDTO;
import com.bbyiya.enums.ProductStatusEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PProducts;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.pic.service.cts.IProductMgtService;
import com.bbyiya.utils.DateUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.PProductStyleResult;
import com.bbyiya.vo.product.ProductSearchParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("productMgtService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class ProductMgtServiceImpl implements IProductMgtService {

	/*------------------Product---------------------------*/
	@Autowired
	private PProductsMapper productsMapper;
	
	@Autowired
	private PProductstylesMapper productstyleMapper;
	
	/**
	 * 根据查询条件获取产品列表
	 * @param index
	 * @param size
	 * @param searchParam
	 * @return
	 */
	public ReturnModel findProductListBySearchParam(int index,int size,ProductSearchParam searchParam) {
		ReturnModel rq = new ReturnModel();
		
		if(searchParam==null)
			searchParam=new ProductSearchParam();
		if(searchParam.getStatus()==null) searchParam.setStatus(Integer.parseInt(ProductStatusEnum.ok.toString()));
		PageHelper.startPage(index, size);
		List<PProducts> list=productsMapper.findProductListBySearchParam(searchParam);	
		PageInfo<PProducts> result=new PageInfo<PProducts>(list);
		rq.setBasemodle(result);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取产品列表成功！");
		return rq;
	}
	/**
	 * 根据产品ID修改产品信息
	 * @param productDto
	 * @param productid
	 * @return
	 */
	public ReturnModel updateProductByProductId(PProductsDTO pdto) {
		ReturnModel rq = new ReturnModel();		
		if (pdto == null) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("抱歉！参数错误");
			return rq;
		}
		if(ObjectUtil.isEmpty(pdto.getProductid())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误");
			return rq;
		}
		
		PProducts product=productsMapper.selectByPrimaryKey(pdto.getProductid());
		if(!ObjectUtil.isEmpty(pdto.getTitle())){
			product.setTitle(pdto.getTitle());
		}
		if(!ObjectUtil.isEmpty(pdto.getDefaultimg())) product.setDefaultimg(pdto.getDefaultimg());
		product.setDescription(pdto.getDescription());
		product.setPostmodelid(pdto.getPostmodelid());
		product.setPrice(pdto.getPrice());
		product.setSort(pdto.getSort());
		product.setStatus(pdto.getStatus());
		product.setUpdatetime(new Date());
		productsMapper.updateByPrimaryKeySelective(product);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("修改产品成功！");
		return rq;
	}
	
	
	/**
	 * 根据查询条件获取产品款式列表
	 * @param index
	 * @param size
	 * @param searchParam
	 * @return
	 */
	public ReturnModel findProductStylesBySearchParam(int index,int size,ProductSearchParam searchParam) {
		ReturnModel rq = new ReturnModel();
		
		if(searchParam==null)
			searchParam=new ProductSearchParam();
		if(searchParam.getStatus()==null) searchParam.setStatus(Integer.parseInt(ProductStatusEnum.ok.toString()));
		PageHelper.startPage(index, size);
		
		List<PProductStyleResult> stylelist=productstyleMapper.findProductStylesBySearchParam(searchParam);		
		PageInfo<PProductStyleResult> result=new PageInfo<PProductStyleResult>(stylelist);
		if(result!=null&&result.getList()!=null&&result.getList().size()>0){
			for (PProductStyleResult style : result.getList()) {
				PProducts pp=productsMapper.selectByPrimaryKey(style.getProductId());
				if(pp==null) continue;
				style.setProductTitle(pp.getTitle());
				style.setCreateTimeStr(DateUtil.getTimeStr(style.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));		
			}
		}
		rq.setBasemodle(result);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("获取产品列表成功！");
		return rq;
	}
	
	/**
	 * 根据产品ID修改产品信息
	 * @param productDto
	 * @param productid
	 * @return
	 */
	public ReturnModel addAndupdateProductStyles(Long userId,PProductstyles style) {
		ReturnModel rq = new ReturnModel();	
		
		rq.setStatu(ReturnStatus.SystemError);
		if(style==null){
			rq.setStatusreson("参数有误,style为null");
			return rq;
		}
		if(ObjectUtil.isEmpty(style.getStyleid())){
			rq.setStatusreson("参数有误,styleid为空");
			return rq;
		}
		if(ObjectUtil.isEmpty(style.getProductid())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数有误,productid为空");
			return rq;
		}		
		if(ObjectUtil.isEmpty(style.getPropertystr())){
			rq.setStatusreson("参数有误,款式属性必填");
			return rq;
		} 
		if(ObjectUtil.isEmpty(style.getDefaultimg())){
			rq.setStatusreson("参数有误,默认图片必须填");
			return rq;
		} 
		if(ObjectUtil.isEmpty(style.getPrice())){
			rq.setStatusreson("参数有误,价格必须填");
			return rq;
		} 
		
		if(!ObjectUtil.validSqlStr(style.getDefaultimg())
				||!ObjectUtil.validSqlStr(style.getDescription())
				||!ObjectUtil.validSqlStr(style.getPropertystr())){
			rq.setStatusreson("存在非法字符");
			return rq;
		}
		PProducts product=productsMapper.selectByPrimaryKey(style.getProductid());
		if(product==null){
			rq.setStatusreson("productId产品ID在系统中不存在！");
			return rq;
		}
		//如果状态为空则默认为销售中状态
		if(ObjectUtil.isEmpty(style.getStatus())){
			style.setStatus(Integer.parseInt(ProductStatusEnum.ok.toString()));
		}
		PProductstyles newstyle=productstyleMapper.selectByPrimaryKey(style.getStyleid());
		boolean isAdd=false;
		if(newstyle==null){
			isAdd=true;
			newstyle=new PProductstyles();
		}
		newstyle.setDefaultimg(style.getDefaultimg());
		newstyle.setDescription(style.getDescription());		
		newstyle.setPrice(style.getPrice());
		newstyle.setPropertystr(style.getPropertystr());
		newstyle.setStatus(style.getStatus());
		//如果勾选为默认款，处理。。。
		if(style.getIsdefault()!=null&&style.getIsdefault()==1){
			productstyleMapper.updateIsDefaultByProductId(style.getProductid());
		}
		newstyle.setIsdefault(style.getIsdefault());
		if(isAdd){
			newstyle.setStyleid(style.getStyleid());
			newstyle.setUserid(userId);
			newstyle.setCreatetime(new Date());
			newstyle.setProductid(style.getProductid());
			productstyleMapper.insert(newstyle);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("新增产品款式成功！");
		}else{
			productstyleMapper.updateByPrimaryKeySelective(newstyle);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("修改产品款式成功！");
		}
		
		
		return rq;
	}
	
}
