package skillstelem.http.routes

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.circe.CirceEntityEncoder.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import org.typelevel.log4cats.LoggerFactory

import skillstelem.algebras.MetricsAlg

class MetricsRoutes(metricsAlg: MetricsAlg)(using LoggerFactory[IO]) extends Http4sDsl[IO] {

   private val prefixPath = "/metrics"

   private val insecureRoutes = HttpRoutes.of[IO] {
      case GET -> Root                  => Ok("Metrics Route")
      case GET -> Root / "skillCounts"  => metricsAlg.getSkillCounts().flatMap(Ok(_))
      case GET -> Root / "sourceCounts" => metricsAlg.getSourceCounts().flatMap(Ok(_))
   }

   val routes = Router(prefixPath -> insecureRoutes)

}
