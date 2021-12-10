package solutions.desati.twatter.`charts-service`

import io.vertx.core.json.JsonObject
import io.vertx.kotlin.core.json.jsonObjectOf
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object Charts : LongIdTable() {
  val data = varchar("data", 100000)
}

class Chart(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<Chart>(Charts)

  var data by Charts.data

  val view get() = jsonObjectOf(
    "id" to this.id.value,
    "data" to JsonObject(this.data),
  )
}

fun connectDatabase() {
  Database.connect(
    "jdbc:postgresql://${Env.dbHost}/",
    "org.postgresql.ds.PGSimpleDataSource",
    "postgres",
    "jizzle"
  )

  transaction {
    SchemaUtils.createMissingTablesAndColumns(
      Charts
    )
    println("Database connected")
  }
}
