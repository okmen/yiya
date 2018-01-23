package com.bbyiya.cts.web;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.cts.service.ICtsMusicService;
import com.bbyiya.cts.vo.MusicAddParam;
import com.bbyiya.cts.vo.admin.AdminLoginSuccessResult;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.SMusics;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;

/**
 * 乐库管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/music")
public class MusicMgtController extends CtsSSOController {
	@Resource(name = "ctsMusicService")
	private ICtsMusicService musicService;

	/**
	 * Music 01 乐库-新增/修改音乐
	 * 
	 * @param model
	 * @param musicJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/addOrEdit_music")
	public String musicAdd(Model model, String musicJson) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		AdminLoginSuccessResult user = this.getLoginUser();
		if (user != null) {
			if (!ObjectUtil.isEmpty(musicJson)) {
				MusicAddParam param = (MusicAddParam) JsonUtil.jsonStrToObject(musicJson, MusicAddParam.class);
				if (param != null) {
					SMusics musicsEntity = new SMusics();
					musicsEntity.setDownloadurl(param.getDownloadurl());
					musicsEntity.setName(param.getName());
					musicsEntity.setMusictype(param.getMusictype());
					rqModel = musicService.addOrEdit_SMusics(musicsEntity);
				}
			}
		} else {
			rqModel.setStatu(ReturnStatus.LoginError);
			rqModel.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
}
