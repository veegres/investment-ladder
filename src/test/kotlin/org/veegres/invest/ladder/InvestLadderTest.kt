package org.veegres.invest.ladder

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import jakarta.inject.Inject
import java.time.Duration

@MicronautTest
class InvestLadderTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }

    @Test
    fun testDuration() {
        val tt = Duration.parse("1 day")
        Assertions.assertEquals(tt.toDays(),  1)
    }

}
