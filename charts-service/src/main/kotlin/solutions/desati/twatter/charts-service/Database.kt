package solutions.desati.twatter.`charts-service`

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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

fun ConnectDatabase() {
  val config = HikariConfig()
  config.jdbcUrl = "jdbc:pgsql://localhost:5432"
  config.username = "charts"
  config.dataSourceClassName = "com.impossibl.postgres.jdbc.PGDataSource"
  val hikariDataSource = HikariDataSource(config)
  Database.connect(hikariDataSource)

  transaction {
    SchemaUtils.createMissingTablesAndColumns(
      Charts
    )
    println("Database connected")
  }
}
