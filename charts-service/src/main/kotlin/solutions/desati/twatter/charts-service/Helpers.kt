package solutions.desati.twatter.`charts-service`

import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

fun HttpServerResponse.json(view: JsonObject) = this
  .putHeader("content-type", "application/json; charset=utf-8")
  .end(view.encode())

fun HttpServerResponse.json(views: List<JsonObject>) = this
  .putHeader("content-type", "application/json; charset=utf-8")
  .end(JsonArray(views).encode())
