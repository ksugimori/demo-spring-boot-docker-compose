package com.example.demo_spring_boot_docker_compose

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoSpringBootDockerComposeApplication

fun main(args: Array<String>) {
	runApplication<DemoSpringBootDockerComposeApplication>(*args)
}
