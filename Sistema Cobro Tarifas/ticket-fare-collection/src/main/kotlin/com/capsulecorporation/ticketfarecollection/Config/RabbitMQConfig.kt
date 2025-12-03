package com.capsulecorporation.ticketfarecollection.Config

import com.capsulecorporation.ticketfarecollection.Database.LocalDatabase
import com.capsulecorporation.ticketfarecollection.Model.FareTransaction
import org.springframework.amqp.core.*
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.SimpleMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@Configuration
class RabbitMQConfig {
    @Value("\${rabbitmq.queue.name}")
    private lateinit var queueName: String

    @Value("\${rabbitmq.exchange.name}")
    private lateinit var exchange: String

    @Value("\${rabbitmq.routing.key}")
    private lateinit var routingKey: String

    @Value("\${rabbitmq.dead-letter.queue.name}")
    private lateinit var deadLetterQueueName: String

    @Value("\${rabbitmq.dead-letter.exchange.name}")
    private lateinit var deadLetterExchange: String

    @Value("\${spring.rabbitmq.host}")
    private lateinit var host: String

    @Value("\${spring.rabbitmq.port}")
    private var port: Int = 0

    @Value("\${spring.rabbitmq.username}")
    private lateinit var username: String

    @Value("\${spring.rabbitmq.password}")
    private lateinit var password: String

    @Bean
    fun localDatabase() = LocalDatabase()

    @Bean
    fun queue(): Queue {
        val args = mapOf("x-dead-letter-exchange" to deadLetterExchange)
        return Queue(queueName, true, false, false, args)
    }

    @Bean
    fun deadLetterQueue() = Queue(deadLetterQueueName)

    @Bean
    fun exchange() = TopicExchange(exchange)

    @Bean
    fun deadLetterExchange() = TopicExchange(deadLetterExchange)

    @Bean
    fun binding() = BindingBuilder.bind(queue()).to(exchange()).with(routingKey)

    @Bean
    fun deadLetterBinding() = BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(routingKey)



    @Bean
    fun connectionFactory(): ConnectionFactory {
        val connectionFactory = CachingConnectionFactory()
        connectionFactory.setHost(host)
        connectionFactory.port = port
        connectionFactory.username = username
        connectionFactory.setPassword(password)
        return connectionFactory
    }

    @Bean
    fun rabbitTemplate(): RabbitTemplate {
        val template = RabbitTemplate(connectionFactory())
        template.setExchange(exchange)
        template.routingKey = routingKey
        return template
    }

    @Bean
    fun retryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()

        val backOffPolicy = ExponentialBackOffPolicy()
        backOffPolicy.initialInterval = 1000
        backOffPolicy.multiplier = 2.0
        backOffPolicy.maxInterval = 30000

        retryTemplate.setBackOffPolicy(backOffPolicy)

        val retryPolicy = SimpleRetryPolicy()
        retryPolicy.maxAttempts = 3

        retryTemplate.setRetryPolicy(retryPolicy)

        return retryTemplate
    }

    @Bean
    fun rabbitAdmin() = RabbitAdmin(connectionFactory())



}