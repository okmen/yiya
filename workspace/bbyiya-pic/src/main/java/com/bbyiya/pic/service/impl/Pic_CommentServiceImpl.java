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
			rqModel.setStatusreson("������ȫ");
			return rqModel;
		}
		UUsers user=usersMapper.selectByPrimaryKey(userId);
		if(user!=null){
			param.setHeadimg(user.getUserimg());
			param.setNickname(user.getNickname());
		}else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("�û�������");
			return rqModel;
		}
		param.setUserid(userId);
		param.setCreatetime(new Date());
		myproductcommentsMapper.insertSelective(param);
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("�ɹ���"); 
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
			rq.setStatusreson("��������Ϊ��");
			return rq;
		}
		if(param.getTipclassid()!=null&&param.getTipclassid()>0){
			PCommentstemp tem=commentstempMapper.selectByPrimaryKey(param.getTipclassid());
			//���е�������ʾ����
			if(tem!=null){ 
				//�޸���ʾ��������
				if(!ObjectUtil.isEmpty(param.getTipclassname())){
					commentstempMapper.updateByPrimaryKeySelective(param);
				}
				//������ʾ���޸ġ�������
				if(param.getTips()!=null&&param.getTips().size()>0){
					for (PCommentstips tip : param.getTips()) {
						//�޸�������ʾ
						if(tip.getTipid()!=null&&tip.getTipid()>0&&!ObjectUtil.isEmpty(tip.getContent())){
							 tipsMapper.updateByPrimaryKeySelective(tip);
						}else {
							//������ʾ����
							if(!ObjectUtil.isEmpty(tip.getContent())){
								tip.setTipclassid(param.getTipclassid()); 
								tip.setCreatetime(new Date());
								tipsMapper.insertSelective(tip);
							}
						}
					}
				}
			}
		}else {//�������۷���
			if(param.getProductid()==null||param.getProductid()<=0){
				rq.setStatusreson("��Ʒid����Ϊ��");
				return rq;
			}
			if(ObjectUtil.isEmpty(param.getTipclassname())){
				rq.setStatusreson("��ʾ�������Ʋ���Ϊ��");
				return rq;
			}
			param.setCreatetime(new Date());
			commentstempMapper.insertSelective(param);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setStatusreson("�����ɹ���");
		return rq;
	}
	
	public ReturnModel delTip(Long userId,Integer tipId){
		ReturnModel rq=new ReturnModel();
		int count= tipsMapper.deleteByPrimaryKey(tipId);
		if(count>0){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("��ʾ����ɾ���ɹ�");
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("ɾ��ʧ��"); 
		}
		return rq;
	}
	
	public ReturnModel delCommentClass(Long userId,Integer commentClassId){
		ReturnModel rq=new ReturnModel();
		tipsMapper.deleteByClassID(commentClassId);
		int count= commentstempMapper.deleteByPrimaryKey(commentClassId);
		if(count>0){
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("���۷���ɾ���ɹ�");
		}else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("ɾ��ʧ��"); 
		}
		return rq;
	}
	
}
