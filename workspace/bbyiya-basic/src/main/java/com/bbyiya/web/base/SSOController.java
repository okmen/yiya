package com.bbyiya.web.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.bbyiya.model.EErrors;
import com.bbyiya.service.IUserInfoMgtService;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.RedisUtil;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.baseUtils.CookieUtils;
import com.bbyiya.dao.EErrorsMapper;

/**
 * 用户登陆验证（farther class）
 * 
 * @author Administrator
 *
 */
public class SSOController {
	/**
	 * 管理员登录 cookie缓存名
	 */
	protected static String PHOTO_TOKEN = "pic12_token_user";
	/**
	 * 用户登陆成功后返回类
	 */
	private LoginSuccessResult user;

	@Autowired
	public HttpServletRequest request;
	@Autowired
	public HttpServletResponse response;
	
	/**
	 * 用户信息处理
	 */
	@Resource(name = "userInfoMgtService")
	public IUserInfoMgtService userInfoMgtService;

	@Autowired
	private EErrorsMapper logMapper;
	

	/**
	 * 用户
	 */
	public LoginSuccessResult getLoginUser() {
		String ticket = getTicket();
		if (ObjectUtil.isEmpty(ticket)) {
			return null;
		}
		Object userObject = RedisUtil.getObject(ticket);
		if (userObject != null)// 如果存在
		{
			user = (LoginSuccessResult) userObject;
			return user;
		}
		return null;// 用户过期
	}

	/**
	 * 退出登录
	 * 
	 * @return
	 */
	public boolean loginOut() {
		String ticket = getTicket();
		if (ObjectUtil.isEmpty(ticket)) {
			return true;
		}
		Object userObject = RedisUtil.getObject(ticket);
		if (userObject != null)// 如果存在
		{
			RedisUtil.delete(ticket);
		}
		return true;
	}

	/**
	 * web、wap 浏览器端获取用户信息
	 * 
	 * @return
	 */
	public LoginSuccessResult getWebLoginUser() {
		String ticket = CookieUtils.getCookie_web(request);
		if (ObjectUtil.isEmpty(ticket))
			return null;
		Object userObject = RedisUtil.getObject(ticket);
		if (userObject != null)// 如果存在
		{
			user = (LoginSuccessResult) userObject;
			RedisUtil.setExpire(ticket, 86400);// 延长时间
			return user;
		}
		return null;// 用户过期
	}


	/**
	 * 获取用户登录 Ticket
	 * 
	 * @return
	 */
	public String getTicket() {
		String ticket = request.getHeader("ticket");
		if (ObjectUtil.isEmpty(ticket)) {
			ticket = request.getParameter("ticket");
			if(ObjectUtil.isEmpty(ticket)){
				// 获取cookie中的ticket
				ticket = CookieUtils.getCookie_web(request);
			}
		}
		return ticket;
	}
	

	
	/**
	 * 更新 用户登录信息
	 * @param userId
	 */
	public  void updateLoginUser(Long userId){
		String ticket = getTicket();
		if (ObjectUtil.isEmpty(ticket)) {
			ticket = CookieUtils.getCookie_web(request);
		}
		if (!ObjectUtil.isEmpty(ticket)) {
			LoginSuccessResult loginUser = userInfoMgtService.getLoginSuccessResult(userId);
			if (loginUser != null) {
				RedisUtil.setObject(ticket, loginUser, 86400);
			}
		}
	}

	/**
	 * 插入错误信息
	 * @param msg
	 * @param className
	 */
	public void addErrorLog(String msg,String className) {
		EErrors errors = new EErrors();
		errors.setClassname(this.getClass().getName());
		errors.setMsg(msg);
		errors.setCreatetime(new Date());
		logMapper.insert(errors);
	}
	/**
	 * 获取用户ip
	 * @return
	 */
	public String getIpStr() {
		String ipAddres = request.getHeader("x-forwarded-for");
		if (ObjectUtil.isEmpty(ipAddres)) {
			ipAddres = request.getHeader("Proxy-Client-IP");
			if (ObjectUtil.isEmpty(ipAddres)) {
				ipAddres = request.getHeader("WL-Proxy-Client-IP");
				if (ObjectUtil.isEmpty(ipAddres)) {
					ipAddres = request.getRemoteAddr();
				}
			}
		}
		return ipAddres;
	}
	
	
	/**
	 * 接受xml参数 
	 * @param request
	 * @return
	 */
	public static String readReqStr(HttpServletRequest request) {
		BufferedReader reader = null;
		StringBuilder sb = new StringBuilder();
		try {
			reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != reader) {
					reader.close();
				}
			} catch (IOException e) {

			}
		}
		return sb.toString();
	}
}
