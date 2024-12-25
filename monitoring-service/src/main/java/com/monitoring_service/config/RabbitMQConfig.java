package com.monitoring_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "deviceExchange";
    public static final String CREATE_QUEUE = "deviceCreateQueue";
    public static final String UPDATE_QUEUE = "deviceUpdateQueue";
    public static final String DELETE_QUEUE = "deviceDeleteQueue";

    @Bean
    public Queue queue() {
        return new Queue("energy_measurements", true); // Durable queue
    }

    @Bean
    public TopicExchange deviceExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue createQueue() {
        return new Queue(CREATE_QUEUE);
    }

    @Bean
    public Queue updateQueue() {
        return new Queue(UPDATE_QUEUE);
    }

    @Bean
    public Queue deleteQueue() {
        return new Queue(DELETE_QUEUE);
    }

    @Bean
    public Queue deleteAllByUserIdQueue() {
        return new Queue("deviceDeleteAllByUserIdQueue");
    }

    @Bean
    public Binding bindingDeleteAllByUserIdQueue(Queue deleteAllByUserIdQueue, TopicExchange deviceExchange) {
        return BindingBuilder.bind(deleteAllByUserIdQueue).to(deviceExchange).with("device.deleteAllByUserId");
    }



    @Bean
    public Binding bindingCreateQueue(Queue createQueue, TopicExchange deviceExchange) {
        return BindingBuilder.bind(createQueue).to(deviceExchange).with("device.create");
    }

    @Bean
    public Binding bindingUpdateQueue(Queue updateQueue, TopicExchange deviceExchange) {
        return BindingBuilder.bind(updateQueue).to(deviceExchange).with("device.update");
    }

    @Bean
    public Binding bindingDeleteQueue(Queue deleteQueue, TopicExchange deviceExchange) {
        return BindingBuilder.bind(deleteQueue).to(deviceExchange).with("device.delete");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

