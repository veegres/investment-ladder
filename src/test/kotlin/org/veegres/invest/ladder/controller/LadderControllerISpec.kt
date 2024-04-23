package org.veegres.invest.ladder.controller

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.veegres.invest.ladder.dto.LadderDto
import org.veegres.invest.ladder.entity.LadderDirection
import org.veegres.invest.ladder.entity.LadderType
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@MicronautTest
class LadderControllerISpec {

    @Test
    fun `should return 200 OK on GET accounts`(spec: RequestSpecification) {
        spec
            .`when`()
            .get("/investment/ladders/accounts")
            .then()
            .statusCode(200)
            .header("Content-Type", "application/json")
    }

    @Test
    fun `should return 404 Not Found on GET user by ID`(spec: RequestSpecification) {
        spec
            .`when`()
            .get("/investment/ladders/accounts/12341")
            .then()
            .statusCode(404)
    }

    @Test
    fun `should create a ladder`(spec: RequestSpecification) {

        val request = LadderDto(
            accountId = "accountId",
            instrumentId = UUID.randomUUID(),
            stepQuantity = 5,
            stepInterval = 60,
            direction = LadderDirection.BUY,
            type = LadderType.NO_MATTER,
            startTime = Instant.now().minus(1, ChronoUnit.DAYS),
            endTime = Instant.now().plus(2, ChronoUnit.DAYS)
        )
        spec
            .`when`()
            .contentType(ContentType.JSON)
            .body(request)
            .post("/investment/ladders")
            .then()
            .statusCode(201)

        val list = spec
            .`when`()
            .get("/investment/ladders")
            .then()
            .statusCode(200)
            .extract()
            .`as`(object : TypeRef<List<LadderDto>>() {})

        assertEquals(1, list.size)
        val createdLadder = list.first()
        assertEquals(request.accountId, createdLadder.accountId)
        assertEquals(request.instrumentId, createdLadder.instrumentId)
    }
}