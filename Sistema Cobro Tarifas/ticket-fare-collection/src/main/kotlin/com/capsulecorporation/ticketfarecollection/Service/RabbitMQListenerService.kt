package com.capsulecorporation.ticketfarecollection.Service

import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry
import org.springframework.stereotype.Service

@Service
class RabbitMQListenerService(private val rabbitListenerEndpointRegistry: RabbitListenerEndpointRegistry) {
    fun startListeners() {
        rabbitListenerEndpointRegistry.start()
    }
}