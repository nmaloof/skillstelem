package skillstelem.modules

import cats.effect.IO
import cats.syntax.all.*
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.server.middleware.{ErrorAction, RequestLogger}
import org.typelevel.log4cats.LoggerFactory

import skillstelem.http.routes.{HookRoutes, MetricsRoutes}

class HttpApi(algebras: Algebras)(using LoggerFactory[IO]) {

   private val logger = LoggerFactory.getLogger

   private val hookRoutes    = new HookRoutes(algebras.hook)
   private val metricsRoutes = new MetricsRoutes(algebras.metrics)

   private val middleware = { (http: HttpRoutes[IO]) =>
      RequestLogger.httpRoutes[IO](
        logHeaders = false,
        logBody = false,
        redactHeadersWhen = _ => false,
        logAction = Some((msg: String) => logger.info(msg))
      )(http)
   } andThen { (http: HttpRoutes[IO]) =>
      ErrorAction.httpRoutes(
        http,
        (req, err) => logger.error(err)(s"${req.method} ${req.uri} failed")
      )
   }

   private val allRoutes = middleware(hookRoutes.routes <+> metricsRoutes.routes)

   val httpApp = Router("/api/v1/" -> allRoutes)
}
