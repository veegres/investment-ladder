package org.veegres.invest.ladder.entity

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal
import java.math.BigInteger
import java.time.Instant
import java.util.*

@Serdeable
@MappedEntity
data class Ladder(
    @Id
    val id: UUID,
    val accountId: String,
    val instrumentId: UUID,
    val stepQuantity: Long,
    val stepInterval: BigInteger, // to Duration
    val direction: LadderDirection,
    val type: LadderType,
    val startTime: Instant,
    val endTime: Instant,
    val firstPrice: BigDecimal,
    val lastPrice: BigDecimal,
    val status: LadderStatus
)

enum class LadderDirection {
    BUY, SELL
}

enum class LadderType {
    UP, DOWN, NO_MATTER
}

enum class LadderStatus {
    IN_PROGRESS, CANCELLED, ENDED
}

@JdbcRepository(dialect = Dialect.H2)
interface LadderRepository : CrudRepository<Ladder, UUID>