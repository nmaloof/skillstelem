package skillstelem.local

import cats.effect.{IO, Ref}

import skillstelem.algebras.{HookAlg, MetricsAlg}
import skillstelem.domain.*

object LocalDev {

   class HookPrinter extends HookAlg {
      override def recordToolUse(toolUse: PostToolUse): cats.effect.IO[Unit] = IO.println(toolUse)
   }

   class InMemoryHook(ref: Ref[IO, Metrics]) extends HookAlg {
      override def recordToolUse(toolUse: PostToolUse): IO[Unit] = ???
   }

   class InMemoryMetrics(ref: Ref[IO, Metrics]) extends MetricsAlg {
      override def getSkillCounts(): IO[Map[String, Int]]  = ???
      override def getSourceCounts(): IO[Map[String, Int]] = ???
   }
}
