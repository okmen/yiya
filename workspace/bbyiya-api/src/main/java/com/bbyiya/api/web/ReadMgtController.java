package com.bbyiya.api.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.SReadstypes;
import com.bbyiya.service.IReadsMgtService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.reads.ReadsResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/read")
public class ReadMgtController extends SSOController {

	@Resource(name="readsMgtService")
	private IReadsMgtService readService;
	
	/**
	 * S 03 读物库-读物列表
	 * 获取乐库音乐列表
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/list")
	public String list(@RequestParam(required = false, defaultValue = "0")int typeId ,@RequestParam(required = false, defaultValue = "1") int index,@RequestParam(required = false, defaultValue = "10") int size) throws Exception {
		PageInfo<ReadsResult> result= readService.find_SReadsPageInfo(typeId, index, size);
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(result);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	/**
	 * S 04 读物分类列表
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/readtypelist")
	public String readtypelist() throws Exception {
		List<SReadstypes> result= readService.find_SReadstypeslist();
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(result);
		return JsonUtil.objectToJsonStr(rq);
	}
}
