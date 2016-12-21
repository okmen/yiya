package com.bbyiya.service;

import java.util.List;

import com.bbyiya.vo.reads.DailyReadResult;
import com.bbyiya.vo.user.LoginSuccessResult;

public interface IReadsMgtService {
	List<DailyReadResult> find_DailyReadResultlist(LoginSuccessResult user);
}
