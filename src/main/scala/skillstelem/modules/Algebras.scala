package skillstelem.modules

import cats.effect.{IO, Ref}
import doobie.Transactor
import org.typelevel.log4cats.LoggerFactory

import skillstelem.algebras.*
import skillstelem.domain.*
import skillstelem.local.LocalDev
import skillstelem.repos.{HookRepoStream, MetricsRepo}

class Algebras(val hook: HookAlg, val metrics: MetricsAlg) {}

object Algebras {
   def makeInMemory = {
      val metrics = Metrics()
      val r       = Ref.unsafe[IO, Metrics](metrics)
      new Algebras(LocalDev.InMemoryHook(r), LocalDev.InMemoryMetrics(r))
   }

   def makeLocal(xa: Transactor[IO])(using LoggerFactory[IO]) = {
      for {
         hr <- HookRepoStream.make(xa)
         mr = MetricsRepo(xa)
      } yield Algebras(hr, mr)
      // new Algebras(HookRepo(xa), MetricsRepo(xa))
   }
}
