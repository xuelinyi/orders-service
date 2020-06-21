package com.orders.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DemoReq implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

}
