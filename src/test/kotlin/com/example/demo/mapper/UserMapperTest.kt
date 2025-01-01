package com.example.demo.mapper

import com.example.demo.dto.User
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@MybatisTest
@Sql("create_users.sql")
class UserMapperTest {
    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val mySqlContainer = MySQLContainer("mysql:8.1.0")
    }

    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var userMapper: UserMapper

    @BeforeTest
    fun setUp() {
        jdbcTemplate.execute("INSERT INTO users VALUES (1, 'Alice')")
        jdbcTemplate.execute("INSERT INTO users VALUES (2, 'Bob')")
    }

    @Test
    fun testSelect() {
        val result = userMapper.selectById(1)

        assertEquals(expected = User(1L, "Alice"), actual = result)
    }
}