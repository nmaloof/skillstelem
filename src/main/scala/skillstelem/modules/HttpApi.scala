package skillstelem.modules

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.server.Router

import skillstelem.http.routes.HookRoutes

class HttpApi {
   private val hookRoutes = new HookRoutes()

   private val middleware = { (http: HttpRoutes[IO]) => http }

   private val allRoutes = middleware(hookRoutes.routes)

   val httpApp = Router("/api/v1/" -> allRoutes)
}
