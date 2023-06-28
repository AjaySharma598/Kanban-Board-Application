/*
 * Author:Ajay Sharma
 * Date : 16-02-2023
 *Created With : Intellij IDEA Community Edition
 */

package com.niit.rabbitMq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MessageConfiguration {
    private  String exchangeName="direct-Exchange";
    private String queueName="direct-Queue";

    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange(exchangeName);
    }
    @Bean
    public Queue requestQueue(){
        return new Queue(queueName,true);
    }
    @Bean
    public Jackson2JsonMessageConverter movieJackson2JsonConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate=new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(movieJackson2JsonConverter());
        return rabbitTemplate;
    }
    @Bean
    public Binding bindingExchangeAndQueue(DirectExchange directExchange, Queue queue){
        return  BindingBuilder.bind(requestQueue()).to(directExchange).with("direct-routing");
    }

}
