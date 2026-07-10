package skillstelem

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.zaxxer.hikari.HikariConfig
import doobie.Transactor
import doobie.hikari.HikariTransactor
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.{LoggerFactory, slf4j}

import skillstelem.modules.{Algebras, HttpApi}

object Main extends IOApp {

   given LoggerFactory[IO] = slf4j.Slf4jFactory.create[IO]

   override def run(args: List[String]): IO[ExitCode] = {
      val app = for {
         config <- Resource.eval(AppConfig.config.load[IO])
         xa     <- makeTransactor(config.dbConfig)
         _      <- makeServer(config.apiConfig, xa)
      } yield ()

      app.useForever.as(ExitCode.Success)
   }

   def makeServer(apiConfig: ApiConfig, xa: Transactor[IO]) = {
      EmberServerBuilder
         .default[IO]
         .withHost(apiConfig.host)
         .withPort(apiConfig.port)
         .withHttp2
         .withHttpApp(HttpApi(Algebras.makeLocal(xa)).httpApp.orNotFound)
         .build
   }

   def makeTransactor(dbConfig: DatabaseConfig) = {
      val config = {
         val c = new HikariConfig()
         c.setDriverClassName("org.sqlite.JDBC")
         c.setJdbcUrl(dbConfig.url)
         c.setUsername(dbConfig.username)
         c.setPassword(dbConfig.password)
         c
      }
      HikariTransactor.fromHikariConfig[IO](config)
   }
}
