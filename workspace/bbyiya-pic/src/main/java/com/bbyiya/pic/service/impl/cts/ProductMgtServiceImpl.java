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
		rq.setStatusreson("��ȡ��Ʒ�б�ɹ���");
		return rq;
	}
	
	/**
	 * ���ݲ�ƷID�޸Ĳ�Ʒ��Ϣ
	 * @param productDto
	 * @param productid
	 * @return
	 */
	public ReturnModel addAndupdateProductStyles(Long userId,PProductstyles style) {
		ReturnModel rq = new ReturnModel();	
		
		rq.setStatu(ReturnStatus.SystemError);
		if(style==null){
			rq.setStatusreson("��������,styleΪnull");
			return rq;
		}
		if(ObjectUtil.isEmpty(style.getStyleid())){
			rq.setStatusreson("��������,styleidΪ��");
			return rq;
		}
		if(ObjectUtil.isEmpty(style.getProductid())){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��������,productidΪ��");
			return rq;
		}		
		if(ObjectUtil.isEmpty(style.getPropertystr())){
			rq.setStatusreson("��������,��ʽ���Ա���");
			return rq;
		} 
		if(ObjectUtil.isEmpty(style.getDefaultimg())){
			rq.setStatusreson("��������,Ĭ��ͼƬ������");
			return rq;
		} 
		if(ObjectUtil.isEmpty(style.getPrice())){
			rq.setStatusreson("��������,�۸������");
			return rq;
		} 
		
		if(!ObjectUtil.validSqlStr(style.getDefaultimg())
				||!ObjectUtil.validSqlStr(style.getDescription())
				||!ObjectUtil.validSqlStr(style.getPropertystr())){
			rq.setStatusreson("���ڷǷ��ַ�");
			return rq;
		}
		PProducts product=productsMapper.selectByPrimaryKey(style.getProductid());
		if(product==null){
			rq.setStatusreson("productId��ƷID��ϵͳ�в����ڣ�");
			return rq;
		}
		//���״̬Ϊ����Ĭ��Ϊ������״̬
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
		//�����ѡΪĬ�Ͽ��������
		if(style.getIsdefault()==1){
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
			rq.setStatusreson("������Ʒ��ʽ�ɹ���");
		}else{
			productstyleMapper.updateByPrimaryKeySelective(newstyle);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("�޸Ĳ�Ʒ��ʽ�ɹ���");
		}
		
		
		return rq;
	}
	
}
