package org.veegres.invest.ladder.service

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import org.veegres.invest.client.tinkoff.converter.toBigDecimal
import org.veegres.invest.ladder.entity.Ladder
import org.veegres.invest.ladder.entity.LadderDirection
import org.veegres.invest.ladder.entity.LadderStep
import org.veegres.invest.ladder.job.LadderStepJob
import ru.tinkoff.piapi.contract.v1.OrderDirection
import ru.tinkoff.piapi.contract.v1.OrderType
import ru.tinkoff.piapi.contract.v1.OrdersServiceGrpc.OrdersServiceBlockingStub
import ru.tinkoff.piapi.contract.v1.PostOrderRequest
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Singleton
class LadderStepService(
    private val ordersService: OrdersServiceBlockingStub
) {

    fun execute(step: Step): StepExecuted {
        val orderId = UUID.randomUUID().toString()
        val request = PostOrderRequest
            .newBuilder()
            .setQuantity(step.stepQuantity)
            .setAccountId(step.accountId)
            .setInstrumentId(step.instrumentId.toString())
            .setOrderId(orderId)
            .setDirection(OrderDirection.ORDER_DIRECTION_BUY)
            .setOrderType(OrderType.ORDER_TYPE_BESTPRICE)
            .build()
        val response = ordersService.postOrder(request)
        val executed = StepExecuted(
            orderId = response.orderId,
            price = response.executedOrderPrice.toBigDecimal()
        )
        LOG.info("Step $step was executed $executed")
        return executed
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LadderStepJob::class.java)
    }
}

data class Step(
    val ladderId: UUID,
    val accountId: String,
    val instrumentId: UUID,
    val stepQuantity: Long,
    val direction: LadderDirection,
)

fun Ladder.toStep(): Step {
    return Step(
        this.id,
        this.accountId,
        this.instrumentId,
        this.stepQuantity,
        this.direction
    )
}

data class StepExecuted(
    val orderId: String,
    val price: BigDecimal
)

fun StepExecuted.toLadderStep(ladderId: UUID): LadderStep {
    return LadderStep(
        id = UUID.randomUUID(),
        ladderId = ladderId,
        orderId = this.orderId,
        createdOn = Instant.now()
    )
}