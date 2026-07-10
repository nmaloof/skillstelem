package skillstelem.algebras

import cats.effect.IO

import skillstelem.domain.Metric

trait MetricsAlg {
   def getSkillCounts(): IO[Metric[Int]]
   def getSourceCounts(): IO[Metric[Int]]
}
