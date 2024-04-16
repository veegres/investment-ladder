package org.veegres.invest.client.tinkoff

import io.grpc.Channel
import io.micronaut.context.annotation.Factory
import jakarta.inject.Named
import jakarta.inject.Singleton
import ru.tinkoff.piapi.contract.v1.*

@Factory
class TinkoffInvestGrpcClientFactory(
    @Named("sandboxChannel")
    private val channel: Channel
) {

    @Singleton
    fun getSandboxServiceSync(): SandboxServiceGrpc.SandboxServiceBlockingStub {
        return SandboxServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun getOperationServiceSync(): OperationsServiceGrpc.OperationsServiceBlockingStub {
        return OperationsServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun getOrderServiceSync(): OrdersServiceGrpc.OrdersServiceBlockingStub {
        return OrdersServiceGrpc.newBlockingStub(channel)
    }

    @Singleton
    fun getUserServiceSync(): UsersServiceGrpc.UsersServiceBlockingStub {
        return UsersServiceGrpc.newBlockingStub(channel)
    }
}
