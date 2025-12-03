package com.capsulecorporation.ticketfarecollection

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TicketFareCollectionApplication

fun main(args: Array<String>) {
    runApplication<TicketFareCollectionApplication>(*args)
}