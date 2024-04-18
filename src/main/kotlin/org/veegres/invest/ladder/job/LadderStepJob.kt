package org.veegres.invest.ladder.job

import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import kotlinx.coroutines.*
import org.slf4j.LoggerFactory
import org.veegres.invest.ladder.entity.Ladder
import org.veegres.invest.ladder.entity.LadderRepository
import org.veegres.invest.ladder.entity.LadderStatus
import org.veegres.invest.ladder.entity.LadderStepRepository
import org.veegres.invest.ladder.service.LadderStepService
import org.veegres.invest.ladder.service.StepExecuted
import org.veegres.invest.ladder.service.toLadderStep
import org.veegres.invest.ladder.service.toStep
import java.time.Instant

@Singleton
class LadderStepJob(
    val ladderRepository: LadderRepository,
    val ladderStepRepository: LadderStepRepository,
    val ladderStepService: LadderStepService
) {

    // TODO idea: separate lastOrderTime from lastTryExecuteStepTime. benefits: no retries if something gonna happen
    @Scheduled(fixedDelay = "15s", initialDelay = "5s")
    fun handle() {
        LOG.info("Job started processing")
        val now = Instant.now()
        val ladders = ladderRepository.findReadyLaddersAndLock(now, LadderStatus.IN_PROGRESS)
        LOG.info("Next ladders are going to be execute step ${ladders.map { it.id }}")
        runBlocking {
            val deferreds = ladders.map { ladder ->
                async(Dispatchers.Default) {
                    LOG.info("Start step execution for ${ladder.id} with interval ${ladder.stepInterval}")
                    try {
                        val step = ladder.toStep()
                        val execStep = ladderStepService.execute(step)
                        updateLadderAndStepWithUnlock(ladder, execStep)
                    } catch (re: RuntimeException) { // TODO need different behavior for errors, and notifications for customer
                        LOG.error("Step executed with error ${re.message}")
                        unlockLadder(ladder)
                    }
                }
            }
            deferreds.awaitAll()
        }
        LOG.info("Job ended processing")
    }

    private fun updateLadderAndStepWithUnlock(ladder: Ladder, execStep: StepExecuted) {
        val ladderStep = execStep.toLadderStep(ladder.id)
        val firstPrice = ladder.firstOrderPrice ?: execStep.price
        val firstOn = ladder.firstOrderOn ?: Instant.now()
        val lastPrice = execStep.price
        val lastOn = Instant.now()
        ladderStepRepository.save(ladderStep)
        ladderRepository.updateByStepAndUnlock(ladder.id, LadderStatus.IN_PROGRESS, firstPrice, firstOn, lastPrice, lastOn)
    }

    private fun unlockLadder(ladder: Ladder) {
        ladderRepository.updateStatus(listOf(ladder.id), LadderStatus.IN_PROGRESS)
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LadderStepJob::class.java)
    }
}