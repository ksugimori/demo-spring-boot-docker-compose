package com.example.demo.controller

import com.example.demo.dto.User
import com.example.demo.mapper.UserMapper
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(private val userMapper: UserMapper) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody user: User): User {
        userMapper.insert(user)

        return user
    }

    @GetMapping("/{id}")
    fun read(@PathVariable id: Long): User {
        return userMapper.selectById(id) ?: throw UserNotFoundException(id)
    }

    @GetMapping
    fun readAll(): List<User> {
        return userMapper.selectAll()
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody user: User) {
        if (id != user.id) {
            throw InconsistentUserIdException(id, user.id)
        }

        userMapper.update(user)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        userMapper.deleteById(id)
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
private class UserNotFoundException(id: Long) : RuntimeException("user not found: $id")

@ResponseStatus(HttpStatus.BAD_REQUEST)
private class InconsistentUserIdException(idInPath: Long, idInBody: Long?) :
    RuntimeException("requested ID not matched. path: /users/$idInPath, request body: $idInBody")