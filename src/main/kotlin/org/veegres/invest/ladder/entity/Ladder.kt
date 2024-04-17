package org.veegres.invest.ladder.entity

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import jakarta.transaction.Transactional
import org.veegres.invest.ladder.dto.LadderDto
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.util.*

@MappedEntity
data class Ladder(
    @Id
    val id: UUID,
    val accountId: UUID,
    val instrumentId: UUID,
    val stepQuantity: Long,
    val stepInterval: Duration,
    val type: LadderType,
    val direction: LadderDirection,
    val status: LadderStatus,
    val startTime: Instant,
    val endTime: Instant,
    val firstOrderPrice: BigDecimal?,
    val firstOrderOn: Instant?,
    val lastOrderPrice: BigDecimal?,
    val lastOrderOn: Instant?
)

fun LadderDto.toLadder(): Ladder {
    return Ladder(
        id = UUID.randomUUID(),
        accountId = this.accountId,
        instrumentId = this.instrumentId,
        stepQuantity = this.stepQuantity,
        stepInterval = Duration.ofSeconds(this.stepInterval),
        type = this.type,
        direction = this.direction,
        status = LadderStatus.IN_PROGRESS,
        startTime = this.startTime,
        endTime = this.endTime,
        firstOrderPrice = null,
        firstOrderOn = null,
        lastOrderPrice = null,
        lastOrderOn = null
    )
}

enum class LadderStatus {
    IN_PROGRESS, LOCKED, CANCELLED,
}

enum class LadderDirection {
    BUY, SELL
}

enum class LadderType {
    NO_MATTER, // TODO UP, DOWN,
}

@JdbcRepository(dialect = Dialect.H2)
abstract class LadderRepository : CrudRepository<Ladder, UUID> {

    @Query("SELECT * FROM LADDER l WHERE l.STATUS = :status AND l.START_TIME <= :now AND :now <= l.END_TIME FOR UPDATE", nativeQuery = true)
    abstract fun findLadders(now: Instant, status: LadderStatus): List<Ladder>

    @Query("UPDATE LADDER l SET l.STATUS = :status WHERE l.ID IN (:ids)", nativeQuery = true)
    abstract fun updateStatus(ids: List<UUID>, status: LadderStatus)

    @Transactional
    open fun findLaddersAndLock(now: Instant, status: LadderStatus): List<Ladder> {
        val ladders = findLadders(now, status)
        val ids = ladders.map { it.id }
        updateStatus(ids, LadderStatus.LOCKED)
        return ladders
    }

    @Query("UPDATE LADDER l " +
           "SET l.STATUS = :status, " +
           "    l.FIRST_ORDER_PRICE = :firstOrderPrice, l.FIRST_ORDER_ON = :firstOrderOn, " +
           "    l.LAST_ORDER_PRICE = :lastOrderPrice, l.LAST_ORDER_ON = :lastOrderOn " +
           "WHERE l.ID = :id", nativeQuery = true)
    abstract fun updateByStepAndUnlock(id: UUID, status: LadderStatus, firstOrderPrice: BigDecimal, firstOrderOn: Instant, lastOrderPrice: BigDecimal, lastOrderOn: Instant)
}