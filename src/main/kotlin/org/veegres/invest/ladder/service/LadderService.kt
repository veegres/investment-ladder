package org.veegres.invest.ladder.service

import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import org.veegres.invest.ladder.dto.AccountDto
import org.veegres.invest.ladder.dto.InstrumentDto
import org.veegres.invest.ladder.dto.LadderDto
import org.veegres.invest.ladder.entity.Ladder
import org.veegres.invest.ladder.entity.LadderRepository
import ru.tinkoff.piapi.contract.v1.*
import ru.tinkoff.piapi.contract.v1.InstrumentsServiceGrpc.InstrumentsServiceBlockingStub
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
    private val instrumentsService: InstrumentsServiceBlockingStub,
    private val ladderRepository: LadderRepository
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

    fun createLadder(ladderDto: LadderDto) {
        val ladder = Ladder.fromDto(ladderDto)
        ladderRepository.save(ladder)
    }

    fun test() {
        val request = PostOrderRequest
            .newBuilder()
            .setQuantity(1)
            .setAccountId("b74112d3-bf74-4013-b5eb-643f02d6c8e0")
            .setInstrumentId("c190ff1f-1447-4227-b543-316332699ca5")
            .setOrderId(UUID.randomUUID().toString())
            .setDirection(OrderDirection.ORDER_DIRECTION_BUY)
            .setOrderType(OrderType.ORDER_TYPE_BESTPRICE)
            .build()
        val response = sandboxService.postSandboxOrder(request)

        LOG.info(response.toString())
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(LadderService::class.java)
    }
}