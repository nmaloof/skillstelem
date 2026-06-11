package skillstelem.domain

trait Hook

enum Source(val src: String):
   case Claude extends Source("claude")
   case Cursor extends Source("cursor")

case class PostToolUse(
    source: Source,
    sessionId: String,
    skillName: String
) extends Hook
