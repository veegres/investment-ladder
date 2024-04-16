package org.veegres.invest.ladder.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import org.veegres.invest.ladder.dto.AccountDto
import org.veegres.invest.ladder.dto.InstrumentDto
import org.veegres.invest.ladder.service.LadderService
import org.veegres.invest.ladder.user.User
import org.veegres.invest.ladder.user.UserRepository

@Controller("/invest/ladder")
class LadderController(private val ladderService: LadderService, val userRepository: UserRepository) {

    @Get(uri = "/accounts",  produces = [MediaType.APPLICATION_JSON])
    fun getAccounts(): List<AccountDto> {
        return ladderService.getAccounts()
    }

    @Get(uri = "/instruments",  produces = [MediaType.APPLICATION_JSON])
    fun findInstruments(@QueryValue query: String): List<InstrumentDto> {
        return ladderService.findInstruments(query)
    }

    @Get(uri = "/two", produces = [MediaType.APPLICATION_JSON])
    fun index2(): List<User> {
        return userRepository.findAll()
    }

    @Post(uri = "/tree", produces = [MediaType.APPLICATION_JSON])
    fun index3(@Body user: User): User {
        return userRepository.save(user)
    }

    @Post(uri = "/test")
    fun test() {
        return ladderService.test()
    }
}