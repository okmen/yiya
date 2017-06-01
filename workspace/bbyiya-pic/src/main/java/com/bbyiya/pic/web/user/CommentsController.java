package com.bbyiya.pic.web.user;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.PCommentstemp;
import com.bbyiya.model.PMyproductcomments;
import com.bbyiya.pic.service.IPic_CommentService;
import com.bbyiya.pic.utils.Json2Objects;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

/**
 * 作品评论
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/myproduct/comment")
public class CommentsController extends SSOController{

	@Resource(name = "pic_commentService")
	private IPic_CommentService commentService;
	
	/**
	 *
	 * M07 作品评论-评论提示
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/findTemplist")
	public String getAccountInfo(long productId) throws Exception {
		ReturnModel rq = new ReturnModel();
		List<PCommentstemp> list = commentService.findCommentResultList(productId);
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(list);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * M09 作品评论-新增评论
	 * 
	 * @param commentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addComments")
	public String addComments(String commentJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			commentJson=ObjectUtil.filterEmoji(commentJson);
			PMyproductcomments param = Json2Objects.getParam_PMyproductcomments(commentJson);
			if (param != null) {
				rq = commentService.addPinglun(user.getUserId(), param);
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数不全");
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * M08 作品评论-用户评论列表
	 * @param cartId
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/commentlist")
	public String commentlist(long cartId,@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "20")int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq=commentService.findCommentsList(user.getUserId(), cartId, index, size);
		}else {
			rq=commentService.findCommentsList(null, cartId, index, size);
		}
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	} 
	
	/**
	 * M10 作品评论-评论者头像列表
	 * @param cartId
	 * @param index
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/commentHeadImglist")
	public String commentHeadImglist(long cartId,@RequestParam(required = false, defaultValue = "1")int index,@RequestParam(required = false, defaultValue = "10")int size) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq=commentService.findCommentsHeadImgList(cartId, index, size);
		
		rq.setStatu(ReturnStatus.Success);
		return JsonUtil.objectToJsonStr(rq);
	} 
	
	/**
	 * 新增评论模板
	 * @param commentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/modifyCommentTemp")
	public String addCommentTemp(String commentJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			PCommentstemp param=Json2Objects.getParam_PCommentstemp(commentJson);
			rq=commentService.modify_Comments(user.getUserId(), param);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delTip")
	public String delTip(int tipid) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq=commentService.delTip(user.getUserId(), tipid);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/delTipClass")
	public String delTipClass(int classId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			rq=commentService.delCommentClass(user.getUserId(), classId);
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
}
