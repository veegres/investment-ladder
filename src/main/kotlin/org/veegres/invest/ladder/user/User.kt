package org.veegres.invest.ladder.user

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import io.micronaut.serde.annotation.Serdeable
import java.util.*

@Serdeable
@MappedEntity
data class User(@Id val id: UUID, val name: String, val age: Int)

@JdbcRepository(dialect = Dialect.H2)
interface UserRepository : CrudRepository<User, UUID>