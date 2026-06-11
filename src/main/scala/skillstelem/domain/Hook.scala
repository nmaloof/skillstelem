package skillstelem.domain

trait Hook

enum Source(val src: String):
   case Claude extends Source("claude")
   case Cursor extends Source("cursor")
