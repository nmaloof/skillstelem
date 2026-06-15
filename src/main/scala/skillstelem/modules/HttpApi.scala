package skillstelem.modules

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.typelevel.log4cats.LoggerFactory

import skillstelem.http.routes.HookRoutes

class HttpApi(algebras: Algebras)(using LoggerFactory[IO]) {
   private val hookRoutes = new HookRoutes(algebras.hook)

   private val middleware = { (http: HttpRoutes[IO]) => http }

   private val allRoutes = middleware(hookRoutes.routes)

   val httpApp = Router("/api/v1/" -> allRoutes)
}
