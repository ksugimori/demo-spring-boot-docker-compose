package com.example.demo.mapper

import com.example.demo.config.MyContainers
import com.example.demo.dto.User
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.AutoConfigureDataJdbc
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.test.context.jdbc.Sql
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@MybatisTest
@ImportTestcontainers(MyContainers::class)
@AutoConfigureDataJdbc
@Sql(
    scripts = ["/ddl/create_users.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class UserMapperTest {
    @Autowired
    lateinit var jdbcClient: JdbcClient

    @Autowired
    lateinit var userMapper: UserMapper

    @Test
    fun testInsert() {
        // When
        val user = User(id = null, name = "Alice")
        userMapper.insert(user)

        // Then
        val createdRecord = jdbcClient
            .sql("SELECT id, name FROM users WHERE id = ${user.id}")
            .query(User::class.java)
            .single()

        assertEquals("Alice", createdRecord.name)
    }

    @Test
    @Sql(statements = ["INSERT INTO users VALUES (1, 'Alice')"])
    fun testSelectById() {
        val result = userMapper.selectById(1)

        assertEquals(User(1L, "Alice"), result)
    }

    @Test
    @Sql(
        statements = [
            "INSERT INTO users VALUES (1, 'Alice')",
            "INSERT INTO users VALUES (2, 'Bob')",
        ]
    )
    fun testSelectAll() {
        // When
        val resultList = userMapper.selectAll()

        // Then
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
        // When
        userMapper.update(User(1, "Updated"))

        // Then
        val records = jdbcClient.sql("SELECT id, name FROM users")
            .query(User::class.java)
            .list()

        assertEquals(2, records.size)
        assertContains(records, User(1L, "Updated"))
        assertContains(records, User(2L, "Bob"))
    }

    @Test
    @Sql(
        statements = [
            "INSERT INTO users VALUES (1, 'Alice')",
            "INSERT INTO users VALUES (2, 'Bob')",
        ]
    )
    fun testDeleteById() {
        // When
        userMapper.deleteById(1)

        // Then
        val records = jdbcClient.sql("SELECT id, name FROM users")
            .query(User::class.java)
            .list()

        assertEquals(1, records.size)
        assertContains(records, User(2L, "Bob"))
    }
}