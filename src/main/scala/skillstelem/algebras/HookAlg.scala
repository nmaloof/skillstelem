package skillstelem.algebras

import cats.effect.IO

import skillstelem.domain.PostToolUse

trait HookAlg {
   def recordToolUse(toolUse: PostToolUse): IO[Unit]
}
