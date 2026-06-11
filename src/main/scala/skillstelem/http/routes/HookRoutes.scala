package skillstelem.http.routes

import cats.effect.IO
import io.circe.parser.decode
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router

import skillstelem.domain.wire.Claude

class HookRoutes extends Http4sDsl[IO] {

   private val prefixPath = "/hook"

   private val insecureRoutes = HttpRoutes.of[IO] {
      case GET -> Root        => Ok("Got")
      case req @ POST -> Root =>
         for {
            raw <- req.as[String]
            _   <- IO.println(raw)
            _   <- decode[Claude.ClaudeHook](raw) match {
               case Left(value) => IO.println(value.toString())
               case Right(hook) => IO.println(s"Good: $hook")
            }
            resp <- Ok("hello")
         } yield resp
   }

   val routes = Router(prefixPath -> insecureRoutes)

}
