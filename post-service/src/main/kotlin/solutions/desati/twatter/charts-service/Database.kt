package solutions.desati.twatter.`post-service`

import io.vertx.kotlin.core.json.jsonObjectOf
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentTimestamp
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import solutions.desati.twatter.`charts-service`.Env

object Posts : LongIdTable() {
  val author = long("author")
  val content = varchar("content", 1000)
  val created = datetime("create").defaultExpression(CurrentTimestamp())
}

class Post(id: EntityID<Long>) : LongEntity(id) {
  companion object : LongEntityClass<Post>(Posts)

  var author by Posts.author
  var content by Posts.content
  var created by Posts.created

  val view get() = jsonObjectOf(
    "id" to this.id.value,
    "author" to this.author,
    "content" to this.content,
    "created" to this.created.toString(),
    "charts" to PostCharts.select { PostCharts.post eq id }.map { it[PostCharts.chart] }
  )
}

object PostCharts : Table() {
  val post = reference("post", Posts)
  val chart = long("chart")

  override val primaryKey = PrimaryKey(arrayOf(post, chart))
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
      Posts,
      PostCharts,
    )
  }

  println("Database connected")
}
