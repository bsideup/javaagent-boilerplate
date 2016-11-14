@Grab("com.sparkjava:spark-core:2.1")
import static spark.Spark.*

get("/hello/*") { req, res -> "Hello!" }
