package com.example.demo.mapper

import com.example.demo.dto.User
import org.apache.ibatis.annotations.*

@Mapper
interface UserMapper {
    @Insert("INSERT INTO users (name) VALUES (#{user.name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    fun insert(@Param("user") user: User): Long

    @Select("SELECT * FROM users WHERE id = #{id}")
    fun selectById(@Param("id") id: Long): User?

    @Select("SELECT * FROM users")
    fun selectAll(): List<User>

    @Update("UPDATE users SET name = #{user.name} WHERE id = #{user.id}")
    fun update(@Param("user") user: User)

    @Delete("DELETE FROM users WHERE id = #{id}")
    fun deleteById(@Param("id") id: Long)
}