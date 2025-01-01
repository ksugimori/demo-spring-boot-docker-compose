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
import kotlin.test.assertContains
import kotlin.test.assertEquals

@MybatisTest
@Sql(scripts = ["/ddl/create_users.sql"], statements = ["TRUNCATE users"])
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@ImportTestcontainers(TestcontainersConfig::class)
class UserMapperTest {
    @Autowired
    lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    lateinit var userMapper: UserMapper

    @Test
    @Sql(statements = ["INSERT INTO users VALUES (1, 'Alice')"])
    fun testInsert() {
        val user = User(id = null, name = "Bob")
        userMapper.insert(user)

        val resultList = userMapper.selectAll()

        assertEquals(2, resultList.size)
        assertContains(resultList, User(1L, "Alice"))
        assertContains(resultList, User(2L, "Bob"))
    }

    @Test
    @Sql(statements = ["INSERT INTO users VALUES (1, 'Alice')"])
    fun testSelectById() {
        val result = userMapper.selectById(1)

        assertEquals(expected = User(1L, "Alice"), actual = result)
    }

    @Test
    @Sql(
        statements = [
            "INSERT INTO users VALUES (1, 'Alice')",
            "INSERT INTO users VALUES (2, 'Bob')",
        ]
    )
    fun testSelectAll() {
        val resultList = userMapper.selectAll()

        assertEquals(2, resultList.size)
        assertContains(resultList, User(1L, "Alice"))
        assertContains(resultList, User(2L, "Bob"))
    }

    @Test
    @Sql(
        statements = [
            "INSERT INTO users VALUES (1, 'Alice')",
            "INSERT INTO users VALUES (2, 'Bob')",
        ]
    )
    fun testUpdate() {
        userMapper.update(User(1, "Updated"))

        val resultList = userMapper.selectAll()

        assertEquals(2, resultList.size)
        assertContains(resultList, User(1L, "Updated"))
        assertContains(resultList, User(2L, "Bob"))
    }

    @Test
    @Sql(
        statements = [
            "INSERT INTO users VALUES (1, 'Alice')",
            "INSERT INTO users VALUES (2, 'Bob')",
        ]
    )
    fun testDeleteById() {
        userMapper.deleteById(1)

        val resultList = userMapper.selectAll()

        assertEquals(1, resultList.size)
        assertContains(resultList, User(2L, "Bob"))
    }
}