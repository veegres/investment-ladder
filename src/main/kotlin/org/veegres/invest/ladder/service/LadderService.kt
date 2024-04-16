package org.veegres.invest.ladder.service

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import org.veegres.invest.ladder.dto.AccountDto
import org.veegres.invest.ladder.dto.InstrumentDto
import ru.tinkoff.piapi.contract.v1.FindInstrumentRequest
import ru.tinkoff.piapi.contract.v1.GetAccountsRequest
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc.InstrumentsServiceBlockingStub
import ru.tinkoff.piapi.contract.v1.OpenSandboxAccountRequest
import ru.tinkoff.piapi.contract.v1.OpenSandboxAccountResponse
import ru.tinkoff.piapi.contract.v1.OperationsServiceGrpc.OperationsServiceBlockingStub
import ru.tinkoff.piapi.contract.v1.OrdersServiceGrpc.OrdersServiceBlockingStub
import ru.tinkoff.piapi.contract.v1.SandboxServiceGrpc.SandboxServiceBlockingStub
import ru.tinkoff.piapi.contract.v1.UsersServiceGrpc.UsersServiceBlockingStub
import java.util.*

@Singleton
class LadderService(
    private val sandboxService: SandboxServiceBlockingStub,
    private val operationService: OperationsServiceBlockingStub,
    private val ordersService: OrdersServiceBlockingStub,
    private val usersService: UsersServiceBlockingStub,
    private val instrumentsService: InstrumentsServiceBlockingStub
) {

    fun getAccounts(): List<AccountDto> {
        val request = GetAccountsRequest.newBuilder().build()
        val accounts = usersService.getAccounts(request)
        val dtos = accounts.accountsList.map {
            AccountDto(it.id, it.name, it.type.toString(), it.status.toString())
        }
        return dtos
    }

    fun findInstruments(query: String): List<InstrumentDto> {
        val request = FindInstrumentRequest.newBuilder().setQuery(query).build()
        val instruments = instrumentsService.findInstrument(request)
        val dtos = instruments.instrumentsList.map {
            InstrumentDto(it.name, it.ticker, UUID.fromString(it.uid), it.instrumentType)
        }
        return dtos
    }

    fun test() {
        val request = GetAccountsRequest.newBuilder().build()
        val accounts = usersService.getAccounts(request)
        val account = accounts.getAccounts(1).status

        val requestInst = FindInstrumentRequest.newBuilder().setQuery("ОФЗ").build()
        val instruments = instrumentsService.findInstrument(requestInst)
        val tt = instruments.instrumentsCount

        LOG.info(tt.toString())
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LadderService::class.java)
    }
}