package skillstelem.repos

import cats.effect.IO
import doobie.*
import doobie.implicits.*

import skillstelem.algebras.MetricsAlg
import skillstelem.domain.Metric

class MetricsRepo(xa: Transactor[IO]) extends MetricsAlg {

   import MetricsRepoSQL.*

   override def getSkillCounts(): IO[Metric[Int]]  = totalCountsQuery.to[List].map(_.toMap).transact(xa)
   override def getSourceCounts(): IO[Metric[Int]] = skillCountsQuery.to[List].map(_.toMap).transact(xa)

}

private object MetricsRepoSQL {

   val totalCountsQuery = sql"""
        select skill_name, count(*)
        from tool_calls
        group by skill_name
    """.query[(String, Int)]

   val skillCountsQuery = sql"""
        select source, count(*)
        from tool_calls
        group by source
    """.query[(String, Int)]
}
