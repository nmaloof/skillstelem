package skillstelem

import cats.effect.{ExitCode, IO, IOApp, Resource}
import com.zaxxer.hikari.HikariConfig
import doobie.hikari.HikariTransactor
import fly4s.Fly4s
import fly4s.data.{Fly4sConfig, Locations, ValidatePattern}
import fly4s.implicits.*
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.{slf4j, LoggerFactory}

import skillstelem.modules.{Algebras, HttpApi}

object Main extends IOApp {

   given LoggerFactory[IO] = slf4j.Slf4jFactory.create[IO]

   override def run(args: List[String]): IO[ExitCode] = {
      val logger = LoggerFactory.getLogger

      val app = for {
         config <- Resource.eval(AppConfig.config.load[IO]).evalTap{ conf => 
            logger.info(s"Server listening on: '${conf.apiConfig.host}:${conf.apiConfig.port}'")
         }
         xa     <- makeTransactor(config.dbConfig)
         _      <- makeFlyway(config.dbConfig).evalMap(_.validateAndMigrate.result)
         algs   <- Algebras.makeLocal(xa)
         httpApi = new HttpApi(algs)
         _ <- makeServer(config.apiConfig, httpApi)
      } yield ()

      app.useForever.as(ExitCode.Success)
   }

   def makeServer(apiConfig: ApiConfig, httpApi: HttpApi) = {
      EmberServerBuilder
         .default[IO]
         .withHost(apiConfig.host)
         .withPort(apiConfig.port)
         .withHttp2
         .withHttpApp(httpApi.httpApp.orNotFound)
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

   def makeFlyway(dbConfig: DatabaseConfig) = Fly4s.make[IO](
     url = dbConfig.url,
     user = Some(dbConfig.username),
     password = Some(dbConfig.password.toCharArray),
     config = Fly4sConfig(
       table = "flyway",
       locations = Locations("db"),
       ignoreMigrationPatterns = List(ValidatePattern.ignorePendingMigrations)
     )
   )
}
