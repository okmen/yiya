package com.bbyiya.pic.web.user;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.PMyproductactivitycodeMapper;
import com.bbyiya.dao.PMyproducttempMapper;
import com.bbyiya.dao.PMyproducttempapplyMapper;
import com.bbyiya.dao.PProductstylesMapper;
import com.bbyiya.enums.MyProductTempType;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.enums.pic.ActivityCodeStatusEnum;
import com.bbyiya.enums.pic.MyProducttempApplyStatusEnum;
import com.bbyiya.model.PMyproductactivitycode;
import com.bbyiya.model.PMyproducttemp;
import com.bbyiya.model.PMyproducttempapply;
import com.bbyiya.model.PProductstyles;
import com.bbyiya.pic.service.ibs.IIbs_MyProductTempService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

@Controller
@RequestMapping(value = "/myProduct/yiye")
public class YiyeCodeController extends SSOController {
	@Autowired
	private PMyproductactivitycodeMapper codeMapper;
	@Autowired
	private PMyproducttempMapper tempMapper;

	@Autowired
	private PProductstylesMapper styleMapper;
	/**
	 * 活动码验证
	 * 
	 * @param commentJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/codecheck")
	public String access(String codenum,int tempId) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			if (!ObjectUtil.isEmpty(codenum)) {
				PMyproductactivitycode codeMod = codeMapper.selectByPrimaryKey(codenum);
				if (codeMod != null) {
					if (codeMod.getStatus() != null && codeMod.getStatus().intValue() == Integer.parseInt(ActivityCodeStatusEnum.used.toString())) {
						rq.setStatu(ReturnStatus.ParamError);
						rq.setStatusreson("不好意思，您的活动码已经使用过！");
						return JsonUtil.objectToJsonStr(rq);
					} else if (codeMod.getStatus() == null || codeMod.getStatus().intValue() == Integer.parseInt(ActivityCodeStatusEnum.notuse.toString())) {
						PMyproducttemp temp= tempMapper.selectByPrimaryKey(codeMod.getTempid());
						if(temp!=null&&temp.getTempid().intValue()==tempId){
							rq.setStatu(ReturnStatus.Success);
							Map<String, Object> map=new HashMap<String, Object>();
							if(temp.getStyleid()!=null){
								PProductstyles style= styleMapper.selectByPrimaryKey(temp.getStyleid());
								if(style!=null){
									map.put("price", style.getPrice());
									map.put("property", style.getPropertystr());
									rq.setBasemodle(map); 
								}
							}
							
						}else {
							rq.setStatu(ReturnStatus.ParamError);
							rq.setStatusreson("不好意思，您的活动码不支持在本活动使用！（PS:活动码只能在指定活动中使用！）");
						}
					} else {
						rq.setStatusreson("不好意思，您的活动码失效！");
						return JsonUtil.objectToJsonStr(rq);
					}
				}else {
					rq.setStatusreson("很遗憾，你输入的活动码不正确，未能获得活动资格！");
				}
			} else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("请输入活动码！");
			}
		}
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@Resource(name = "ibs_MyProductTempService")
	private IIbs_MyProductTempService ibs_tempService;
	@Autowired
	private PMyproducttempapplyMapper tempApplyMapper;
	
	/**
	 * 活动码兑换
	 * @param codeNum
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/jdAct")
	public String jdAct(String codeNum) throws Exception {
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		LoginSuccessResult user = super.getLoginUser();
		if(user!=null&&user.getUserId()!=null){
			rq=checkCodeNum(user.getUserId(), codeNum);
			if(ReturnStatus.Success.equals(rq.getStatu())&&rq.getBasemodle()!=null){
				PMyproductactivitycode codeMod=(PMyproductactivitycode)rq.getBasemodle();
				if(codeMod!=null){
					PMyproducttemp temp= tempMapper.selectByPrimaryKey(codeMod.getTempid());
					if(temp!=null){
						//提交申请
						PMyproducttempapply apply=new PMyproducttempapply();
						apply.setTempid(codeMod.getTempid());
						apply.setUserid(user.getUserId());
						if(!ObjectUtil.isEmpty(user.getMobilePhone())){
							apply.setMobilephone(user.getMobilePhone()); 
						}
						apply.setCreatetime(new Date());
						apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.apply.toString()));
						
						//异业模板 申请人数+1
						temp.setApplycount(temp.getApplycount()==null?1:temp.getApplycount()+1);
						tempMapper.updateByPrimaryKeySelective(temp);
						
						//免审核，直接通过审核分配作品id
						rq=ibs_tempService.doAcceptOrAutoTempApplyOpt(apply);
						if(rq.getStatu().equals(ReturnStatus.Success)){
							//不需要审核，直接通过审核
							apply.setStatus(Integer.parseInt(MyProducttempApplyStatusEnum.ok.toString()));
							apply.setVerfiytime(new Date());
							//------------------------  活动码兑换-***********************************--------------------
							if(temp.getType()!=null&&temp.getType().intValue()==Integer.parseInt(MyProductTempType.code.toString())){
								codeMod.setUsedtime(new Date());
								codeMod.setUserid(user.getUserId());
								codeMod.setStatus(1);
								try {
									Map<String, Object> mapr= (Map<String, Object>)rq.getBasemodle();
									if(mapr!=null){
										codeMod.setCartid((Long)mapr.get("mycartid"));
									}
								} catch (Exception e) {
									
								}
								codeMapper.updateByPrimaryKey(codeMod);
								//插入申请提交信息
								tempApplyMapper.insert(apply);
								
								rq.setStatu(ReturnStatus.Success);
								rq.setStatusreson("提交申请成功！");
							}
						}
					}
					
				}
			}
		}else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

	/**
	 * 活动码验证
	 * @param codenum
	 * @return
	 */
	public ReturnModel checkCodeNum(Long userId, String codenum){
		ReturnModel rq = new ReturnModel();
		rq.setStatu(ReturnStatus.ParamError);
		if (!ObjectUtil.isEmpty(codenum)) {
			PMyproductactivitycode codeMod = codeMapper.selectByPrimaryKey(codenum);
			if (codeMod != null) {
				if (codeMod.getStatus() != null && codeMod.getStatus().intValue() == Integer.parseInt(ActivityCodeStatusEnum.used.toString())) {
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("不好意思，您的活动码已经使用过！");
					return rq;
				}else if (codeMod.getStatus() != null && codeMod.getStatus().intValue() == Integer.parseInt(ActivityCodeStatusEnum.notuse.toString())) {
					rq.setBasemodle(codeMod);
					rq.setStatu(ReturnStatus.Success);
					return rq;
				}
				else {
					rq.setStatusreson("不好意思，您的活动码失效！");
					return rq;
				}
			}else {
				rq.setStatusreson("很遗憾，你输入的活动码不正确，未能获得活动资格！");
				return rq;
			}
		} else {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("请输入活动码！");
		}
		return rq;
	}
}
