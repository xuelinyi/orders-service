package com.orders.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.orders.bean.DemoBean;
import com.orders.entity.request.DemoReq;
import com.orders.entity.response.DemoVo;
import com.orders.entity.response.ResponseCodeEnum;
import com.orders.entity.response.ResponseResult;
import com.orders.service.DemoService;
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
@RequestMapping(value = "/demo")
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Value("${word}")
    private String word;

    @Autowired
    private DemoService demoService;

    @GetMapping(value = "/select")
    public ResponseResult<IPage<DemoVo>> select(
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "pageNum") Integer pageNum,
            @RequestParam(value = "pageSize") Integer pageSize) {

        ResponseResult<IPage<DemoVo>> result = new ResponseResult();
        try {
            System.out.println(word);
            IPage<DemoBean> iPage = new Page<>(pageNum, pageSize);
            QueryWrapper<DemoBean> wrapper = new QueryWrapper<>();
            if(StringUtils.isNotEmpty(phone)) {
                wrapper.eq("phone", phone);
            }
            if(StringUtils.isNotEmpty(startTime)) {
                wrapper.ge("create_time", startTime);
            }
            if(StringUtils.isNotEmpty(endTime)) {
                wrapper.lt("create_time", endTime);
            }
            wrapper.orderByDesc("create_time");
            IPage<DemoBean> page = demoService.selectPage(iPage, wrapper);
            return result.success(Convert.ConvertDemoVo(page));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.error();
    }



    @GetMapping(value = "/{id}")
    public ResponseResult<DemoVo> getById(@PathVariable(value = "id") Integer id) {
        ResponseResult<DemoVo> result = new ResponseResult();
        try {
            DemoBean demoBean = demoService.selectById(id);
            return result.success(Convert.ConvertDemoVo(demoBean));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.error();
    }

    @PostMapping(value = "/add")
    public ResponseResult<Boolean> addDemo(@RequestBody DemoReq demoReq) {
        ResponseResult<Boolean> result = new ResponseResult();
        try {
            QueryWrapper<DemoBean> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", demoReq.getPhone()).or().eq("id_card", demoReq.getIdCard());
            int count = demoService.selectCount(wrapper);
            if(count > 0) {
                return result.error(ResponseCodeEnum.DUPLICATE_PHONE_ID_CARD);
            }
            return result.success(demoService.insert(Convert.ConvertDemoBean(demoReq)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.error();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseResult<Boolean> delteById(@PathVariable(value = "id") Integer id) {
        ResponseResult<Boolean> result = new ResponseResult();
        try {
            return result.success(demoService.deleteById(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.error();
    }


    @PutMapping(value = "/put")
    public ResponseResult<Boolean> putDemo(@RequestBody DemoReq demoReq) {
        ResponseResult<Boolean> result = new ResponseResult();
        try {
            QueryWrapper<DemoBean> wrapper = new QueryWrapper<>();
            wrapper.eq("phone", demoReq.getPhone()).or().eq("id_card", demoReq.getIdCard());
            int count = demoService.selectCount(wrapper);
            if(count > 0) {
                return result.error(ResponseCodeEnum.DUPLICATE_PHONE_ID_CARD);
            }
            return result.success(demoService.updateById(Convert.ConvertDemoBean(demoReq)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.error();
    }
}
