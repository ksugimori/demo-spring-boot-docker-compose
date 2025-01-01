package com.example.demo.controller

import com.example.demo.dto.User
import com.example.demo.mapper.UserMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userMapper: UserMapper) {
    @GetMapping("/{id}")
    fun read(@PathVariable id: Long): User {
        return userMapper.selectById(id) ?: throw UserNotFoundException()
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleException(ex: UserNotFoundException): ResponseEntity<Unit> {
        return ResponseEntity.notFound().build()
    }
}

class UserNotFoundException : RuntimeException()