package com.orders.config.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue Queue() {
        return new Queue("hello");
    }


    // 下单并且派单存队列
    public static final String ORDER_DIC_QUEUE = "order_dic_queue";
    // 补单队列，判断订单是否已经被创建
    public static final String ORDER_CREATE_QUEUE = "order_create_queue";
    // 下单并且派单交换机
    public static final String ORDER_EXCHANGE_NAME = "order_exchange_name";

    // 1.定义订单队列
    @Bean
    public Queue directOrderDicQueue() {
        return new Queue(ORDER_DIC_QUEUE);
    }

    // 2.定义补订单队列
    @Bean
    public Queue directCreateOrderQueue() {
        return new Queue(ORDER_CREATE_QUEUE);
    }

    // 2.定义交换机
    @Bean
    DirectExchange directOrderExchange() {
        return new DirectExchange(ORDER_EXCHANGE_NAME);
    }

    // 3.订单队列与交换机绑定
    @Bean
    Binding bindingExchangeOrderDicQueue() {
        return BindingBuilder.bind(directOrderDicQueue()).to(directOrderExchange()).with("orderRoutingKey");
    }

    // 3.补单队列与交换机绑定
    @Bean
    Binding bindingExchangeCreateOrder() {
        return BindingBuilder.bind(directCreateOrderQueue()).to(directOrderExchange()).with("orderRoutingKey");
    }



    /**
     * 死信队列 交换机标识符
     */
    private static final String DEAD_LETTER_QUEUE_KEY = "x-dead-letter-exchange";

    /**
     * 死信队列交换机绑定键标识符
     */
    private static final String DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * 死信队列跟交换机类型没有关系 不一定为directExchange 不影响该类型交换机的特性.
     */
    @Bean("deadLetterExchange")
    public DirectExchange deadLetterExchange() {
        // return (DirectExchange)
        // ExchangeBuilder.directExchange("DL_EXCHANGE").durable(true).build();
        return new DirectExchange("DL_EXCHANGE", true, false);
    }

    /**
     * 声明一个死信队列. x-dead-letter-exchange 对应 死信交换机 x-dead-letter-routing-key 对应
     * 死信队列
     */
    @Bean("deadLetterQueue")
    public Queue deadLetterQueue() {
        Map<String, Object> args = new HashMap<>(2);
        // x-dead-letter-exchange 声明 死信交换机
        args.put(DEAD_LETTER_QUEUE_KEY, "DL_EXCHANGE");
        // x-dead-letter-routing-key 声明 死信路由键
        args.put(DEAD_LETTER_ROUTING_KEY, "KEY_R");
        return QueueBuilder.durable("DL_QUEUE").withArguments(args).build();
    }

    /**
     * 定义死信队列转发队列.
     *
     * @return the queue
     */
    @Bean("redirectQueue")
    public Queue redirectQueue() {
        return QueueBuilder.durable("REDIRECT_QUEUE").build();
    }

    /**
     * 死信路由通过 DL_KEY 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding deadLetterBinding() {
        return new Binding("DL_QUEUE", Binding.DestinationType.QUEUE, "DL_EXCHANGE", "DL_KEY", null);
    }

    /**
     * 死信路由通过 KEY_R 绑定键绑定到死信队列上.
     *
     * @return the binding
     */
    @Bean
    public Binding redirectBinding() {
        return new Binding("REDIRECT_QUEUE", Binding.DestinationType.QUEUE, "DL_EXCHANGE", "KEY_R", null);
    }


}