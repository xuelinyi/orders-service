package com.orders.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.orders.bean.DemoBean;
import com.orders.bean.OrderInfoBean;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<OrderInfoBean> {

}
