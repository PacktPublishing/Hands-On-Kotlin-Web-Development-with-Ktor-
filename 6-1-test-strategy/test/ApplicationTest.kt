package com.example

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testMyUnit() {
        val s = "test"
        assertEquals("test", s)
    }

    @Test
    fun testRoot() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("HELLO WORLD!", response.content)
            }
        }
    }

    @Test
    fun testClientMock() {
        runBlocking {
            val client = HttpClient(MockEngine) {
                engine {
                    addHandler { request ->
                        when (request.url.fullPath) {
                            "/" -> respond(
                                    ByteReadChannel(byteArrayOf(1, 2, 3)),
                                    headers = headersOf("X-MyHeader", "MyValue")
                            )
                            else -> respond("Not Found ${request.url.encodedPath}", HttpStatusCode.NotFound)
                        }
                    }
                }
                expectSuccess = false
            }
            assertEquals(byteArrayOf(1, 2, 3).toList(), client.get<ByteArray>("/").toList())
            assertEquals("Not Found api/other/path", client.get<String>("/api/other/path"))
        }
    }
}
