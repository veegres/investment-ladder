package org.veegres.invest.ladder.controller

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import org.veegres.invest.ladder.client.tinkoff.contracts.TinkoffClient
import org.veegres.invest.ladder.user.User
import org.veegres.invest.ladder.user.UserRepository

@Controller("/invest-ladder")
class InvestLadderController(private val tinkoffClient: TinkoffClient, val userRepository: UserRepository) {

    @Get(uri="/one")
    fun index(): String {
        return tinkoffClient.getAccounts().toString()
    }

    @Get(uri="/two", produces = [MediaType.APPLICATION_JSON])
    fun index2(): List<User> {
        return userRepository.findAll()
    }

    @Post(uri="/tree", produces = [MediaType.APPLICATION_JSON])
    fun index3(@Body user: User): User {
        return userRepository.save(user)
    }
}