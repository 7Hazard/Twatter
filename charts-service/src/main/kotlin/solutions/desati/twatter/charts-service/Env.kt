package solutions.desati.twatter.`charts-service`

object Env {
  val isContainer = System.getenv("IS_CONTAINER") != null
  val dbHost = System.getenv("TWATTER_DB_HOST") ?: "localhost:5432"
}
