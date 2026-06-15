package skillstelem

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s.{ipv4, port}
import org.http4s.ember.server.EmberServerBuilder
import org.typelevel.log4cats.{LoggerFactory, slf4j}

import skillstelem.modules.{Algebras, HttpApi}

object Main extends IOApp {

   given LoggerFactory[IO]                            = slf4j.Slf4jFactory.create[IO]
   override def run(args: List[String]): IO[ExitCode] = {
      for {
         _ <- makeServer.useForever
      } yield ExitCode.Success
   }

   def makeServer = {
      EmberServerBuilder
         .default[IO]
         .withHost(ipv4"0.0.0.0")
         .withPort(port"9090")
         .withHttp2
         .withHttpApp(HttpApi(Algebras.makeLocal).httpApp.orNotFound)
         .build
   }
}
