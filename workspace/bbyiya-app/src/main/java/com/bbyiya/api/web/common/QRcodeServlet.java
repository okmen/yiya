package com.bbyiya.api.web.common;

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

import com.bbyiya.utils.QRCodeUtil;

/**
 * ���ɶ�ά��
 * @author Administrator
 *
 */
@WebServlet("/common/getQRcode")
public class QRcodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * ���ɶ�ά��
	 */
	public QRcodeServlet() {
		super();
	}

	// ���ٷ���
	public void destroy() {
		super.destroy();
	}

	// ��ӦGet����
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//�������ά��
		String url=request.getParameter("urlstr");
		String content="http://www.baidu.com";
		encoderQRCode(content, 0, response);
	}

	// ��ӦPost����
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * ���ɶ�ά��
	 * @param content
	 * @param size
	 * @param response
	 */
	public void encoderQRCode(String content, int size,
			HttpServletResponse response) {
		try {
			if (size <= 0)
				size = 8;
			BufferedImage bufImg = QRCodeUtil.createImage(content, "", true); 
			// ���ɶ�ά��QRCodeͼƬ
			ImageIO.write(bufImg, "jpg", response.getOutputStream());
		} catch (Exception e) {

		}
	}
	
	
	/**
	 * ��ȡ������url
	 * @param request
	 * @return
	 */
	private String getUrl(HttpServletRequest request){
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
