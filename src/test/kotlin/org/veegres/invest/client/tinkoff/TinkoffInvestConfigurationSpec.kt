package org.veegres.invest.client.tinkoff

import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest
class TinkoffInvestConfigurationSpec {

    @Test
    fun testTinkoffInvestConfiguration() {
        val names = listOf("Nirav Assar", "Lionel Messi")
        val items = mapOf(
            "client.tinkoff.app-name" to "evolution",
            "team.color" to "green",
            "team.player-names" to names
        )

        val ctx = ApplicationContext.run(items)
        val config = ctx.getBean(TinkoffInvestConfiguration::class.java)

        assertEquals(config.`app-name`, "evolution")

        ctx.close()
    }

}