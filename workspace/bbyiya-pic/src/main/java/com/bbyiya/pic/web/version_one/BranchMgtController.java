package com.bbyiya.pic.web.version_one;

import java.util.Date;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.EErrorsMapper;
import com.bbyiya.dao.UBranchinfotempMapper;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.UBranchinfotemp;
import com.bbyiya.pic.web.common.Json2Objects;
//import com.bbyiya.model.UUseraddress;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.web.base.SSOController;


@Controller
@RequestMapping(value = "/ibs/branch")
public class BranchMgtController extends SSOController {
	@Autowired
	private UBranchinfotempMapper tempMapper;
	@Autowired
	private EErrorsMapper logger;
	/**
	 * B01 招商报名
	 * @param companyJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/companySubmit")
	public String companySubmit( String companyJson) throws Exception {
		ReturnModel rq = new ReturnModel();
		try {
			UBranchinfotemp model= Json2Objects.getParam_UBranchinfotemp(companyJson);// (UBranchinfotemp)JsonUtil.jsonStrToObject(companyJson, UBranchinfotemp.class);
			if(model!=null){
				if(ObjectUtil.isEmpty(model.getPhone())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("手机号不能为空");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(model.getContactname())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("联系人不能为空");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(ObjectUtil.isEmpty(model.getCompanyname())){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("公司信息不能为空");
					return JsonUtil.objectToJsonStr(rq);
				}
				if(!(ObjectUtil.validSqlStr(model.getPhone())&&ObjectUtil.validSqlStr(model.getCompanyname())&&ObjectUtil.validSqlStr(model.getContactname()))){
					rq.setStatu(ReturnStatus.ParamError);
					rq.setStatusreson("存在非法字符");
					return JsonUtil.objectToJsonStr(rq);
				}
			}else {
				rq.setStatu(ReturnStatus.ParamError);
				rq.setStatusreson("参数错误");
				return JsonUtil.objectToJsonStr(rq);
			}
			model.setCreatetime(new Date()); 
			tempMapper.insertSelective(model);
			rq.setStatu(ReturnStatus.Success);
			rq.setStatusreson("提交成功！");
		} catch (Exception e) {
			rq.setStatu(ReturnStatus.ParamError);
			rq.setStatusreson("参数错误001");
			return JsonUtil.objectToJsonStr(rq);
		} 
		return JsonUtil.objectToJsonStr(rq);
	}
	

//	/**
//	 * 供应商报名页申请
//	 * @param addrJson
//	 * @return
//	 */
//	private UBranchinfotemp getParams(String addrJson) {
//		try {
//			JSONObject model = JSONObject.fromObject(addrJson);
//			if (model != null) {
//				UBranchinfotemp param = new UBranchinfotemp();
//				String contactname=String.valueOf(model.get("contactname"));
//				if(!ObjectUtil.isEmpty(contactname)&&!"null".equals(contactname)){
//					param.setContactname(contactname);
//				}
//				String companyname=String.valueOf(model.get("companyname"));
//				if(!ObjectUtil.isEmpty(companyname)&&!"null".equals(companyname)){
//					param.setCompanyname(companyname);;
//				}
//				String phone=String.valueOf(model.get("phone"));
//				if(!ObjectUtil.isEmpty(phone)&&!"null".equals(phone)){
//					param.setPhone(phone);
//				}
//				Integer province=ObjectUtil.parseInt(String.valueOf(model.get("province")));
//				if(province!=null&&province>0){
//					param.setProvince(province);
//				}
//				Integer city=ObjectUtil.parseInt(String.valueOf(model.get("city")));
//				if(city!=null&&city>0){
//					param.setCity(city);
//				}
//				return param;
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			logger.addError(this.getClass().getName(), e.getMessage()); 
//		}
//		return null;
//		
//	}
	
}
