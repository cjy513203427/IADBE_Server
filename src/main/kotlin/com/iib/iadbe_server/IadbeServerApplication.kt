package com.iib.iadbe_server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IadbeServerApplication

fun main(args: Array<String>) {
    runApplication<IadbeServerApplication>(*args)
}
