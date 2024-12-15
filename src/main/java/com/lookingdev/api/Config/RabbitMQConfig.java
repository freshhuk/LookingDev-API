package com.lookingdev.api.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.username}")
    private String queueUserName;

    @Value("${spring.rabbitmq.password}")
    private String queuePassword;

    /* Names all queue */
    @Value("${queueAPIStatus.name}")
    private String queueAPIStatus;

    @Value("${queueGitStatus.name}")
    private String queueGitStatus;

    @Value("${queueStackOverflowStatus.name}")
    private String queueStackOverflowStatus;

    @Value("${queueAuthStatus.name}")
    private String queueAuthStatus;


    /* Queues beans */
    @Bean
    public Queue queueAPIStatus(){
        return new Queue(queueAPIStatus, false);
    }
    @Bean
    public Queue queueGitStatus(){
        return new Queue(queueGitStatus, false);
    }
    @Bean
    public Queue queueStackOverflowStatus(){
        return new Queue(queueStackOverflowStatus, false);
    }
    @Bean
    public Queue queueAuthStatus(){
        return new Queue(queueAuthStatus, false);
    }


    @Bean
    public CachingConnectionFactory connectionFactory(){
        CachingConnectionFactory connection = new CachingConnectionFactory("localhost");
        connection.setUsername(queueUserName);
        connection.setPassword(queuePassword);
        return connection;
    }
    @Bean
    public RabbitAdmin rabbitAdmin(){
        return  new RabbitAdmin(connectionFactory());
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

}
