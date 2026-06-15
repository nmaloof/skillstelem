package skillstelem.local

import cats.effect.IO

import skillstelem.algebras.HookAlg
import skillstelem.domain.PostToolUse

object LocalDev {

   class HookPrinter extends HookAlg {
      override def recordToolUse(toolUse: PostToolUse): cats.effect.IO[Unit] = IO.println(toolUse)
   }
}
