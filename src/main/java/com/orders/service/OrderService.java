package com.orders.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.orders.bean.DemoBean;
import com.orders.bean.OrderInfoBean;

public interface OrderService extends IService<OrderInfoBean> {

    /**
     * 增加订单
     * @return
     * @throws Exception
     */
    public Boolean addOrderAndDispatch() throws Exception;

}
