package org.veegres.invest.ladder.service

import jakarta.inject.Singleton
import ru.tinkoff.piapi.contract.v1.OpenSandboxAccountRequest
import ru.tinkoff.piapi.contract.v1.OpenSandboxAccountResponse
import ru.tinkoff.piapi.contract.v1.OperationsServiceGrpc.OperationsServiceBlockingStub
import ru.tinkoff.piapi.contract.v1.OrdersServiceGrpc.OrdersServiceBlockingStub
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc.SandboxServiceBlockingStub
import ru.tinkoff.piapi.contract.v1.UsersServiceGrpc.UsersServiceBlockingStub

@Singleton
class LadderService(
    private val sandboxService: SandboxServiceBlockingStub,
    private val operationService: OperationsServiceBlockingStub,
    private val ordersService: OrdersServiceBlockingStub,
    private val usersService: UsersServiceBlockingStub
) {

    fun openSandboxAccount(): OpenSandboxAccountResponse {
        val request = OpenSandboxAccountRequest.newBuilder().build()
        return sandboxService.openSandboxAccount(request)
    }
}