package com.bbyiya.pic.service.impl.cts;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PProductsMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.dao.PScenesMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PScenes;
import com.bbyiya.pic.service.cts.IScenseService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;


@Service("scenseService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class ScenseServiceImpl implements IScenseService{
	
	@Autowired
	private PProductsMapper productsMapper;
	@Autowired
	private PProductstylesMapper styleMapper;
	@Autowired
	private PScenesMapper scenseMapper;
	
	
	/**
	 * ��ӻ��޸ĳ�Ӱ
	 * */
	public ReturnModel addorUpdateScense(Long userid,String myScenseJson){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		JSONObject obj = JSONObject.fromObject(myScenseJson);
		if(obj==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��Ǹ����������");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("title"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("���ⲻ��Ϊ��!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("mintitle"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("С���ⲻ��Ϊ��!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("content"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��Ӱ���ݲ���Ϊ��!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("recreason"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("�Ƽ����ɲ���Ϊ��!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("tips"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��ʾ���벻��Ϊ��!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("productid"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("��ƷID����Ϊ��!");
			return rq;
		}
		PScenes scense=null;
		boolean isadd=true;
		if(obj.has("id")&&!ObjectUtil.isEmpty(obj.getString("id"))){
			scense=scenseMapper.selectByPrimaryKey(obj.getLong("id"));
			isadd=false;
		}else{
			scense=new PScenes();
			scense.setStatus(1);
		}
		scense.setTitle(obj.getString("title"));
		scense.setContent(obj.getString("content"));
		scense.setMintitle(obj.getString("mintitle"));
		scense.setProductid(obj.getLong("productid"));
		scense.setRecreason(obj.getString("recreason"));
		scense.setTips(obj.getString("tips"));
		if(isadd){
			scenseMapper.insert(scense);
			rq.setStatusreson("��ӳ����ɹ���");
		}else{
			scenseMapper.updateByPrimaryKey(scense);
			rq.setStatusreson("�޸ĳ����ɹ���");
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	/**
	 * ��ȡ�����б�
	 * @param index
	 * @param size
	 * @param keywords
	 * @param productId
	 * @return
	 */
	public ReturnModel getScenseList(int index,int size,String keywords,String productid){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<PScenes> sceneslist=scenseMapper.findScenesByProductIdOrkeyword(ObjectUtil.parseLong(productid), keywords);
		PageInfo<PScenes> page=new PageInfo<PScenes>(sceneslist);
		rq.setBasemodle(page);
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("��ȡ�б�ɹ�");
		return rq;
	}
	
}
