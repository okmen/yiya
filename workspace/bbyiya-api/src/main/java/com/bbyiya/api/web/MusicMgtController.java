package com.bbyiya.api.web;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.SMusicttype;
import com.bbyiya.service.IMusicStoreService;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.music.MusicResult;
import com.bbyiya.web.base.SSOController;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping(value = "/music")
public class MusicMgtController extends SSOController {

	@Resource(name="musicStoreService")
	private IMusicStoreService musicService;
	
	/**
	 * S 01 乐库-音乐列表
	 * 获取乐库音乐列表
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/list")
	public String list(@RequestParam(required = false, defaultValue = "0")int typeId ,@RequestParam(required = false, defaultValue = "1") int index,@RequestParam(required = false, defaultValue = "10") int size) throws Exception {
		PageInfo<MusicResult> result= musicService.find_MusicResult(typeId, index, size);
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(result);
		return JsonUtil.objectToJsonStr(rq);
	}
	
	@ResponseBody
	@RequestMapping(value = "/musictypelist")
	public String musictypelist() throws Exception {
		List<SMusicttype> result= musicService.fint_SMusicttypelist();
		ReturnModel rq=new ReturnModel();
		rq.setStatu(ReturnStatus.Success);
		rq.setBasemodle(result);
		return JsonUtil.objectToJsonStr(rq);
	}
}
