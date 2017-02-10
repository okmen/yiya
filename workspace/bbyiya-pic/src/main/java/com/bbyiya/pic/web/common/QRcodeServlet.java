package com.bbyiya.pic.web.common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bbyiya.utils.ConfigUtil;
import com.bbyiya.utils.ObjectUtil;
import com.bbyiya.utils.QRCodeUtil;

/**
 * 生成二维码
 * 
 * @author Administrator
 *
 */
@WebServlet("/common/generateQRcode")
public class QRcodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * 生成二维码
	 */
	public QRcodeServlet() {
		super();
	}

	// 销毁方法
	public void destroy() {
		super.destroy();
	}

	// 响应Get请求
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 规则定义二维码
		String content = "";
		int type = ObjectUtil.parseInt(request.getParameter("t"));
		switch (type) {
		case 1:// 获取
			content = ConfigUtil.getSingleValue("yiya_pic_url");
			break;
		default:
			String url = request.getParameter("urlstr");
			if (!ObjectUtil.isEmpty(url)) {
				content = url;
			}
			break;
		}
		encoderQRCode(content, 0, response);
	}

	// 响应Post请求
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * 生成二维码
	 * 
	 * @param content
	 * @param size
	 * @param response
	 */
	public void encoderQRCode(String content, int size, HttpServletResponse response) {
		try {
			if (size <= 0)
				size = 8;
			BufferedImage bufImg = QRCodeUtil.createImage(content, "", true);
			// 生成二维码QRCode图片
			ImageIO.write(bufImg, "jpg", response.getOutputStream());
		} catch (Exception e) {

		}
	}

	/**
	 * 获取完整的url
	 * 
	 * @param request
	 * @return
	 */
	private String getUrl(HttpServletRequest request) {
		String urlString = request.getParameter("url");
		Map<String, String[]> map = request.getParameterMap();
		Set keSet = map.entrySet();
		for (Iterator itr = keSet.iterator(); itr.hasNext();) {
			Map.Entry me = (Map.Entry) itr.next();
			Object ok = me.getKey();
			Object ov = me.getValue();
			String[] value = new String[1];
			if (ov instanceof String[]) {
				value = (String[]) ov;
			} else {
				value[0] = ov.toString();
			}

			for (int k = 0; k < value.length; k++) {
				if (!ok.equals("url")) {
					urlString += "&" + ok + "=" + value[k];
				}
			}
		}
		return urlString;
	}
}
