package com.example

import kotlinx.coroutines.*
import java.util.*


fun main(args: Array<String>) = runBlocking {
    withContext(Dispatchers.IO) {
        repeat(200_000) {
            launch {
                firstcoroutine(it)
            }
        }
        println("Done with the context")
    }
    println("End of the function")
}

suspend fun firstcoroutine(id: Int) {
    delay(Random().nextLong() % 2000)
    println("first $id")
}