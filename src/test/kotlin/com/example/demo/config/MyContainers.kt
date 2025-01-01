package com.example.demo.config

import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container

object MyContainers {
    @Container
    @ServiceConnection
    val mySqlContainer = MySQLContainer("mysql:8.1.0")
}