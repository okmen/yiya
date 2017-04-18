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
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
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
	 * ���ݲ�ѯ������ȡ��Ʒ�б�
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
		rq.setStatusreson("��ȡ��Ʒ�б�ɹ���");
		return rq;
	}
	/**
	 * ���ݲ�ƷID�޸Ĳ�Ʒ��Ϣ
	 * @param productDto
	 * @param productid
	 * @return
	 */
	public ReturnModel updateProductByProductId(PProductsDTO pdto) {
		ReturnModel rq = new ReturnModel();		
		if (pdto == null) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��Ǹ����������");
			return rq;
		}
		if(ObjectUtil.isEmpty(pdto.getProductid())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��������");
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
		rq.setStatusreson("�޸Ĳ�Ʒ�ɹ���");
		return rq;
	}
	
	
	/**
	 * ���ݲ�ѯ������ȡ��Ʒ��ʽ�б�
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
		
		List<PProductstyles> stylelist=productstyleMapper.findProductStylesBySearchParam(searchParam);		
		PageInfo<PProductstyles> result=new PageInfo<PProductstyles>(stylelist);
		rq.setBasemodle(result);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("��ȡ��Ʒ�б�ɹ���");
		return rq;
	}
	

	
}
