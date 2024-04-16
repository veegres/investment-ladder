package org.veegres.invest.client.tinkoff

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("client.tinkoff")
data class TinkoffInvestConfiguration @ConfigurationInject constructor(
    val `app-name`: String,
    val `sandbox`: TinkoffInvestSTarget,
    val `read-write`: TinkoffInvestRWTarget
) {
    @ConfigurationProperties("sandbox")
    data class TinkoffInvestSTarget(
        val token: String,
        val host: String,
        val port: Int
    )

    @ConfigurationProperties("read-write")
    data class TinkoffInvestRWTarget(
        val token: String,
        val host: String,
        val port: Int
    )
}

