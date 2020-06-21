package com.orders.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orders.bean.DemoBean;
import com.orders.entity.request.DemoReq;
import com.orders.entity.response.DemoVo;
import com.orders.entity.response.ResponseCodeEnum;
import com.orders.entity.response.ResponseResult;
import com.orders.exception.BizException;
import com.orders.service.DemoService;
import com.orders.service.OrderService;
import com.orders.utils.Convert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
@RequestMapping(value = "/order")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/add")
    public ResponseResult<Boolean> addDemo() {
        ResponseResult<Boolean> result = new ResponseResult();
        try {
            return result.success(orderService.addOrderAndDispatch());
        }catch (BizException e) {
            return result.error(e.getExceptionCode(), e.getMessage());
        }
        catch (Exception e) {
            logger.error("下单失败"+e);
        }
        return result.error();
    }


}
