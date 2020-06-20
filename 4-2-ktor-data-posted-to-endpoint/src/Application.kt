package com.example

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.InputStream
import java.io.OutputStream

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        post("/text") {
            val text = call.receiveText()
            log.info("Text received: $text")
            call.respondText("Thank you we received $text")
        }
        post("/form") {
            val parameters = call.receiveParameters()
            parameters.names().forEach {
                val myvalue = parameters.get(it)
                log.info("key: $it, value: $myvalue")
            }
            call.respondText("Thank you for the form data")
        }
        post("/file") {
            val multipart = call.receiveMultipart()
            var title = ""
            val uploadDir = "./upload"
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        if (part.name == "title") {
                            title = part.value
                        }
                    }
                    is PartData.FileItem -> {
                        val ext = File(part.originalFileName).extension
                        val file = File(uploadDir, "upload-${System.currentTimeMillis()}-${title.hashCode()}.$ext")
                        part.streamProvider().use { input ->
                            file.outputStream().buffered().use { output -> input.copyToSuspend(output) }
                        }
                    }
                }
                part.dispose()
            }
        }

        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/gson") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}

suspend fun InputStream.copyToSuspend(
    out: OutputStream,
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    yieldSize: Int = 4 * 1024 * 1024,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
): Long {
    return withContext(dispatcher) {
        val buffer = ByteArray(bufferSize)
        var bytesCopied = 0L
        var bytesAfterYield = 0L
        while (true) {
            val bytes = read(buffer).takeIf { it >= 0 } ?: break
            out.write(buffer, 0, bytes)
            if (bytesAfterYield >= yieldSize) {
                yield()
                bytesAfterYield %= yieldSize
            }
            bytesCopied += bytes
            bytesAfterYield += bytes
        }
        return@withContext bytesCopied
    }
}

