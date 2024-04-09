package com.gs.commons.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gs.commons.entity.PayOrder;
import com.gs.commons.service.PayOrderService;
import com.gs.commons.mapper.PayOrderMapper;
import org.springframework.stereotype.Service;

/**
* @author 69000
* @description 针对表【t_pay_order】的数据库操作Service实现
* @createDate 2024-04-09 14:34:08
*/
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder>
    implements PayOrderService{

}




