package com.lookingdev.api;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.amqp.core.Queue;

import java.util.List;

@SpringBootApplication
public class LookingDevApiApplication {

    private final RabbitAdmin rabbitAdmin;

    private final List<Queue> queues;

    @Autowired
    public LookingDevApiApplication(RabbitAdmin rabbitAdmin, List<Queue> queues) {
        this.rabbitAdmin = rabbitAdmin;
        this.queues = queues;
    }

    @PostConstruct
    public void declareQueues() {
        queues.forEach(rabbitAdmin::declareQueue);
    }


    public static void main(String[] args) {
        SpringApplication.run(LookingDevApiApplication.class, args);
    }

}
