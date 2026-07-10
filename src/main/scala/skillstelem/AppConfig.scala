package skillstelem

import cats.syntax.all.*
import ciris.*
import ciris.http4s.*
import com.comcast.ip4s.{ipv4, port, Host, Port}

final case class ApiConfig(host: Host, port: Port)
final case class DatabaseConfig(url: String, username: String, password: String)

final case class AppConfig(apiConfig: ApiConfig, dbConfig: DatabaseConfig)

object AppConfig {

   val apiConfig = (
     env("API_HOST").as[Host].default(ipv4"0.0.0.0"),
     env("API_PORT").as[Port].default(port"9090")
   ).parMapN(ApiConfig.apply)

   val dbConfig = (
     env("DB_URL").default("jdbc:sqlite:local.db"),
     env("DB_USER").default(""),
     env("DB_PASS").default("")
   ).parMapN(DatabaseConfig.apply)

   def config = (apiConfig, dbConfig).parMapN(AppConfig.apply)
}
