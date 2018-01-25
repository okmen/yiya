package com.bbyiya.pic.web.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bbyiya.dao.OOrderproductsMapper;
import com.bbyiya.dao.OUserordersMapper;
import com.bbyiya.dao.TiActivitysMapper;
import com.bbyiya.dao.TiActivityworksMapper;
import com.bbyiya.dao.TiMyworksMapper;
import com.bbyiya.enums.OrderStatusEnum;
import com.bbyiya.enums.OrderTypeEnum;
import com.bbyiya.enums.ReturnStatus;
import com.bbyiya.model.OOrderproducts;
import com.bbyiya.model.OUserorders;
import com.bbyiya.model.TiActivityworks;
import com.bbyiya.utils.JsonUtil;
import com.bbyiya.vo.ReturnModel;
import com.bbyiya.vo.user.LoginSuccessResult;
import com.bbyiya.web.base.SSOController;

/**
 * 订单退款取消
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/orderMgt")
public class OrderFalseController extends SSOController {
	@Autowired
	private OUserordersMapper orderMapper;
	@Autowired
	private OOrderproductsMapper oproductMapper;
	@Autowired
	private TiActivitysMapper actMapper;
	@Autowired
	private TiActivityworksMapper actworkMapper;
	@Autowired
	private TiMyworksMapper workMapper;

	/**
	 * 订单取消操作
	 * 
	 * @param workId
	 *            作品id
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/orderRetrunBack")
	public String getHome(String userOrderId) throws Exception {
		ReturnModel rq = new ReturnModel();
		LoginSuccessResult user = super.getLoginUser();
		if (user != null) {
			OUserorders userorders = orderMapper.selectByPrimaryKey(userOrderId);
			if (userorders != null) {
				if (userorders.getStatus() != null && userorders.getStatus() == 2) {
					if (userorders.getOrdertype() != null) {
						//台历普通购买
						if (userorders.getOrdertype()==Integer.parseInt(OrderTypeEnum.ti_nomal.toString())) {
							userorders.setStatus(Integer.parseInt(OrderStatusEnum.payed.toString()));
							orderMapper.updateByPrimaryKeySelective(userorders);
							rq.setStatu(ReturnStatus.Success);
							rq.setStatusreson("订单置为已支付的状态！普通购买的台历挂历订单");
						}
						//台历  广告主下单
						else if (userorders.getOrdertype()==Integer.parseInt(OrderTypeEnum.ti_branchOrder.toString())) {
							OOrderproducts oproduct= oproductMapper.selectByPrimaryKey(userOrderId);
							if(oproduct!=null&&oproduct.getCartid()!=null){
								//
								TiActivityworks actwork= actworkMapper.selectByPrimaryKey(oproduct.getCartid());
								if(actwork!=null){
									//TODO 如果用户付了运费怎么处理
									
								}
							}
						}
					}
				}
			}
		} else {
			rq.setStatu(ReturnStatus.LoginError);
			rq.setStatusreson("登录过期");
		}
		return JsonUtil.objectToJsonStr(rq);
	}

}
