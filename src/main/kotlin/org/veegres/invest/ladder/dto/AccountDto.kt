package org.veegres.invest.ladder.dto

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AccountDto(
    val id: String,
    val name: String,
    val type: String,
    val status: String
)