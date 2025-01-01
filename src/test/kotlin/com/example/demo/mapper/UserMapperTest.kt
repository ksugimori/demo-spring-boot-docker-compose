package com.example.demo.mapper

import com.example.demo.config.TestcontainersConfig
import com.example.demo.dto.User
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import kotlin.test.Test
import kotlin.test.assertEquals

@MybatisTest
@Sql("/ddl/create_users.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@ImportTestcontainers(TestcontainersConfig::class)
class UserMapperTest {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var userMapper: UserMapper

    @Test
    @Sql(statements = ["INSERT INTO users VALUES (1, 'Alice')"])
    fun testSelect() {
        val result = userMapper.selectById(1)

        assertEquals(expected = User(1L, "Alice"), actual = result)
    }
}