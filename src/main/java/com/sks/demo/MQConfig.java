package com.sks.demo;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQConfig {
    public static final String QUEUE = "message_queue";
    public static final String EXCHANGE = "message_exchange";
    public static final String ROUTING_KEY = "message_routingKey";

    /**
     * Define a bean of Queue with a name and mark it as non-durable. Non-durable means that the queue and any messages
     * on it will be removed when RabbitMQ is stopped. On the other hand, restarting the application won’t have
     * any effect on the queue.
     * @return
     */
    @Bean
    public Queue queue() {
        return  new Queue(QUEUE, false);
    }

    /**
     * https://www.rabbitmq.com/tutorials/tutorial-five-python.html
     * Messages sent to a topic exchange can't have an arbitrary routing_key - it must be a list of words, delimited
     * by dots. The words can be anything, but usually they specify some features connected to the message.
     * A few valid routing key examples: "stock.usd.nyse", "nyse.vmw", "quick.orange.rabbit". There can be
     * as many words in the routing key as you like, up to the limit of 255 bytes.
     *
     * The binding key must also be in the same form. The logic behind the topic exchange is similar to a direct
     * one - a message sent with a particular routing key will be delivered to all the queues that are
     * bound with a matching binding key. However there are two important special cases for binding keys:
     * * (star) can substitute for exactly one word.
     * # (hash) can substitute for zero or more words
     * @return
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    /**
     * a bean for Binding to tie an exchange with the queue.
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    /**
     * To ensure that messages are delivered in JSON format, create a bean for MessageConverter
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return  new Jackson2JsonMessageConverter();
    }

    /**
     * Bean of rabbitTemplate serves the purpose of sending messages to the queue.
     * @param connectionFactory
     * @return
     */
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return  template;
    }
}
