package com.instructure.bp.codelabs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CodelabsApplication

fun main(args: Array<String>) {
	runApplication<CodelabsApplication>(*args)
}
