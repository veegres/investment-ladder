package org.veegres.invest.ladder.job

import io.micronaut.scheduling.annotation.Scheduled
import org.slf4j.LoggerFactory
import java.text.SimpleDateFormat
import java.util.Date
import jakarta.inject.Singleton

@Singleton
class LadderJob {

    @Scheduled(fixedDelay = "10s")
    fun executeEveryTen() {
        LOG.info("Simple Job every 10 seconds: {}", SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
    }

    @Scheduled(fixedDelay = "45s", initialDelay = "5s")
    fun executeEveryFourtyFive() {
        LOG.info("Simple Job every 45 seconds: {}", SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LadderJob::class.java)
    }
}