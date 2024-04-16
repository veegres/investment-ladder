package org.veegres.invest.ladder.client.tinkoff.contracts

import io.grpc.Channel
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder
import io.grpc.stub.MetadataUtils
import io.micronaut.context.annotation.Factory
import jakarta.inject.Named
import jakarta.inject.Singleton
import java.util.concurrent.TimeUnit

@Factory
class ChannelFactory {

    @Singleton
    @Named("sandboxChannel")
    fun sandboxChannel(): Channel {
        val headers = io.grpc.Metadata().also {
            val appNameKey = io.grpc.Metadata.Key.of("x-app-name", io.grpc.Metadata.ASCII_STRING_MARSHALLER)
            it.put(appNameKey, APP_NAME)
            val authKey = io.grpc.Metadata.Key.of("Authorization", io.grpc.Metadata.ASCII_STRING_MARSHALLER)
            it.put(authKey, "Bearer $TOKEN")
        }

        val channel = NettyChannelBuilder
            .forAddress("sandbox-invest-public-api.tinkoff.ru", 443)
            .intercept(MetadataUtils.newAttachHeadersInterceptor(headers))
            .useTransportSecurity()
            .keepAliveTimeout(60, TimeUnit.SECONDS)
            .maxInboundMessageSize(16777216) // 16 Mb
            .build()
        return channel
    }

    @Singleton
    @Named("writeReadChannel")
    fun writeReadChannel(): Channel {
        val headers = io.grpc.Metadata().also {
            val appNameKey = io.grpc.Metadata.Key.of("x-app-name", io.grpc.Metadata.ASCII_STRING_MARSHALLER)
            it.put(appNameKey, APP_NAME)
            val authKey = io.grpc.Metadata.Key.of("Authorization", io.grpc.Metadata.ASCII_STRING_MARSHALLER)
            it.put(authKey, "Bearer $TOKEN")
        }

        val channel = NettyChannelBuilder
            .forAddress("invest-public-api.tinkoff.ru", 443)
            .intercept(MetadataUtils.newAttachHeadersInterceptor(headers))
            .useTransportSecurity()
            .keepAliveTimeout(60, TimeUnit.SECONDS)
            .maxInboundMessageSize(16777216) // 16 Mb
            .build()
        return channel
    }

    companion object {
        private const val TOKEN = "t.QBrWuL40hXjaxcCbqxF5OobN9_oFCfh-0co_A3_zTGOT2Zcm3BupHOxTGB2ouey25myZA1FqMNTxittPSsG7Mg"
        private const val APP_NAME = "invest-ladder"
    }
}
