package org.veegres.invest.ladder.controller

import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import org.veegres.invest.ladder.dto.AccountDto
import org.veegres.invest.ladder.dto.InstrumentDto
import org.veegres.invest.ladder.dto.LadderDto
import org.veegres.invest.ladder.service.LadderService

@Controller("/investment/ladders")
class LadderController(
    private val ladderService: LadderService
) {

    // TODO move to different controller
    @Get(uri = "/accounts", produces = [MediaType.APPLICATION_JSON])
    fun getAccounts(): List<AccountDto> {
        return ladderService.getAccounts()
    }

    // TODO move to different controller
    @Get(uri = "/instruments", produces = [MediaType.APPLICATION_JSON])
    fun findInstruments(@QueryValue query: String): List<InstrumentDto> {
        return ladderService.findInstruments(query)
    }

    @Post(uri = "/", produces = [MediaType.APPLICATION_JSON])
    @Status(HttpStatus.CREATED)
    fun createLadder(@Body ladderDto: LadderDto) {
        return ladderService.createLadder(ladderDto)
    }

    @Get(uri = "/", produces = [MediaType.APPLICATION_JSON])
    fun getLadders(): List<LadderDto> {
        return ladderService.getLadders()
    }

    @Post(uri = "/test")
    fun test() {
        return ladderService.test()
    }
}