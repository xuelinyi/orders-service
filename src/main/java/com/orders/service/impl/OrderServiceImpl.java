package com.orders.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.orders.bean.OrderInfoBean;
import com.orders.config.rabbitmq.RabbitConfig;
import com.orders.controller.OrderController;
import com.orders.enums.OrderStatusEnum;
import com.orders.mapper.OrderMapper;
import com.orders.service.OrderService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderInfoBean> implements OrderService,RabbitTemplate.ConfirmCallback  {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Boolean addOrderAndDispatch() throws Exception{
        //先下单 订单表插入数据
        OrderInfoBean orderInfoBean = new OrderInfoBean();
        orderInfoBean.setName("黄焖鸡米饭");
        // 价格是300元
        orderInfoBean.setOrderMoney(new BigDecimal("12.96"));
        // 商品id
        String orderCode = UUID.randomUUID().toString();
        orderInfoBean.setOrderCode(orderCode);
        orderInfoBean.setStatus(OrderStatusEnum.PAID.getValue());
        // 1.先下单，创建订单 (往订单数据库中插入一条数据)
        int orderResult = orderMapper.insert(orderInfoBean);
        logger.info("orderResult:" + orderResult);
        if (orderResult <= 0) {
            return false;
            //return setResultError("下单失败!");
        }
        // 2.订单表插插入完数据后 订单表发送 减少商品
        sendReduceStock("12");
        //订单取消
        sendCancelOrder(orderInfoBean.getId()+"");
        return true;
    }

    private void sendReduceStock(String goodsId) {
        JSONObject jsonObect = new JSONObject();
        jsonObect.put("goodsId", goodsId);
        String msg = jsonObect.toJSONString();
        System.out.println("msg:" + msg);
        // 封装消息
        Message message = MessageBuilder.withBody(msg.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8").setMessageId(goodsId).build();
        // 构建回调返回的数据
        CorrelationData correlationData = new CorrelationData(goodsId);
        // 发送消息
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend(RabbitConfig.ORDER_EXCHANGE_NAME, "orderRoutingKey", message, correlationData);
    }


    private void sendCancelOrder(String orderCode) {
        JSONObject jsonObect = new JSONObject();
        jsonObect.put("orderId", orderCode);
        String msg = jsonObect.toJSONString();
        System.out.println("msg:" + msg);
        // 封装消息
        Message message = MessageBuilder.withBody(msg.getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .setContentEncoding("utf-8").setMessageId(orderCode).setExpiration("10000").build();
        // 构建回调返回的数据
        CorrelationData correlationData = new CorrelationData(orderCode);
        // 发送消息
        this.rabbitTemplate.setMandatory(true);
        this.rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.convertAndSend("DL_EXCHANGE", "DL_KEY", message, correlationData);
    }


    // 生产消息确认机制 生产者往服务器端发送消息的时候 采用应答机制
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String goodsId = correlationData.getId(); //id 都是相同的哦  全局ID
        System.out.println("消息id:" + correlationData.getId());
        if (ack) { //消息发送成功
            logger.info("消息发送确认成功");
        } else {
            //重试机制
            sendReduceStock(goodsId);
            logger.info("消息发送确认失败:" + cause);
        }
    }

    /**
     * 10秒后取消未支付的订单
     * @param message
     * @param channel
     * @throws IOException
     */
    @RabbitListener(queues = "REDIRECT_QUEUE")
    @RabbitHandler
    public void redirect(Message message, Channel channel) throws IOException {
        String messageId = message.getMessageProperties().getMessageId();
        String msg = new String(message.getBody(), "UTF-8");
        System.out.println("派单服务平台" + msg + ",消息id:" + messageId);
        OrderInfoBean orderInfoBean = orderMapper.selectById(messageId);
        if(null != orderInfoBean){
            if(orderInfoBean.getStatus() == OrderStatusEnum.PAID.getValue()){
                OrderInfoBean updateOrder = new OrderInfoBean();
                updateOrder.setId(Integer.parseInt(messageId));
                updateOrder.setStatus(OrderStatusEnum.CANCEL.getValue());
                int result = orderMapper.updateById(updateOrder);
                if(result > 0) {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                }
            }
        }
    }
}
