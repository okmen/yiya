package com.bbyiya.cts.web;

import javax.annotation.Resource;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.cts.service.ICtsMusicService;
import com.bbyiya.cts.vo.MusicAddParam;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.SMusics;
import com.bbyiya.model.UAdmin;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.vo.ReturnModel;

public class MusicMgtController extends CtsSSOController {
	@Resource(name = "ctsMusicService")
	private ICtsMusicService musicService;

	/**
	 * ÀÖ¿âÌí¼Ó¡¢ÐÞ¸Ä ÒôÀÖ
	 * 
	 * @param model
	 * @param musicJson
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/musicAdd")
	public String musicAdd(Model model, String musicJson) throws Exception {
		ReturnModel rqModel = new ReturnModel();
		UAdmin user = this.getLoginUser();
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
			rqModel.setStatusreson("µÇÂ¼¹ýÆÚ");
		}
		return JsonUtil.objectToJsonStr(rqModel);
	}
}
