package com.example.demo.controller

import com.example.demo.config.MyContainers
import com.example.demo.dto.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.http.MediaType
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ImportTestcontainers(MyContainers::class)
@Sql(
    scripts = ["/ddl/create_users.sql"],
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
class UserControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var jdbcClient: JdbcClient

    @AfterTest
    fun tearDown() {
        jdbcClient.sql("TRUNCATE users").update()
    }

    @Test
    fun testCreate() {
        val responseBody: User? = webTestClient.post().uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"name": "Alice"}""")
            .exchange()
            .expectStatus().isCreated()
            .expectBody(User::class.java)
            .returnResult()
            .responseBody

        assertNotNull(responseBody)
        assertEquals("Alice", responseBody.name)

        // DB 登録確認
        val createdRecord = jdbcClient
            .sql("SELECT id, name FROM users WHERE id = ${responseBody.id}")
            .query(User::class.java)
            .single()

        assertEquals("Alice", createdRecord.name)
    }

    @Test
    @Sql(statements = ["INSERT INTO users VALUES (1, 'Alice')"])
    fun testRead() {
        val responseBody = webTestClient.get().uri("/users/1")
            .exchange()
            .expectBody(User::class.java)
            .returnResult()
            .responseBody

        assertEquals(User(1L, "Alice"), responseBody)
    }

    @Test
    @Sql(statements = ["INSERT INTO users VALUES (1, 'Alice')"])
    fun testReadNotFound() {
        webTestClient.get().uri("/users/99")
            .exchange()
            .expectStatus().isNotFound()
    }

    @Test
    @Sql(
        statements = [
            "INSERT INTO users VALUES (1, 'Alice')",
            "INSERT INTO users VALUES (2, 'Bob')",
        ]
    )
    fun testReadAll() {
        webTestClient.get().uri("/users")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectBody()
            .json(
                """
                [
                    {
                        "id": 1,
                        "name": "Alice"
                    },
                    {
                        "id": 2,
                        "name": "Bob"
                    }
                ]
            """.trimIndent()
            )
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
        webTestClient.put().uri("/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"id": 1, "name": "Updated"}""")
            .exchange()
            .expectStatus().isOk()
            .expectBody().isEmpty()

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
    fun testUpdateBadRequest() {
        // When
        webTestClient.put().uri("/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue("""{"id": 99, "name": "Updated"}""")
            .exchange()
            .expectStatus().isBadRequest()

        // Then
        val records = jdbcClient.sql("SELECT id, name FROM users")
            .query(User::class.java)
            .list()

        assertEquals(2, records.size)
        assertContains(records, User(1L, "Alice"))
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
        webTestClient.delete().uri("/users/1")
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty()

        // Then
        val records = jdbcClient.sql("SELECT id, name FROM users")
            .query(User::class.java)
            .list()

        assertEquals(1, records.size)
        assertContains(records, User(2L, "Bob"))
    }
}