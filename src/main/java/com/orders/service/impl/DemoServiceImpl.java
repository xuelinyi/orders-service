package com.orders.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orders.bean.DemoBean;
import com.orders.mapper.DemoMapper;
import com.orders.service.DemoService;
import org.springframework.stereotype.Service;

@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper, DemoBean> implements DemoService {
}
