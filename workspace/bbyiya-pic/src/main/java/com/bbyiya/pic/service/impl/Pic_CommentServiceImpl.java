package com.bbyiya.pic.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bbyiya.dao.PCommentstempMapper;
import com.bbyiya.dao.PCommentstipsMapper;
import com.bbyiya.dao.PMyproductcommentsMapper;
import com.bbyiya.dao.PMyproductextMapper;
import com.bbyiya.dao.UUsersMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PCommentstemp;
import com.bbyiya.model.PCommentstips;
import com.bbyiya.model.PMyproductcomments;
import com.bbyiya.model.PMyproductext;
import com.bbyiya.model.UUsers;
import com.bbyiya.pic.service.IPic_CommentService;
import com.bbyiya.utils.ConfigUtil;
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
	@Autowired
	private PMyproductextMapper myproductextMapper;
	
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
			if(!(ObjectUtil.isEmpty(user.getUserimg())||"null".equals(user.getUserimg()))){
				param.setHeadimg(user.getUserimg());
			}else {
				param.setHeadimg(ConfigUtil.getSingleValue("default-headimg"));
			}
			param.setNickname(user.getNickname());
		}else {
			rqModel.setStatu(ReturnStatus.SystemError);
			rqModel.setStatusreson("�û�������");
			return rqModel;
		}
		int count=myproductcommentsMapper.countCommentByUserId(param.getCartid(),userId);
		param.setUserid(userId);
		param.setCreatetime(new Date());
		myproductcommentsMapper.insertSelective(param);
		PMyproductext ext=myproductextMapper.selectByPrimaryKey(param.getCartid());
		if(ext==null){
			 ext=new PMyproductext();
			 ext.setCartid(param.getCartid());
			 ext.setCommentscount(count+1);
			 myproductextMapper.insert(ext);
		}else {
			ext.setCommentscount(count+1);
			myproductextMapper.updateByPrimaryKeySelective(ext);
		}
		
	
		rqModel.setStatu(ReturnStatus.Success);
		rqModel.setStatusreson("�ɹ���"); 
		return rqModel;
	}
	
	public ReturnModel findCommentsList(Long userid, Long cartId,int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<PMyproductcomments> list=myproductcommentsMapper.findCommentlist(cartId);
//		if(list==null||list.size()<=0){
//			list.add(defaulModel());
//		}
		PageInfo<PMyproductcomments> pageInfo=new PageInfo<PMyproductcomments>(list);
		if(pageInfo.getList()==null||pageInfo.getSize()<=0){
			pageInfo.getList().add(defaulModel());
			pageInfo.setTotal(1);
		}
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(pageInfo);
		return rq;
	}
	
	private PMyproductcomments defaulModel(){
		PMyproductcomments cmo=new PMyproductcomments();
		cmo.setCreatetime(new Date());
		cmo.setNickname("��ѽʮ��");
		cmo.setHeadimg("http://pic.bbyiya.com/Fv1TDhI0CVm-XDOk71o2LbrKcQwa"); 
		cmo.setContent(ConfigUtil.getSingleValue("default-comments"));  
		return cmo;
	}
	
	/**
	 * ��ȡ�����б�������ͷ���б�
	 * @param cartId
	 * @param index
	 * @param size
	 * @return
	 */
	public ReturnModel findCommentsHeadImgList(Long cartId,int index,int size){
		ReturnModel rq=new ReturnModel();
		PageHelper.startPage(index, size);
		List<PMyproductcomments> list=myproductcommentsMapper.findCommentHeadImglist(cartId);
		PageInfo<PMyproductcomments> pageInfo=new PageInfo<PMyproductcomments>(list);
		if(pageInfo==null||list.size()<=0){
			pageInfo.getList().add(defaulModel());
			pageInfo.setTotal(1);
		}
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
