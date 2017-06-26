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
	 * 添加或修改场影
	 * */
	public ReturnModel addorUpdateScense(Long userid,String myScenseJson){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.SystemError);	
		JSONObject obj = JSONObject.fromObject(myScenseJson);
		if(obj==null){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("抱歉！参数错误");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("title"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("标题不能为空!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("mintitle"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("小标题不能为空!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("content"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("场影内容不能为空!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("recreason"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("推荐理由不能为空!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("tips"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("提示输入不能为空!");
			return rq;
		}
		if(ObjectUtil.isEmpty(obj.getString("productid"))){
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("产品ID不能为空!");
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
			rq.setStatusreson("添加场景成功！");
		}else{
			scenseMapper.updateByPrimaryKey(scense);
			rq.setStatusreson("修改场景成功！");
		}
		rq.setStatu(ReturnStatus.Success);
		return rq;
	}
	
	/**
	 * 获取场景列表
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
		rq.setStatusreson("获取列表成功");
		return rq;
	}
	
}
