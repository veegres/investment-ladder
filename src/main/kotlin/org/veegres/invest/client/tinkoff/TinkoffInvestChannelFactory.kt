package org.veegres.invest.client.tinkoff

import io.grpc.Channel
import io.grpc.ManagedChannel
import io.grpc.Metadata
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.grpc.stub.MetadataUtils
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.annotation.Requires
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.concurrent.TimeUnit

@Factory
class TinkoffInvestChannelFactory(private val config: TinkoffInvestConfiguration) {

    @Singleton
    @Named("sandboxChannel")
    @Requires(property = "client.tinkoff.sandbox.enable", pattern = "true")
    fun sandboxChannel(): Channel {
        val headers = createHeaders(config.`app-name`, config.sandbox.token)
        return createChannel(config.sandbox.host, config.sandbox.port, headers)
    }

    @Singleton
    @Named("writeReadChannel")
    @Requires(property = "client.tinkoff.sandbox.enable", pattern = "false")
    fun writeReadChannel(): Channel {
        val headers = createHeaders(config.`app-name`, config.`read-write`.token)
        return createChannel(config.`read-write`.host, config.`read-write`.port, headers)
    }

    private fun createHeaders(appName: String, token: String): Metadata {
        return Metadata().also {
            val appNameKey = Metadata.Key.of("x-app-name", Metadata.ASCII_STRING_MARSHALLER)
            it.put(appNameKey, appName)
            val authKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
            it.put(authKey, "Bearer $token")
        }
    }

    private fun createChannel(host: String, port: Int, headers: Metadata): Channel {
        return NettyChannelBuilder
            .forAddress(host, port)
            .intercept(MetadataUtils.newAttachHeadersInterceptor(headers))
            .useTransportSecurity()
            .keepAliveTimeout(60, TimeUnit.SECONDS)
            .maxInboundMessageSize(16777216) // 16 Mb
            .build()
    }
}
