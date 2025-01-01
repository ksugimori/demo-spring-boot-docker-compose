package com.example.demo.mapper

import com.example.demo.dto.User
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Param
import org.apache.ibatis.annotations.Select

@Mapper
interface UserMapper {
    @Select("SELECT * FROM users WHERE id = #{id}")
    fun selectById(@Param("id") id: Long): User?
}