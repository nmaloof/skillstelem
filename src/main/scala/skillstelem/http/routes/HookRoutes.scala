package skillstelem.http.routes

import cats.effect.IO
import io.circe.parser.decode
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.typelevel.log4cats.LoggerFactory

import skillstelem.algebras.HookAlg
import skillstelem.domain.{AcceptedHooks, PostToolUse}

class HookRoutes(hookAlg: HookAlg)(using LoggerFactory[IO]) extends Http4sDsl[IO] {

   private val logger = LoggerFactory.getLogger

   private val prefixPath = "/hook"

   private val insecureRoutes = HttpRoutes.of[IO] {
      case GET -> Root        => Ok("Got")
      case req @ POST -> Root =>
         for {
            raw  <- req.as[String]
            resp <- decode[AcceptedHooks](raw) match {
               case Left(err)    => logger.error(err)(s"Failed to decode Hook Payload: $raw") *> BadRequest()
               case Right(value) => {
                  PostToolUse.fromAccepted(value) match {
                     case Some(ptu) => logger.info(s"Hook decoded from ${ptu.source}") *> hookAlg.recordToolUse(ptu) *> Accepted()
                     case None      => Accepted()
                  }
               }
            }
         } yield resp
   }

   val routes = Router(prefixPath -> insecureRoutes)

}
