package com.bbyiya.pic.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PCommentstempMapper;
import com.bbyiya.dao.PCommentstipsMapper;
import com.bbyiya.dao.PMyproductcommentsMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PCommentstemp;
import com.bbyiya.model.PCommentstips;
import com.bbyiya.model.PMyproductcomments;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.IPic_CommentService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("pic_commentService")
@Transactional(rollbackFor = { RuntimeException.class, Exception.class })
public class Pic_CommentServiceImpl implements IPic_CommentService{
	@Autowired
	private PCommentstempMapper commentstempMapper;
	@Autowired
	private PCommentstipsMapper tipsMapper;
	@Autowired
	private UUsersMapper usersMapper;
	@Autowired
	private PMyproductcommentsMapper myproductcommentsMapper;
	
	
	public List<PCommentstemp> findCommentResultList(Long productId){
		List<PCommentstemp> list=commentstempMapper.findTempsByProductId(productId);
		if(list!=null&&list.size()>0){
			for (PCommentstemp pp : list) {
				pp.setTips(tipsMapper.findListByTempId(pp.getTipclassid()));
			}
		}
		return list;
	}
	
	public ReturnModel  addPinglun(Long userId, PMyproductcomments param){
		ReturnModel rqModel=new ReturnModel();
		if(param==null||ObjectUtil.isEmpty(param.getCartid())||ObjectUtil.isEmpty(param.getContent())){
			rqModel.setStatu(ReturnStatus.ParamError);
			rqModel.setStatusreson("参数不全");
			return rqModel;
		}
		UUsers user=usersMapper.selectByPrimaryKey(userId);
		if(user!=null){
			param.setHeadimg(user.getUserimg());
			param.setNickname(user.getNickname());
		}else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("用户不存在");
			return rqModel;
		}
		param.setUserid(userId);
		param.setCreatetime(new Date());
		myproductcommentsMapper.insertSelective(param);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("成功！"); 
		return rqModel;
	}
	
	public ReturnModel findCommentsList(Long userid, Long cartId,int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<PMyproductcomments> list=myproductcommentsMapper.findCommentlist(cartId);
		PageInfo<PMyproductcomments> pageInfo=new PageInfo<PMyproductcomments>(list);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(pageInfo);
		return rq;
	}
	
	public ReturnModel modify_Comments(Long userId,PCommentstemp param){
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if(param==null){
			rq.setStatusreson("参数不能为空");
			return rq;
		}
		if(param.getTipclassid()!=null&&param.getTipclassid()>0){
			PCommentstemp tem=commentstempMapper.selectByPrimaryKey(param.getTipclassid());
			//已有的评论提示分类
			if(tem!=null){ 
				//修改提示分类名称
				if(!ObjectUtil.isEmpty(param.getTipclassname())){
					commentstempMapper.updateByPrimaryKeySelective(param);
				}
				//评论提示（修改、新增）
				if(param.getTips()!=null&&param.getTips().size()>0){
					for (PCommentstips tip : param.getTips()) {
						//修改评论提示
						if(tip.getTipid()!=null&&tip.getTipid()>0&&!ObjectUtil.isEmpty(tip.getContent())){
							 tipsMapper.updateByPrimaryKeySelective(tip);
						}else {
							//新增提示评论
							if(!ObjectUtil.isEmpty(tip.getContent())){
								tip.setTipclassid(param.getTipclassid()); 
								tip.setCreatetime(new Date());
								tipsMapper.insertSelective(tip);
							}
						}
					}
				}
			}
		}else {//新增评论分类
			if(param.getProductid()==null||param.getProductid()<=0){
				rq.setStatusreson("产品id不能为空");
				return rq;
			}
			if(ObjectUtil.isEmpty(param.getTipclassname())){
				rq.setStatusreson("提示分类名称不能为空");
				return rq;
			}
			param.setCreatetime(new Date());
			commentstempMapper.insertSelective(param);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("操作成功！");
		return rq;
	}
	
	public ReturnModel delTip(Long userId,Integer tipId){
		ReturnModel rq=new ReturnModel();
		int count= tipsMapper.deleteByPrimaryKey(tipId);
		if(count>0){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("提示评论删除成功");
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("删除失败"); 
		}
		return rq;
	}
	
	public ReturnModel delCommentClass(Long userId,Integer commentClassId){
		ReturnModel rq=new ReturnModel();
		tipsMapper.deleteByClassID(commentClassId);
		int count= commentstempMapper.deleteByPrimaryKey(commentClassId);
		if(count>0){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("评论分类删除成功");
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("删除失败"); 
		}
		return rq;
	}
	
}
