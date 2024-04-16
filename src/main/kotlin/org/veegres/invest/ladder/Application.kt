package org.veegres.invest.ladder

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*

@OpenAPIDefinition(
    info = Info(
        title = "invest-ladder",
        version = "1.0",
        description = "Invest with ladder strategy"
    )
)
object Api {
}
fun main(args: Array<String>) {
	run(*args)
}

