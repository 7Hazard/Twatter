package solutions.desati.twatter.`charts-service`

import io.vertx.ext.web.handler.BodyHandler
import org.jetbrains.exposed.sql.transactions.transaction

fun routes() {
  router.post("/charts").handler(BodyHandler.create()).respond {
    val chartsJson = it.bodyAsJsonArray

    val views = transaction {
      chartsJson.map { chart ->
        Chart.new { data = chart.toString() }.view
      }
    }

    it.response().json(views)
  }

  router.get("/charts/:id").respond {
    val id = it.pathParam("id").toLong()

    val view = transaction {
      Chart.find { Charts.id eq id }.single().view
    }

    it.response().json(view)
  }

  router.get("/charts").respond {
    val chartIds = it.queryParam("id").map { it.toLong() }
    val views = transaction {
      Chart.find {
        Charts.id inList chartIds
      }.map { it.view }
    }
    it.response().json(views)
  }
}
