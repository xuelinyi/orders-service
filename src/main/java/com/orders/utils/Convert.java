package com.orders.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orders.bean.DemoBean;
import com.orders.entity.request.DemoReq;
import com.orders.entity.response.DemoVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class Convert {

    public static DemoBean ConvertDemoBean(DemoReq demoReq) {
        DemoBean demoBean = null;
        if(null != demoReq) {
            demoBean = new DemoBean();
            BeanUtils.copyProperties(demoReq, demoBean);
        }
        return demoBean;
    }

    public static IPage<DemoVo> ConvertDemoVo(IPage<DemoBean> page) {
        IPage<DemoVo> pageVo = null;
        if(null != page) {
            pageVo = new Page<>();
            BeanUtils.copyProperties(page, pageVo);
            pageVo.setRecords(ConvertDemoVo(page.getRecords()));
        }
        return pageVo;
    }


    public static List<DemoVo> ConvertDemoVo(List<DemoBean> beanList) {
        List<DemoVo> voList = null;
        if(null != beanList && beanList.size() > 0) {
            voList = new ArrayList<>();
            for(DemoBean demoBean : beanList) {
                voList.add(ConvertDemoVo(demoBean));
            }
        }
        return voList;
    }


    public static DemoVo ConvertDemoVo(DemoBean bean) {
        DemoVo vo = null;
        if(null != bean) {
            vo = new DemoVo();
            BeanUtils.copyProperties(bean, vo);
        }
        return vo;
    }



}
