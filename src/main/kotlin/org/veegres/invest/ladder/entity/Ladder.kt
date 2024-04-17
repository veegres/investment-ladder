package org.veegres.invest.ladder.entity

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import org.veegres.invest.ladder.dto.LadderDto
import java.math.BigDecimal
import java.time.Duration
import java.time.Instant
import java.util.*

@MappedEntity
data class Ladder(
    @Id
    val id: UUID,
    val accountId: String,
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
) {

    companion object {
        fun fromDto(dto: LadderDto): Ladder {
            return Ladder(
                id = UUID.randomUUID(),
                accountId = dto.accountId,
                instrumentId = dto.instrumentId,
                stepQuantity = dto.stepQuantity,
                stepInterval = Duration.ofSeconds(dto.stepInterval),
                type = dto.type,
                direction = dto.direction,
                status = LadderStatus.IN_PROGRESS,
                startTime = dto.startTime,
                endTime = dto.endTime,
                firstOrderPrice = null,
                firstOrderOn = null,
                lastOrderPrice = null,
                lastOrderOn = null
            )
        }
    }
}

enum class LadderStatus {
    IN_PROGRESS, CANCELLED, ENDED
}

enum class LadderDirection {
    BUY, SELL
}

enum class LadderType {
    UP, DOWN, NO_MATTER
}

@JdbcRepository(dialect = Dialect.H2)
interface LadderRepository : CrudRepository<Ladder, UUID>