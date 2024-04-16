package org.veegres.invest.ladder.client.tinkoff.contracts

import io.grpc.Channel
import jakarta.inject.Named
import jakarta.inject.Singleton
import ru.tinkoff.piapi.contract.v1.*

@Singleton
class TinkoffClient(
    @Named("sandboxChannel")
    private val channel: Channel

) {

    fun getAccounts(): OpenSandboxAccountResponse? {
        val service = SandboxServiceGrpc.newBlockingStub(channel)
        val request = OpenSandboxAccountRequest.newBuilder().build()
        val tt = service.openSandboxAccount(request)
        return tt
    }
}
