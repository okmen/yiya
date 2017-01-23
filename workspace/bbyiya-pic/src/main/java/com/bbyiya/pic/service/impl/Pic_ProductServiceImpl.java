package com.bbyiya.pic.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;










import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PMyproductdetailsMapper;
import com.bbyiya.dao.PMyproductsMapper;
import com.bbyiya.dao.PProductdetailsMapper;
import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.MyProductStatusEnum;
import com.bbyiya.model.PMyproductdetails;
import com.bbyiya.model.PMyproducts;
import com.bbyiya.model.PProductdetails;
import com.bbyiya.pic.service.IPic_ProductService;
import com.bbyiya.pic.vo.product.MyProductParam;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.product.ProductSampleVo;

@Service("pic_productService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_ProductServiceImpl implements IPic_ProductService {

	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductdetailsMapper detailMapper;

	@Autowired
	private PMyproductsMapper myMapper;
	@Autowired
	private PMyproductdetailsMapper myDetaiMapper;
	

	public ReturnModel getProductSamples(Long productId) {
		ReturnModel rq = new ReturnModel();
		ProductSampleVo product = productsMapper.getProductBaseVoByProId(productId);
		List<PProductdetails> details= detailMapper.findDetailsByProductId(productId);
		if(details!=null&details.size()>0){
			product.setSampleItems(details);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(product);
		return rq;
	}
	
	
	public ReturnModel saveOrEdit_MyProducts(Long userId, MyProductParam param) {
		ReturnModel rq = new ReturnModel();
		Long cartIdTemp=0l;
		if(param!=null){
			if(param.getCartid()!=null&&param.getCartid()>0){//更新
				cartIdTemp=param.getCartid();
				PMyproducts myproducts= myMapper.selectByPrimaryKey(param.getCartid());
				if(myproducts!=null){//修改
					if(!ObjectUtil.isEmpty(param.getTitle())){
						myproducts.setTitle(param.getTitle()); 
					}
					if(!ObjectUtil.isEmpty(param.getAuthor())){
						myproducts.setAuthor(param.getAuthor());
					}
					//更新用户作品基本信息
					myMapper.updateByPrimaryKeySelective(myproducts);
					if(param.getDetails()!=null&&param.getDetails().size()>0){
						int maxSort=myDetaiMapper.getMaxSort(param.getCartid());
						for (PMyproductdetails de : param.getDetails()) {
							 if(de.getPdid()!=null&&de.getPdid()>0){
								 PMyproductdetails temp=myDetaiMapper.selectByPrimaryKey(de.getPdid());
								 if(temp!=null){
									 de.setCreatetime(new Date());
									 myDetaiMapper.updateByPrimaryKeySelective(de);
								 }
							 }else {
								 maxSort++;
								 de.setCreatetime(new Date());
								 de.setSort(maxSort);//设置排序 
								 myDetaiMapper.insert(de);
							 }
						}
					}
				}
			}else {//新增
				PMyproducts myproduct=new PMyproducts();
				myproduct.setAuthor(param.getAuthor());
				myproduct.setTitle(param.getTitle());
				myproduct.setUserid(userId);
				myproduct.setProductid(param.getProductid());
				myproduct.setCreatetime(new Date());
				myproduct.setStatus(Integer.parseInt(MyProductStatusEnum.ok.toString())) ; 
				myMapper.insertReturnId(myproduct);
				cartIdTemp=myproduct.getCartid();
				if(param.getDetails()!=null&&param.getDetails().size()>0){
					int sort=1;
					for (PMyproductdetails de : param.getDetails()) {
						 de.setCreatetime(new Date());
						 de.setSort(sort);//设置排序
						 de.setCartid(myproduct.getCartid());
						 myDetaiMapper.insert(de);
						 sort++;
					}
				}
			}
		}
		rq.setStatu(ReturnStatus.Success);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("cartId", cartIdTemp);
		rq.setBasemodle(map);   
		return rq;
	}

}
