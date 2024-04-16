package org.veegres.invest.ladder.dto

import io.micronaut.serde.annotation.Serdeable
import java.util.UUID

@Serdeable
data class InstrumentDto(
    val name: String,
    val ticker: String,
    val uid: UUID,
    val type: String
)