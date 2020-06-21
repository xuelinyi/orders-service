package com.orders.enums;

public enum OrderStatusEnum {


    /**
     * 待支付
     */
    PAID(1, "待支付"),

    /**
     * 支付成功
     */
    PAYMENT_SUCCESSFUL(2, "支付成功"),

    /**
     * 取消
     */
    CANCEL(3, "取消")
    ,

    /**
     * 支付失败
     */
    PAYMENT_FAIL(4, "支付失败");


    int value;
    String name;

    OrderStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


    /**
     * 通过value获取枚举的name
     *
     * @param value
     * @return
     */
    public static String getName(int value) {
        OrderStatusEnum enumerate = getEnum(value);
        return enumerate == null ? null : enumerate.getName();
    }

    /**
     * 通过value获取枚举
     *
     * @param value
     * @return
     */
    public static OrderStatusEnum getEnum(int value) {
        for (OrderStatusEnum enumerate : OrderStatusEnum.values()) {
            if (enumerate.getValue() == value) {
                return enumerate;
            }
        }
        return null;
    }
    
}
