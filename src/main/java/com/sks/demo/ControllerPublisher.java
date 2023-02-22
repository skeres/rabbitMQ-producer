package com.sks.demo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
public class ControllerPublisher {

    @Autowired
    private RabbitTemplate template;

    @GetMapping("/publish/{message}")
    public String publishStringMessage(@PathVariable String message) {
       template.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY, new StringMessage01(message));
        System.out.println("application reached method publishStringMessage with data "+message);

        return "Message " +message + " Published";
    }

    @GetMapping("/publish")
    public String publishObjectMessage() {
        ObjectMessage01 objectMessage01= new ObjectMessage01();
        objectMessage01.setMessage("hello");
        objectMessage01.setMessageId(UUID.randomUUID().toString());
        objectMessage01.setMessageDate(new Date());
        template.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY, objectMessage01);
        System.out.println("application reached method publishObjectMessage with data "+ objectMessage01.toString());

        return "Message " +objectMessage01.toString() + " Published";
    }



}