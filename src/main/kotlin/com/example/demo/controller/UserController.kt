package com.example.demo.controller

import com.example.demo.dto.User
import com.example.demo.mapper.UserMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder


@RestController
@RequestMapping("/users")
class UserController(private val userMapper: UserMapper) {
    @PostMapping
    fun create(@RequestBody user: User): ResponseEntity<User> {
        userMapper.insert(user)

        val location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}").buildAndExpand(user.id)
            .toUri()

        return ResponseEntity.created(location).body(user)
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Long): User {
        return userMapper.selectById(id) ?: throw UserNotFoundException()
    }

    @GetMapping
    fun readAll(): List<User> = userMapper.selectAll()

    @ExceptionHandler(UserNotFoundException::class)
    fun handleException(ex: UserNotFoundException): ResponseEntity<Unit> {
        return ResponseEntity.notFound().build()
    }
}

class UserNotFoundException : RuntimeException()