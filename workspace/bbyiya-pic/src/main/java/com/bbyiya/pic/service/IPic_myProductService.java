package com.bbyiya.pic.service;

import com.bbyiya.vo.ReturnModel;

public interface IPic_myProductService {

	ReturnModel sendInvite(Long userId, String phone,Long cartId);
}
