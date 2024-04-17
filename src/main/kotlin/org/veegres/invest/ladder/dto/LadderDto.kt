package org.veegres.invest.ladder.dto

import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import org.veegres.invest.ladder.entity.LadderDirection
import org.veegres.invest.ladder.entity.LadderType
import java.time.Instant
import java.util.*

@Serdeable
data class LadderDto(
    val accountId: UUID,
    val instrumentId: UUID,
    val stepQuantity: Long,
    @Schema(description="Interval in seconds")
    val stepInterval: Long,
    val direction: LadderDirection,
    val type: LadderType,
    val startTime: Instant,
    val endTime: Instant
)

