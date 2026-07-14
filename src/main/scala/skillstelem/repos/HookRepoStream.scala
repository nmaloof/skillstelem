package skillstelem.repos

import cats.effect.std.Queue
import cats.effect.{IO, Resource}
import doobie.Transactor
import org.typelevel.log4cats.LoggerFactory
import scala.concurrent.duration.DurationInt

import skillstelem.algebras.HookAlg
import skillstelem.domain.PostToolUse

class HookRepoStream(queue: Queue[IO, PostToolUse]) extends HookAlg {

   override def recordToolUse(toolUse: PostToolUse): cats.effect.IO[Unit] = queue.offer(toolUse)

}

object HookRepoStream {

   def make(xa: Transactor[IO])(using LoggerFactory[IO]) = {
      val repo   = new HookRepo(xa)
      val logger = LoggerFactory.getLogger

      for {
         queue <- Resource.eval(Queue.bounded[IO, PostToolUse](4096))
         consumer = fs2.Stream
            .fromQueueUnterminated(queue)
            .groupWithin(100, 10.second)
            .evalMap { chunk =>
               repo
                  .recordToolUseBatch(chunk.toList)
                  .handleErrorWith(e => logger.error(e)(s"Failed to write batch of ${chunk.size}"))
                  .flatTap(_ => logger.debug(s"Wrote batch of size: ${chunk.size}"))
            }
            .compile
            .drain
         _ <- Resource.make(consumer.start)(_.cancel)
      } yield HookRepoStream(queue)
   }
}
