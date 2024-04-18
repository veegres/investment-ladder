package org.veegres.invest.ladder

import io.micronaut.runtime.Micronaut
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*
import kotlinx.coroutines.runBlocking

@OpenAPIDefinition(
    info = Info(title = "investment-ladder", version = "1.0", description = "Invest with ladder strategy")
)
object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build(*args).banner(true).start()
    }
}