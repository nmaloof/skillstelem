package skillstelem.modules

import cats.effect.{IO, Ref}
import doobie.Transactor

import skillstelem.algebras.*
import skillstelem.domain.*
import skillstelem.local.LocalDev
import skillstelem.repos.{HookRepo, MetricsRepo}

class Algebras(val hook: HookAlg, val metrics: MetricsAlg) {}

object Algebras {
   def makeInMemory = {
      val metrics = Metrics()
      val r       = Ref.unsafe[IO, Metrics](metrics)
      new Algebras(LocalDev.InMemoryHook(r), LocalDev.InMemoryMetrics(r))
   }

   def makeLocal(xa: Transactor[IO]) = {
      new Algebras(HookRepo(xa), MetricsRepo(xa))
   }
}
