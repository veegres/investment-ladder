package org.veegres.invest.ladder.entity

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.time.Instant
import java.util.*

@MappedEntity
data class LadderStep(
    @Id
    val id: UUID,
    val ladderId: UUID,
    val orderId: String,
    val createdOn: Instant,
)

@JdbcRepository(dialect = Dialect.H2)
interface LadderStepRepository : CrudRepository<LadderStep, UUID>