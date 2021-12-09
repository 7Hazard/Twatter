package solutions.desati.twatter.`post-service`

import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.core.json.jsonArrayOf
import io.vertx.kotlin.core.json.jsonObjectOf
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import solutions.desati.twatter.`charts-service`.json

fun routes() {
  router.post("/posts").handler(BodyHandler.create()).respond {
    val json = it.bodyAsJsonArray

    transaction {
      val posts = json.map {
        it as JsonObject

        // Make post
        val newPost = Post.new {
          author = it.getLong("author")
          content = it.getString("content")
        }

        // Add charts
        for (chartId in it.getJsonArray("charts", jsonArrayOf())) {
          chartId as Int
          PostCharts.insert {
            it[post] = newPost.id
            it[chart] = chartId.toLong()
          }
        }

        newPost.view
      }

      it.response().json(posts)
    }
  }

  router.get("/posts/:id").respond {
    val id = it.pathParam("id").toLong()
    transaction {
      val post = Post.find { Posts.id eq id }.single().view
      it.response().json(post)
    }
  }

  router.get("/posts").respond {
    val ids = it.queryParam("id").map { it.toLong() }
    val authors = it.queryParam("author").map { it.toLong() }

    val posts = transaction {
      val query = Posts.selectAll().orderBy(Posts.created to SortOrder.DESC)

      if(ids.isNotEmpty()) query.andWhere { Posts.id inList ids }
      if(authors.isNotEmpty()) query.andWhere { Posts.author inList authors }

      Post.wrapRows(query).map { it.view }
    }

    it.response().json(posts)
  }
}
