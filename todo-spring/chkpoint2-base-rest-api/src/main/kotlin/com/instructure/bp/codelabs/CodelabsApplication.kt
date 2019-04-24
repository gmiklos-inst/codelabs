package com.instructure.bp.codelabs

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableSwagger2
@SpringBootApplication
class CodelabsApplication

fun main(args: Array<String>) {
	runApplication<CodelabsApplication>(*args)
}
