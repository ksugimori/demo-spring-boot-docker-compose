package com.example.demo

import com.example.demo.config.MyContainersConfiguration
import org.springframework.boot.fromApplication

fun main(args: Array<String>) {
	fromApplication<DemoApplication>()
		.with(MyContainersConfiguration::class.java)
		.run(*args)
}