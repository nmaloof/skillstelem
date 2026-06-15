package skillstelem.repos

import cats.effect.IO
import doobie.*
import doobie.implicits.*

import skillstelem.algebras.HookAlg
import skillstelem.domain.PostToolUse

class HookRepo(xa: Transactor[IO]) extends HookAlg {

   import HookRepoSQL.*

   override def recordToolUse(toolUse: PostToolUse): cats.effect.IO[Unit] = insertToolUse(toolUse).run.transact(xa).void

   def recordToolUseBatch(batch: List[PostToolUse]): IO[Unit] = {
      val rows = batch.map(x => (x._1.src, x._2, x._3))
      insertToolUseBatch.updateMany(rows).transact(xa).void
   }

}

private object HookRepoSQL {

   def insertToolUse(toolUse: PostToolUse): Update0 = sql"""
        insert into tool_calls (source, session_id, skill_name) values
        (${toolUse.source.src}, ${toolUse.sessionId}, ${toolUse.skillName})
    """.update

   val insertToolUseBatch = Update[(String, String, String)]("insert into tool_calls (source, session_id, skill_name) values (?,?,?)")
}
